package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.pagos.ui.FilaPago;
import ar.edu.ub.club.socios.aplicacion.FichaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de vista de la ficha del socio (P-08): datos personales y de acceso, categoría, estado, grupo con sus
 * integrantes, cuotas del grupo, reservas futuras, pagos y saldo a favor del grupo.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(ficha)}; la vista solo lo
 * muestra (MVP Vista Pasiva: las entidades no llegan a la vista).
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
 *
 * @param id socio
 * @param dni documento
 * @param nombre nombre
 * @param apellido apellido
 * @param fechaNacimiento nacimiento
 * @param telefono teléfono
 * @param direccion domicilio
 * @param email correo
 * @param username nombre de usuario
 * @param categoria nombre de la categoría
 * @param estado estado de la cuenta (ACTIVA o INACTIVA)
 * @param fechaAlta alta
 * @param idGrupo grupo vigente
 * @param integrantes integrantes del grupo
 * @param cuotas cuotas del grupo (de la más reciente a la más antigua)
 * @param reservasFuturas reservas futuras confirmadas del socio
 * @param pagos pagos del grupo (vacía: {@code FichaSocio} no los trae; se consultan en P-28)
 * @param saldo saldo a favor del grupo
 */
public record DetalleSocio(Long id, String dni, String nombre, String apellido, LocalDate fechaNacimiento, String telefono, String direccion, String email, String username, String categoria, String estado, LocalDate fechaAlta, Long idGrupo, List<FilaIntegrante> integrantes, List<FilaCuota> cuotas, List<FilaResumenReserva> reservasFuturas, List<FilaPago> pagos, Dinero saldo) {

    /**
     * Construye el modelo desde la ficha que devuelve {@code ServicioSocios.consultarSocio}. Las cuotas se
     * muestran sin acción de pago online (la ficha es del administrador).
     *
     * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07
     * @param ficha ficha del socio
     * @return modelo de vista
     */
    public static DetalleSocio desde(FichaSocio ficha) {
        Socio socio = ficha.getSocio();
        GrupoFamiliar grupo = ficha.getGrupo();
        List<FilaIntegrante> integrantes = grupo.getIntegrantes().stream()
                .map(s -> FilaIntegrante.desde(s, grupo.getTitular())).toList();
        List<FilaCuota> cuotas = ficha.getEstadoCuenta().stream().map(c -> FilaCuota.desde(c, false)).toList();
        List<FilaResumenReserva> reservas = ficha.getReservas().stream().map(FilaResumenReserva::desde).toList();
        return new DetalleSocio(socio.getId(), socio.getDni(), socio.getNombre(), socio.getApellido(),
                socio.getFechaNacimiento(), socio.getTelefono(), socio.getDireccion(), socio.getEmail(),
                socio.getUsername(), socio.getCategoria().getNombre(), socio.getEstado().name(), socio.getFechaAlta(),
                grupo.getId(), integrantes, cuotas, reservas, List.of(), grupo.saldoAFavor());
    }
}
