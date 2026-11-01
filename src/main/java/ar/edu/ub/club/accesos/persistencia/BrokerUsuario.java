package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.Map;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public abstract class BrokerUsuario<T extends Usuario> extends BrokerBaseDatos<T> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: PENDIENTE
     * @param proveedor proveedor de conexiones
     */
    public BrokerUsuario(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Class<?> claseRaiz() {
        return Usuario.class;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    protected String tabla() {
        return "usuario";
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    protected String consultaBase() {
        return "SELECT usuario.*, " + tablaHija() + ".* FROM usuario JOIN " + tablaHija() + " ON " + tablaHija() + ".id = usuario.id";
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @return el resultado
     */
    protected Map<String, Object> valoresInsercion(T objeto) {
        java.util.Map<String, Object> v = new java.util.LinkedHashMap<>();
        v.put("username", objeto.getUsername());
        v.put("email", objeto.getEmail());
        v.put("password_hash", objeto.getPasswordHash());
        v.put("estado", objeto.getEstado());
        v.put("intentos_fallidos", objeto.getIntentosFallidos());
        v.put("bloqueado_hasta", objeto.getBloqueadoHasta());
        v.put("fecha_creacion", objeto.getFechaCreacion());
        return v;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @return el resultado
     */
    protected Map<String, Object> valoresActualizacion(T objeto) {
        return valoresInsercion(objeto);
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @param alta valor de {@code alta}
     */
    protected void guardarPartes(T objeto, boolean alta) {
        Map<String, Object> hija = valoresHija(objeto);
        java.util.List<Object> param = new java.util.ArrayList<>(hija.values());
        param.add(objeto.getId());
        if (alta) {
            String cols = String.join(", ", hija.keySet());
            String marcas = String.join(", ", java.util.Collections.nCopies(hija.size(), "?"));
            ejecutarActualizacion("INSERT INTO " + tablaHija() + " (" + cols + ", id) VALUES (" + marcas + ", ?)", param);
        } else {
            java.util.List<String> asig = new java.util.ArrayList<>();
            for (String c : hija.keySet()) {
                asig.add(c + " = ?");
            }
            ejecutarActualizacion("UPDATE " + tablaHija() + " SET " + String.join(", ", asig) + " WHERE id = ?", param);
        }
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    protected abstract String tablaHija();

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param objeto valor de {@code objeto}
     * @return el resultado
     */
    protected abstract Map<String, Object> valoresHija(T objeto);
}
