package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.pagos.ui.FilaPago;
import ar.edu.ub.club.socios.aplicacion.FichaSocio;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-04
 */
public record DetalleSocio(Long id, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, String email, String username, String categoria, String estado, LocalDate fechaAlta, Long idGrupo, List<FilaIntegrante> integrantes, List<FilaCuota> cuotas, List<FilaResumenReserva> reservasFuturas, List<FilaPago> pagos, Dinero saldo) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-04
     * @param ficha valor de {@code ficha}
     * @return el resultado
     */
    public static DetalleSocio desde(FichaSocio ficha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
