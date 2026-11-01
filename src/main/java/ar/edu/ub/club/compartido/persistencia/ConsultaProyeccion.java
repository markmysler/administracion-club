package ar.edu.ub.club.compartido.persistencia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Objeto consulta de proyección: devuelve filas de lectura o valores ({@code Turno}, {@code ResumenReserva},
 * {@code Dinero}, {@code Long}, filas de reportes). Su {@code sql()} es la sentencia SELECT completa. La resuelve
 * {@code FachadaPersistencia} con {@code ProveedorConexiones}, sin broker ni mapa de identidad.
 *
 * @param <T> clase de resultado
 *
 * <p>Casos de uso: todos
 */
public abstract class ConsultaProyeccion<T> extends Consulta<T> {

    /** Bitácora del SQL ejecutado (nivel DEBUG). */
    private static final Logger BITACORA = LoggerFactory.getLogger(ConsultaProyeccion.class);

    /**
     * Convierte la fila actual en un objeto de resultado.
     *
     * <p>Casos de uso: todos
     * @param fila fila del resultado
     * @return objeto de resultado
     */
    public abstract T leerFila(FilaResultado fila);

    /**
     * Ejecuta {@code sql()} con la conexión de la transacción y llama a {@code leerFila} por cada fila.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return filas leídas
     */
    @Override
    List<T> resolver(FachadaPersistencia fachada) {
        ProveedorConexiones proveedor = fachada.proveedor();
        Connection conexion = proveedor.conexionActual();
        BITACORA.debug("SQL {} : {} {}", getClass().getSimpleName(), sql(), parametros());
        try (PreparedStatement sentencia = conexion.prepareStatement(sql())) {
            BrokerBaseDatos.vincularParametros(sentencia, parametros());
            List<T> resultado = new ArrayList<>();
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    resultado.add(leerFila(new FilaResultado(rs, "")));
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw new IllegalStateException("Error en la consulta " + getClass().getSimpleName(), e);
        } finally {
            proveedor.liberar(conexion);
        }
    }

    /**
     * Cuenta las filas con {@code SELECT COUNT(*) FROM (sql()) q}.
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     * @return cantidad de filas
     */
    @Override
    long contarCon(FachadaPersistencia fachada) {
        ProveedorConexiones proveedor = fachada.proveedor();
        Connection conexion = proveedor.conexionActual();
        try (PreparedStatement sentencia = conexion.prepareStatement("SELECT COUNT(*) FROM (" + sql() + ") q")) {
            BrokerBaseDatos.vincularParametros(sentencia, parametros());
            try (ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("Error al contar " + getClass().getSimpleName(), e);
        } finally {
            proveedor.liberar(conexion);
        }
    }
}
