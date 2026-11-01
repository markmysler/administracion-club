package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.Pago;
import java.time.LocalDateTime;

/**
 * Modelo de vista (record inmutable) de la pantalla imprimible P-29: datos del comprobante o de la constancia de
 * acreditación y datos institucionales del club (MODELO 11.6.1).
 *
 * <p>Casos de uso: CU-05, CU-36, CU-37
 * @param id id del comprobante
 * @param numero número correlativo (C-00000001)
 * @param fechaEmision fecha y hora de emisión
 * @param importe importe
 * @param tipo {@code COMPROBANTE_PAGO} o {@code CONSTANCIA_ACREDITACION} (pago ACREDITADO, RN-21)
 * @param periodoCuota período de la cuota
 * @param idGrupo grupo familiar
 * @param origen origen del pago
 * @param medio medio del pago
 * @param nombreClub nombre del club
 * @param direccionClub dirección del club
 * @param telefonoClub teléfono del club
 * @param emailClub correo del club
 */
public record DetalleComprobante(Long id, String numero, LocalDateTime fechaEmision, Dinero importe, String tipo,
                                 String periodoCuota, Long idGrupo, String origen, String medio, String nombreClub,
                                 String direccionClub, String telefonoClub, String emailClub) {

    /** Tipo de documento de un pago aprobado. */
    public static final String COMPROBANTE_PAGO = "COMPROBANTE_PAGO";

    /** Tipo de documento de un pago acreditado como saldo a favor. */
    public static final String CONSTANCIA_ACREDITACION = "CONSTANCIA_ACREDITACION";

    /**
     * Fábrica: combina el comprobante (y su pago y cuota) con la configuración del club.
     *
     * <p>Casos de uso: CU-05, CU-36, CU-37
     * @param comprobante comprobante emitido
     * @param club configuración institucional del club
     * @return el detalle
     */
    public static DetalleComprobante desde(Comprobante comprobante, ConfiguracionClub club) {
        Pago pago = comprobante.getPago();
        boolean acreditado = pago != null && pago.getEstado() != null
                && "ACREDITADO".equals(pago.getEstado().codigo());
        return new DetalleComprobante(comprobante.getId(), comprobante.getNumero(), comprobante.getFechaEmision(),
                comprobante.getImporte(), acreditado ? CONSTANCIA_ACREDITACION : COMPROBANTE_PAGO,
                pago == null || pago.getCuota() == null ? null : pago.getCuota().getPeriodo(),
                pago == null || pago.getCuota() == null ? null : pago.getCuota().getIdGrupo(),
                pago == null ? null : pago.origen(), pago == null ? null : pago.descripcionMedio(),
                club == null ? null : club.getNombre(), club == null ? null : club.getDireccion(),
                club == null ? null : club.getTelefono(), club == null ? null : club.getEmail());
    }
}
