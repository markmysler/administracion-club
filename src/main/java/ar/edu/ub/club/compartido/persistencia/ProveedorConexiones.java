package ar.edu.ub.club.compartido.persistencia;

import java.sql.Connection;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

/**
 * Único punto de acceso al pool HikariCP (Indirection / Protected Variations): entrega la conexión de la
 * transacción de Spring en curso y el mapa de identidad de esa transacción. Los brokers nunca abren ni confirman
 * transacciones: lo hace {@code @Transactional} en las fachadas.
 *
 * <p>Casos de uso: todos
 */
@Component
public class ProveedorConexiones {

    /** Origen de datos (HikariDataSource configurado por Spring Boot). */
    private final DataSource origenDatos;

    /**
     * Crea el proveedor sobre el origen de datos de la aplicación.
     *
     * <p>Casos de uso: todos
     * @param origenDatos pool de conexiones
     */
    public ProveedorConexiones(DataSource origenDatos) {
        this.origenDatos = origenDatos;
    }

    /**
     * Devuelve la conexión de la transacción de Spring en curso ({@code DataSourceUtils.getConnection}); fuera de
     * una transacción, una conexión del pool que debe liberarse con {@code liberar}.
     *
     * <p>Casos de uso: todos
     * @return conexión JDBC
     */
    public Connection conexionActual() {
        return DataSourceUtils.getConnection(origenDatos);
    }

    /**
     * Libera la conexión ({@code DataSourceUtils.releaseConnection}): no la cierra si pertenece a la transacción.
     *
     * <p>Casos de uso: todos
     * @param conexion conexión obtenida con {@code conexionActual}
     */
    public void liberar(Connection conexion) {
        DataSourceUtils.releaseConnection(conexion, origenDatos);
    }

    /**
     * Devuelve el {@code MapaIdentidad} ligado a la transacción en curso (TransactionSynchronizationManager); si no
     * existe lo crea y registra su descarte al terminar la transacción. La clave del recurso es el
     * {@code ConnectionHolder} de la transacción, así una transacción nueva ({@code REQUIRES_NEW}) tiene su propio
     * mapa. Fuera de una transacción devuelve un mapa nuevo en cada llamada.
     *
     * <p>Casos de uso: todos
     * @return mapa de identidad de la transacción
     */
    public MapaIdentidad mapaIdentidadActual() {
        if (!TransactionSynchronizationManager.isSynchronizationActive()) {
            return new MapaIdentidad();
        }
        Connection conexion = conexionActual();
        liberar(conexion);
        Object duenio = TransactionSynchronizationManager.getResource(origenDatos);
        if (duenio == null) {
            return new MapaIdentidad();
        }
        String clave = "mapaIdentidad@" + System.identityHashCode(duenio);
        MapaIdentidad mapa = (MapaIdentidad) TransactionSynchronizationManager.getResource(clave);
        if (mapa == null) {
            MapaIdentidad nuevo = new MapaIdentidad();
            TransactionSynchronizationManager.bindResource(clave, nuevo);
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                /**
                 * Descarta el mapa al terminar la transacción.
                 *
                 * <p>Casos de uso: todos
                 * @param estado estado final de la transacción
                 */
                @Override
                public void afterCompletion(int estado) {
                    nuevo.vaciar();
                    TransactionSynchronizationManager.unbindResourceIfPossible(clave);
                }
            });
            mapa = nuevo;
        }
        return mapa;
    }
}
