package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.socios.aplicacion.PerfilSocio;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05
 */
public record ResumenPerfil(Long idSocio, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, String email, String categoria, Long idGrupo, boolean esTitular, List<FilaIntegrante> integrantes, FilaCuota cuotaVigente, List<FilaResumenReserva> proximasReservas) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param perfil valor de {@code perfil}
     * @return el resultado
     */
    public static ResumenPerfil desde(PerfilSocio perfil) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
