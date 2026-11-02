package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.accesos.persistencia.BrokerUsuario;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker de {@code Socio}: herencia JOINED sobre las tablas {@code usuario} (columnas comunes, las maneja
 * {@code BrokerUsuario}) y {@code socio} (columnas propias, {@code tablaHija()} y {@code valoresHija}). Resuelve
 * las asociaciones a uno (categoría y grupo familiar) con {@code fachada().get(...)} en {@code cargarAsociaciones}.
 * {@code BrokerReserva} también lo usa para materializar el socio leído con JOIN
 * ({@code materializarDesdeFila(fila.conPrefijo("s_"))}).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de
 * {@code BrokerBaseDatos}. Bean {@code @Component}; nadie lo llama directamente: se usa {@code FachadaPersistencia}.
 *
 * <p>Casos de uso: todos
 */
@Component
public class BrokerSocio extends BrokerUsuario<Socio> {

    /**
     * Claves foráneas leídas en {@code crearObjeto} y pendientes de resolver en {@code cargarAsociaciones}
     * (instancia del socio → {categoria_id, grupo_familiar_id}). Se indexa por identidad de objeto y está
     * sincronizado porque el broker es un singleton compartido por todas las transacciones; cada entrada vive
     * solo entre los dos pasos de una materialización.
     */
    private final Map<Socio, Long[]> clavesPendientes = Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerSocio(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve la categoría y el grupo familiar con {@code fachada().get(id, Clase)} y los entrega con
     * {@code socio.restaurarAsociaciones(categoria, grupo)}. El socio ya está registrado en el mapa de identidad,
     * así que la asociación circular socio → grupo → titular encuentra la misma instancia.
     *
     * <p>Casos de uso: todos
     * @param s socio recién creado
     */
    @Override
    protected void cargarAsociaciones(Socio s) {
        Long[] claves = clavesPendientes.remove(s);
        if (claves == null) {
            return;
        }
        CategoriaSocio categoria = fachada().get(claves[0], CategoriaSocio.class);
        GrupoFamiliar grupo = fachada().get(claves[1], GrupoFamiliar.class);
        s.restaurarAsociaciones(categoria, grupo);
    }

    /**
     * Tabla hija de la herencia JOINED.
     *
     * <p>Casos de uso: todos
     * @return {@code "socio"}
     */
    @Override
    protected String tablaHija() {
        return "socio";
    }

    /**
     * Columnas propias de la tabla {@code socio} (sin el id, que agrega {@code BrokerUsuario.guardarPartes}). Las
     * asociaciones se pasan como entidades: {@code aParametro} las convierte en su id.
     *
     * <p>Casos de uso: todos
     * @param objeto socio a guardar
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresHija(Socio objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("dni", objeto.getDni());
        valores.put("nombre", objeto.getNombre());
        valores.put("apellido", objeto.getApellido());
        valores.put("fecha_nacimiento", objeto.getFechaNacimiento());
        valores.put("telefono", objeto.getTelefono());
        valores.put("direccion", objeto.getDireccion());
        valores.put("fecha_alta", objeto.getFechaAlta());
        valores.put("categoria_id", objeto.getCategoria());
        valores.put("grupo_familiar_id", objeto.getGrupoFamiliar());
        return valores;
    }

    /**
     * Clase concreta que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseObjeto() {
        return Socio.class;
    }

    /**
     * Tabla principal de la jerarquía: la define {@code BrokerUsuario} ({@code usuario}).
     *
     * <p>Casos de uso: todos
     * @return {@code "usuario"}
     */
    @Override
    protected String tabla() {
        return super.tabla();
    }

    /**
     * Desmaterialización: {@code Socio.reconstituir(...)} con las columnas de {@code usuario} y {@code socio} de la
     * fila (con o sin prefijo); guarda las claves de categoría y grupo para {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param fila fila de {@code usuario JOIN socio}
     * @return socio sin asociaciones
     */
    @Override
    protected Socio crearObjeto(FilaResultado fila) {
        Socio socio = Socio.reconstituir(fila.largo("id"), fila.texto("username"), fila.texto("email"), fila.texto("password_hash"),
                EstadoCuenta.valueOf(fila.texto("estado")), fila.entero("intentos_fallidos"),
                fila.fechaHora("bloqueado_hasta"), fila.fechaHora("fecha_creacion"), fila.texto("dni"),
                fila.texto("nombre"), fila.texto("apellido"), fila.fecha("fecha_nacimiento"), fila.texto("telefono"),
                fila.texto("direccion"), fila.fecha("fecha_alta"));
        clavesPendientes.put(socio, new Long[] {fila.largo("categoria_id"), fila.largo("grupo_familiar_id")});
        return socio;
    }

    /**
     * Columnas del INSERT en {@code usuario}: las define {@code BrokerUsuario} (la fila de {@code socio} la inserta
     * {@code guardarPartes}).
     *
     * <p>Casos de uso: CU-01
     * @param objeto socio nuevo
     * @return mapa ordenado columna → valor de la tabla usuario
     */
    @Override
    protected Map<String, Object> valoresInsercion(Socio objeto) {
        return super.valoresInsercion(objeto);
    }

    /**
     * Columnas del UPDATE en {@code usuario}: las define {@code BrokerUsuario} (la fila de {@code socio} la
     * actualiza {@code guardarPartes}).
     *
     * <p>Casos de uso: CU-02, CU-03, CU-07, CU-09
     * @param objeto socio persistido
     * @return mapa ordenado columna → valor de la tabla usuario
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Socio objeto) {
        return super.valoresActualizacion(objeto);
    }
}
