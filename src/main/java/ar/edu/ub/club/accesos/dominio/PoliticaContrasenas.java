package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import org.springframework.stereotype.Component;

/**
 * Regla de dominio que valida toda contraseña nueva (RNF-03), según NIST SP 800-63B-4 y OWASP: no exige
 * combinaciones de tipos de caracteres ni cambios periódicos. La usan {@code ServicioSocios.registrarSocio} y
 * {@code ServicioAccesos.crearAdministrador}, {@code restablecerClave} y {@code modificarDatosAcceso}. Bean de
 * Spring (Pure Fabrication de una regla reutilizable).
 *
 * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
 */
@Component
public class PoliticaContrasenas {

    /** Mínimo de caracteres. */
    private int longitudMinima;

    /** Máximo de caracteres (cada carácter Unicode cuenta como uno; se aceptan espacios). */
    private int longitudMaxima;

    /** Contraseñas comunes o filtradas. */
    private ListaContrasenasProhibidas prohibidas;

    /**
     * Crea la política con los límites fijos 15 y 64.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param prohibidas lista de contraseñas prohibidas
     */
    public PoliticaContrasenas(ListaContrasenasProhibidas prohibidas) {
        this.longitudMinima = 15;
        this.longitudMaxima = 64;
        this.prohibidas = prohibidas;
    }

    /**
     * Lanza {@code ErrorNegocio} con el motivo si la clave tiene menos de 15 o más de 64 caracteres, figura en la
     * lista prohibida o es igual al nombre de usuario (sin distinguir mayúsculas). La longitud se cuenta en puntos
     * de código Unicode. El error no lleva campo: la fachada lo vuelve a lanzar con el campo de su formulario.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param clave Contraseña propuesta.
     * @param username Nombre de usuario de la cuenta.
     * @throws ErrorNegocio si la clave no cumple la política
     */
    public void validar(String clave, String username) {
        if (clave == null || clave.isEmpty()) {
            throw new ErrorNegocio("La contraseña es obligatoria.");
        }
        int longitud = clave.codePointCount(0, clave.length());
        if (longitud < longitudMinima) {
            throw new ErrorNegocio("La contraseña debe tener al menos " + longitudMinima + " caracteres.");
        }
        if (longitud > longitudMaxima) {
            throw new ErrorNegocio("La contraseña no puede tener más de " + longitudMaxima + " caracteres.");
        }
        if (username != null && clave.equalsIgnoreCase(username)) {
            throw new ErrorNegocio("La contraseña no puede ser igual al nombre de usuario.");
        }
        if (prohibidas.contiene(clave)) {
            throw new ErrorNegocio("La contraseña es demasiado común o figura en filtraciones conocidas; elija otra.");
        }
    }
}
