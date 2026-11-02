package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * Datos de solo lectura de la cuenta que se modifica en P-14 (CU-15): el tipo de cuenta se muestra pero no se
 * puede cambiar (CU-16 FA1, RN-04). El tipo y el nombre salen de las operaciones polimórficas de {@code Usuario}.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-15, CU-16
 *
 * @param id identificador de la cuenta
 * @param username nombre de usuario
 * @param nombreCompleto apellido y nombre
 * @param email correo
 * @param tipoCuenta "Socio" o "Administrador"
 * @param estado "Activa" o "Inactiva"
 */
public record DetalleCuenta(Long id, String username, String nombreCompleto, String email, String tipoCuenta, String estado) {

    /**
     * Arma el detalle de una cuenta de cualquier tipo.
     *
     * <p>Casos de uso: CU-15, CU-16
     * @param usuario cuenta
     * @return modelo de vista
     */
    public static DetalleCuenta desde(Usuario usuario) {
        return new DetalleCuenta(usuario.getId(), usuario.getUsername(), usuario.nombreCompleto(), usuario.getEmail(),
                usuario.tipoCuenta(), FilaCuenta.textoEstado(usuario.getEstado()));
    }
}
