package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * Saldo a favor de un grupo: suma de créditos menos suma de débitos de {@code movimiento_saldo} (RN-21; 0 si no
 * hay movimientos). La usa {@code ServicioCuotas} en la emisión y al aplicar un crédito. Consulta 46 de la
 * Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class SaldoDeGrupo extends ConsultaProyeccion<Dinero> {

    /** Grupo familiar. */
    private final Long idGrupo;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param idGrupo Grupo familiar.
     */
    public SaldoDeGrupo(Long idGrupo) {
        this.idGrupo = idGrupo;
    }

    /**
     * Clase del resultado: {@code Dinero}.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return Dinero.class
     */
    @Override
    public Class<Dinero> claseResultado() {
        return Dinero.class;
    }

    /**
     * Lee el saldo de la única fila.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fila Fila del resultado.
     * @return saldo a favor
     */
    @Override
    public Dinero leerFila(FilaResultado fila) {
        Dinero saldo = fila.dinero("saldo");
        return saldo == null ? Dinero.cero() : saldo;
    }

    /**
     * SELECT completo de la proyección.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return sentencia SQL
     */
    @Override
    public String sql() {
        return "SELECT COALESCE(SUM(CASE WHEN movimiento_saldo.tipo = 'CREDITO' THEN movimiento_saldo.importe"
                + " ELSE -movimiento_saldo.importe END), 0) AS saldo FROM movimiento_saldo"
                + " WHERE movimiento_saldo.grupo_familiar_id = ?";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return [idGrupo]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idGrupo);
    }
}
