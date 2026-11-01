package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import org.springframework.stereotype.Component;

/**
 * Crea el {@code PagoConSaldo} APROBADO que registra la cancelación de una cuota con el saldo a favor (RN-21).
 * Lo usa el observador {@code RegistradorPagoConSaldo}.
 *
 * <p>Creador concreto del patrón Factory Method (GoF).
 *
 * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
 */
@Component
public class ProcesadorPagoConSaldo extends ProcesadorPago {

    /**
     * Crea el creador.
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param fachadaPersistencia fachada de persistencia
     */
    public ProcesadorPagoConSaldo(FachadaPersistencia fachadaPersistencia) {
        super(fachadaPersistencia);
    }

    /**
     * Método fábrica: {@code new PagoConSaldo(c, datos.importe)} (sec_tmp_emision_p2).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @param cuota Cuota cubierta.
     * @param datos Importe cubierto con saldo.
     * @return el pago con saldo APROBADO
     */
    @Override
    protected Pago crearPago(Cuota cuota, DatosPago datos) {
        return new PagoConSaldo(cuota, datos.getImporte());
    }
}
