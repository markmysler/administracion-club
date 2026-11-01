package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.util.List;
import java.util.UUID;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
 */
public class PagoPorReferenciaExterna extends ConsultaEntidades<PagoOnline> {

    /** Atributo referencia. */
    private UUID referencia;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
     * @return el resultado
     */
    public Class<PagoOnline> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor), vencimiento de pagos online pendientes
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
