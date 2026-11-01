package ar.edu.ub.club.cuotas.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.ConsultaProyeccion;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) de proyección; lo resuelve {@code FachadaPersistencia} sin broker.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class SaldoDeGrupo extends ConsultaProyeccion<Dinero> {

    /** Atributo idGrupo. */
    private Long idGrupo;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    public Class<Dinero> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fila valor de {@code fila}
     * @return el resultado
     */
    @Override
    public Dinero leerFila(FilaResultado fila) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
