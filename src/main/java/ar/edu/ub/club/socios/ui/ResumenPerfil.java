package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.socios.aplicacion.PerfilSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de vista del inicio (P-05) y de «Mi perfil» (P-24) del socio: datos personales, categoría, grupo con sus
 * integrantes e indicación de titular, cuota vigente y próximas reservas. Sin opciones de edición (CU-05).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(perfil)}.
 *
 * <p>Casos de uso: CU-05
 *
 * @param idSocio socio de la sesión
 * @param dni documento
 * @param nombre nombre
 * @param apellido apellido
 * @param fechaNacimiento nacimiento
 * @param telefono teléfono
 * @param direccion domicilio
 * @param email correo
 * @param categoria nombre de la categoría
 * @param idGrupo grupo vigente
 * @param esTitular si el socio es el titular del grupo
 * @param integrantes integrantes del grupo
 * @param cuotaVigente última cuota emitida o {@code null} («Sin cuotas emitidas», CU-05 FA1)
 * @param proximasReservas reservas futuras confirmadas
 */
public record ResumenPerfil(Long idSocio, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, String email, String categoria, Long idGrupo, boolean esTitular, List<FilaIntegrante> integrantes, FilaCuota cuotaVigente, List<FilaResumenReserva> proximasReservas) {

    /**
     * Construye el modelo desde el perfil que devuelve {@code ServicioSocios.consultarPerfilPropio}. La cuota se
     * muestra sin la acción de pago online: el pago se inicia desde el estado de cuenta (P-25).
     *
     * <p>Casos de uso: CU-05
     * @param perfil perfil del socio
     * @return modelo de vista
     */
    public static ResumenPerfil desde(PerfilSocio perfil) {
        Socio socio = perfil.getSocio();
        GrupoFamiliar grupo = perfil.getGrupo();
        List<FilaIntegrante> integrantes = grupo.getIntegrantes().stream()
                .map(s -> FilaIntegrante.desde(s, grupo.getTitular())).toList();
        FilaCuota cuota = perfil.getCuotaVigente() == null ? null : FilaCuota.desde(perfil.getCuotaVigente(), false);
        List<FilaResumenReserva> reservas = perfil.getProximasReservas().stream().map(FilaResumenReserva::desde)
                .toList();
        return new ResumenPerfil(socio.getId(), socio.getDni(), socio.getNombre(), socio.getApellido(),
                socio.getFechaNacimiento(), socio.getTelefono(), socio.getDireccion(), socio.getEmail(),
                socio.getCategoria().getNombre(), grupo.getId(), grupo.esTitular(socio), integrantes, cuota, reservas);
    }
}
