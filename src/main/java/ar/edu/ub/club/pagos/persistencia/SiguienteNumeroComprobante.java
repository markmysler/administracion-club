package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * Siguiente número correlativo de comprobante: {@code nextval('comprobante_numero_seq')}. La usa {@code
 * GeneradorComprobante}, que le da el formato C-00000001. Consulta 60 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-35, CU-36; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class SiguienteNumeroComprobante extends ConsultaProyeccion<Long> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-35, CU-36
     */
    public SiguienteNumeroComprobante() {
        // sin parámetros
    }

    /**
     * Clase del resultado: {@code Long}.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return Long.class
     */
    @Override
    public Class<Long> claseResultado() {
        return Long.class;
    }

    /**
     * Lee el número de la única fila.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @param fila fila
     * @return número
     */
    @Override
    public Long leerFila(FilaResultado fila) {
        return fila.largo("numero");
    }

    /**
     * SELECT de la secuencia.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return sentencia SQL
     */
    @Override
    public String sql() {
        return "SELECT nextval('comprobante_numero_seq') AS numero";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-35, CU-36
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}
