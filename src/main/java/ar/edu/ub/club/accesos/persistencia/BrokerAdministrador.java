package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker concreto de {@code Administrador} (tablas {@code usuario} + {@code administrador}). Database Broker
 * (Larman, cap. 34): completa los ganchos del Template Method de {@code BrokerUsuario} / {@code BrokerBaseDatos}.
 * Queda registrado en {@code FachadaPersistencia} bajo la jerarquía {@code Usuario}.
 *
 * <p>Casos de uso: CU-11, CU-12, CU-13, CU-14, CU-15, CU-16, CU-17
 */
@Component
public class BrokerAdministrador extends BrokerUsuario<Administrador> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerAdministrador(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Tabla hija: {@code administrador}.
     *
     * <p>Casos de uso: todos
     * @return {@code "administrador"}
     */
    @Override
    protected String tablaHija() {
        return "administrador";
    }

    /**
     * Columnas de la tabla {@code administrador}: nombre y apellido.
     *
     * <p>Casos de uso: CU-14
     * @param objeto administrador
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresHija(Administrador objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("nombre", objeto.getNombre());
        valores.put("apellido", objeto.getApellido());
        return valores;
    }

    /**
     * Clase que maneja: {@code Administrador}.
     *
     * <p>Casos de uso: todos
     * @return {@code Administrador.class}
     */
    @Override
    public Class<Administrador> claseObjeto() {
        return Administrador.class;
    }

    /**
     * Tabla padre (la de {@code BrokerUsuario}); se redefine solo para dejar explícito el gancho.
     *
     * <p>Casos de uso: todos
     * @return {@code "usuario"}
     */
    @Override
    protected String tabla() {
        return super.tabla();
    }

    /**
     * Desmaterializa con {@code Administrador.reconstituir(...)} las columnas de {@code usuario} y
     * {@code administrador}.
     *
     * <p>Casos de uso: todos
     * @param fila fila del JOIN
     * @return administrador
     */
    @Override
    protected Administrador crearObjeto(FilaResultado fila) {
        return Administrador.reconstituir(fila.largo("id"), fila.texto("username"), fila.texto("email"),
                fila.texto("password_hash"), EstadoCuenta.valueOf(fila.texto("estado")),
                fila.entero("intentos_fallidos"), fila.fechaHora("bloqueado_hasta"), fila.fechaHora("fecha_creacion"),
                fila.texto("nombre"), fila.texto("apellido"));
    }

    /**
     * Columnas del INSERT en {@code usuario} (las de {@code administrador} van en {@code guardarPartes}).
     *
     * <p>Casos de uso: CU-14
     * @param objeto administrador nuevo
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(Administrador objeto) {
        return super.valoresInsercion(objeto);
    }

    /**
     * Columnas del UPDATE de {@code usuario}.
     *
     * <p>Casos de uso: CU-11, CU-13, CU-15, CU-17
     * @param objeto administrador persistido
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(Administrador objeto) {
        return super.valoresActualizacion(objeto);
    }
}
