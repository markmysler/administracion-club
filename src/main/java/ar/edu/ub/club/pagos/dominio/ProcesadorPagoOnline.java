package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import java.util.UUID;
import org.springframework.stereotype.Component;

/**
 * Crea el {@code PagoOnline} Pendiente por el importe a pagar de la cuota, con una referencia externa nueva
 * (UUID) y el titular que lo inicia. Lo usa {@code ServicioPagos.iniciarPagoOnline}.
 *
 * <p>Creador concreto del patrón Factory Method (GoF).
 *
 * <p>Casos de uso: CU-35
 */
@Component
public class ProcesadorPagoOnline extends ProcesadorPago {

    /**
     * Crea el creador.
     *
     * <p>Casos de uso: CU-35
     * @param fachadaPersistencia fachada de persistencia
     */
    public ProcesadorPagoOnline(FachadaPersistencia fachadaPersistencia) {
        super(fachadaPersistencia);
    }

    /**
     * Método fábrica: {@code new PagoOnline(c, c.importeAPagar(), UUID nuevo, datos.fecha)} con {@code
     * iniciadoPor = datos.iniciadoPor} (sec_cu35_p4).
     *
     * <p>Casos de uso: CU-35
     * @param cuota Cuota.
     * @param datos Fecha y titular.
     * @return el pago online Pendiente
     */
    @Override
    protected Pago crearPago(Cuota cuota, DatosPago datos) {
        PagoOnline pago = new PagoOnline(cuota, cuota.importeAPagar(), UUID.randomUUID(), datos.getFecha());
        pago.asignarIniciadoPor(datos.getIniciadoPor());
        return pago;
    }
}
