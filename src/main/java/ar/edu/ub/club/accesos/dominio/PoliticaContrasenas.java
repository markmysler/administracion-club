package ar.edu.ub.club.accesos.dominio;

/**
 * Regla de dominio que valida toda contraseña nueva (RNF-03), según NIST SP 800-63B-4 y OWASP: no exige
 * combinaciones de tipos de caracteres ni cambios periódicos. La usan {@code ServicioSocios.registrarSocio} y
 * {@code ServicioAccesos.crearAdministrador}, {@code restablecerClave} y {@code modificarDatosAcceso}.
 *
 * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
 */
public class PoliticaContrasenas {

    /** Mínimo de caracteres. */
    private int longitudMinima;

    /** Máximo de caracteres (cada carácter Unicode cuenta como uno; se aceptan espacios). */
    private int longitudMaxima;

    /** Contraseñas comunes o filtradas. */
    private ListaContrasenasProhibidas prohibidas;

    /**
     * Lanza {@code ErrorNegocio} con el motivo si la clave tiene menos de 15 o más de 64 caracteres, figura en la
     * lista prohibida o es igual al nombre de usuario (sin distinguir mayúsculas).
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param clave Contraseña propuesta.
     * @param username Nombre de usuario de la cuenta.
     */
    public void validar(String clave, String username) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
