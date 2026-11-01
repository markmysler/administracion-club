package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import java.time.LocalDateTime;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
public record DetalleComprobante(Long id, String numero, LocalDateTime fechaEmision, Dinero importe, String tipo, String periodoCuota, Long idGrupo, String origen, String medio, String nombreClub, String direccionClub, String telefonoClub, String emailClub) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param comprobante valor de {@code comprobante}
     * @param club valor de {@code club}
     * @return el resultado
     */
    public static DetalleComprobante desde(Comprobante comprobante, ConfiguracionClub club) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
