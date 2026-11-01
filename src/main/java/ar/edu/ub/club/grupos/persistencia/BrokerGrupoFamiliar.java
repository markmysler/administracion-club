package ar.edu.ub.club.grupos.persistencia;

import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.compartido.persistencia.ProxyListaVirtual;
import ar.edu.ub.club.cuotas.persistencia.CuotasDeGrupo;
import ar.edu.ub.club.cuotas.persistencia.MovimientosDeGrupo;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.Socio;
import ar.edu.ub.club.socios.persistencia.SociosDeGrupo;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code GrupoFamiliar} sobre la tabla {@code grupo_familiar}. En {@code crearObjeto} crea, sin tocar la
 * base, tres {@code ProxyListaVirtual} (Virtual Proxy) para {@code integrantes} ({@code SociosDeGrupo}),
 * {@code cuotas} ({@code CuotasDeGrupo}) y {@code movimientos} ({@code MovimientosDeGrupo}); resuelve el titular
 * con {@code fachada().get(id, Socio)} y, en {@code guardarPartes}, hace {@code put} de los integrantes cargados
 * para actualizar {@code socio.grupo_familiar_id} (lo que JPA guardaba en cascada).
 *
 * <p>Restricción {@code ck_grupo_titular_vigente} ({@code NOT vigente OR titular_id IS NOT NULL}, no diferible):
 * en el alta de un socio (CU-01) el titular todavía no existe en la base, así que el grupo se inserta con
 * {@code titular_id} nulo y {@code vigente = false}; {@code guardarPartes} inserta al socio (que ya puede
 * referenciar el grupo) y luego completa {@code titular_id} y {@code vigente} con un UPDATE, todo en la misma
 * transacción.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de
 * {@code BrokerBaseDatos}. Bean {@code @Component}; se usa a través de {@code FachadaPersistencia}.
 *
 * <p>Casos de uso: todos
 */
@Component
public class BrokerGrupoFamiliar extends BrokerBaseDatos<GrupoFamiliar> {

    /**
     * Id del titular leído en {@code crearObjeto}, pendiente de resolver en {@code cargarAsociaciones}
     * (por identidad de objeto, sincronizado porque el broker es un singleton compartido).
     */
    private final Map<GrupoFamiliar, Long> titularesPendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerGrupoFamiliar(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Desmaterialización: {@code GrupoFamiliar.reconstituir(id, fechaCreacion, vigente, integrantes, cuotas,
     * movimientos)} con tres {@code ProxyListaVirtual} que se cargan en el primer acceso; guarda el id del titular.
     *
     * <p>Casos de uso: todos
     * @param fila fila de {@code grupo_familiar}
     * @return grupo sin titular
     */
    @Override
    protected GrupoFamiliar crearObjeto(FilaResultado fila) {
        Long id = fila.largo("id");
        GrupoFamiliar grupo = GrupoFamiliar.reconstituir(id, fila.fecha("fecha_creacion"), fila.booleano("vigente"),
                new ProxyListaVirtual<>(fachada(), new SociosDeGrupo(id)),
                new ProxyListaVirtual<>(fachada(), new CuotasDeGrupo(id)),
                new ProxyListaVirtual<>(fachada(), new MovimientosDeGrupo(id)));
        Long idTitular = fila.largo("titular_id");
        if (idTitular != null) {
            titularesPendientes.put(grupo, idTitular);
        }
        return grupo;
    }

    /**
     * Resuelve el titular con {@code fachada().get(idTitular, Socio)} y lo entrega con
     * {@code restaurarAsociaciones(titular)} (si el titular es el socio que se está materializando, el mapa de
     * identidad devuelve esa misma instancia).
     *
     * <p>Casos de uso: todos
     * @param g grupo recién creado
     */
    @Override
    protected void cargarAsociaciones(GrupoFamiliar g) {
        Long idTitular = titularesPendientes.remove(g);
        g.restaurarAsociaciones(idTitular == null ? null : fachada().get(idTitular, Socio.class));
    }

    /**
     * Guarda las partes del agregado: hace {@code put} de cada integrante si la lista está en memoria (lista nueva
     * o proxy ya cargado), lo que inserta al socio nuevo del alta o actualiza su {@code grupo_familiar_id}. En el
     * alta, si el titular no existía al insertar, completa {@code titular_id} y {@code vigente}.
     *
     * <p>Casos de uso: CU-01, CU-03, CU-07, CU-08, CU-09
     * @param g grupo guardado
     * @param alta true si se acaba de insertar
     */
    @Override
    protected void guardarPartes(GrupoFamiliar g, boolean alta) {
        List<Socio> integrantes = g.getIntegrantes();
        boolean cargados = !(integrantes instanceof ProxyListaVirtual<?> proxy) || proxy.estaCargada();
        if (cargados) {
            for (Socio integrante : integrantes) {
                fachada().put(integrante);
            }
        }
        if (alta && g.getTitular() != null && g.getTitular().getId() != null) {
            ejecutarActualizacion("UPDATE grupo_familiar SET titular_id = ?, vigente = ? WHERE id = ?",
                    List.of(g.getTitular().getId(), g.getVigente(), g.getId()));
        }
    }

    /**
     * Clase que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return {@code GrupoFamiliar.class}
     */
    @Override
    public Class<GrupoFamiliar> claseObjeto() {
        return GrupoFamiliar.class;
    }

    /**
     * Tabla de la clase.
     *
     * <p>Casos de uso: todos
     * @return {@code "grupo_familiar"}
     */
    @Override
    protected String tabla() {
        return "grupo_familiar";
    }

    /**
     * Columnas del INSERT. Si el titular todavía no está persistido (alta de socio, CU-01) inserta
     * {@code titular_id} nulo y {@code vigente = false} para respetar {@code ck_grupo_titular_vigente};
     * {@code guardarPartes} completa la fila.
     *
     * <p>Casos de uso: CU-01, CU-09
     * @param objeto grupo nuevo
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(GrupoFamiliar objeto) {
        boolean titularPersistido = objeto.getTitular() != null && objeto.getTitular().getId() != null;
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("titular_id", titularPersistido ? objeto.getTitular().getId() : null);
        valores.put("fecha_creacion", objeto.getFechaCreacion());
        valores.put("vigente", titularPersistido ? objeto.getVigente() : Boolean.FALSE);
        return valores;
    }

    /**
     * Columnas del UPDATE: titular, fecha de creación y vigencia.
     *
     * <p>Casos de uso: CU-03, CU-07, CU-08, CU-09
     * @param objeto grupo persistido
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(GrupoFamiliar objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("titular_id", objeto.getTitular() == null ? null : objeto.getTitular().getId());
        valores.put("fecha_creacion", objeto.getFechaCreacion());
        valores.put("vigente", objeto.getVigente());
        return valores;
    }
}
