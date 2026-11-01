package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import org.springframework.stereotype.Component;

/**
 * Crea el {@code PagoManual} ya APROBADO con el medio, el administrador, el importe recibido y la fecha actual.
 * Lo usa {@code ServicioPagos.registrarPagoManual}.
 *
 * <p>Creador concreto del patrón Factory Method (GoF).
 *
 * <p>Casos de uso: CU-36
 */
@Component
public class ProcesadorPagoManual extends ProcesadorPago {

    /**
     * Crea el creador.
     *
     * <p>Casos de uso: CU-36
     * @param fachadaPersistencia fachada de persistencia
     */
    public ProcesadorPagoManual(FachadaPersistencia fachadaPersistencia) {
        super(fachadaPersistencia);
    }

    /**
     * Método fábrica: {@code new PagoManual(c, datos.importe, datos.medio, datos.registradoPor, datos.fecha)}.
     *
     * <p>Casos de uso: CU-36
     * @param cuota Cuota.
     * @param datos Importe, medio, administrador y fecha.
     * @return el pago manual APROBADO
     */
    @Override
    protected Pago crearPago(Cuota cuota, DatosPago datos) {
        return new PagoManual(cuota, datos.getImporte(), datos.getMedio(), datos.getRegistradoPor(), datos.getFecha());
    }
}
