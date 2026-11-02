package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos del agregado {@code DisponibilidadClub} (tabla {@code disponibilidad_club}, fila
 * única {@code id = 1}, más la tabla dependiente {@code dia_atencion} con el atributo multivaluado
 * {@code diasAtencion}). En {@code cargarAsociaciones} carga siempre los días (consulta de proyección
 * {@code DiasAtencion}) y los feriados (consulta {@code Feriados}); en {@code guardarPartes} borra y vuelve a
 * insertar las filas de {@code dia_atencion} (07C), con el día como número ISO 8601.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
 */
@Component
public class BrokerDisponibilidadClub extends BrokerBaseDatos<DisponibilidadClub> {

    /** Borra los días de atención de la disponibilidad (parte del agregado). */
    private static final String BORRAR_DIAS = "DELETE FROM dia_atencion WHERE dia_atencion.disponibilidad_id = ?";

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param proveedor proveedor de conexiones
     */
    public BrokerDisponibilidadClub(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Gancho del Template Method: carga los días de atención con {@code DiasAtencion(id)} y los feriados con
     * {@code Feriados()} y los entrega con {@code restaurarAsociaciones} (listas chicas: se cargan siempre).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param d disponibilidad recién creada
     */
    @Override
    protected void cargarAsociaciones(DisponibilidadClub d) {
        List<DiaSemana> dias = fachada().buscar(new DiasAtencion(d.getId()));
        Set<DiaSemana> conjunto = dias.isEmpty() ? EnumSet.noneOf(DiaSemana.class) : EnumSet.copyOf(dias);
        List<Feriado> feriados = fachada().buscar(new Feriados());
        d.restaurarAsociaciones(conjunto, feriados);
    }

    /**
     * Gancho del Template Method: guarda la parte {@code dia_atencion} del agregado; borra las filas de la
     * disponibilidad y vuelve a insertar una por día con su número ISO (CU-24 paso 7).
     *
     * <p>Casos de uso: CU-24
     * @param d disponibilidad guardada
     * @param alta true si se acaba de insertar
     */
    @Override
    protected void guardarPartes(DisponibilidadClub d, boolean alta) {
        ejecutarActualizacion(BORRAR_DIAS, List.of(d.getId()));
        for (DiaSemana dia : d.getDiasAtencion()) {
            ejecutarActualizacion("INSERT INTO dia_atencion (disponibilidad_id, dia) VALUES (?, ?)",
                    List.of(d.getId(), dia.numeroIso()));
        }
    }

    /**
     * Gancho del Template Method: borra las filas de {@code dia_atencion} antes de la fila principal (la fila única
     * no se borra en ningún caso de uso, pero así el agregado queda consistente).
     *
     * <p>Casos de uso: CU-24
     * @param d disponibilidad que se borra
     */
    @Override
    protected void borrarPartes(DisponibilidadClub d) {
        ejecutarActualizacion(BORRAR_DIAS, List.of(d.getId()));
    }

    /**
     * Clase que maneja el broker (clave del registro en {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return {@code DisponibilidadClub.class}
     */
    @Override
    public Class<DisponibilidadClub> claseObjeto() {
        return DisponibilidadClub.class;
    }

    /**
     * Gancho del Template Method: tabla principal.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @return nombre de la tabla {@code disponibilidad_club}
     */
    @Override
    protected String tabla() {
        return "disponibilidad_club";
    }

    /**
     * Gancho del Template Method: reconstituye la fila con {@code DisponibilidadClub.reconstituir(id, apertura,
     * cierre)}.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-32
     * @param fila fila de {@code disponibilidad_club}
     * @return la disponibilidad (sin días ni feriados todavía)
     */
    @Override
    protected DisponibilidadClub crearObjeto(FilaResultado fila) {
        return DisponibilidadClub.reconstituir(fila.largo("id"), fila.hora("hora_apertura"), fila.hora("hora_cierre"));
    }

    /**
     * Gancho del Template Method: columnas del INSERT; incluye {@code id = ID_UNICO} (fila única).
     *
     * <p>Casos de uso: CU-24
     * @param objeto disponibilidad
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(DisponibilidadClub objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("id", DisponibilidadClub.ID_UNICO);
        valores.putAll(valoresActualizacion(objeto));
        return valores;
    }

    /**
     * Gancho del Template Method: columnas del UPDATE (horario de apertura y cierre; los días van en
     * {@code guardarPartes}).
     *
     * <p>Casos de uso: CU-24
     * @param objeto disponibilidad persistida
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(DisponibilidadClub objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("hora_apertura", objeto.getHoraApertura());
        valores.put("hora_cierre", objeto.getHoraCierre());
        return valores;
    }
}
