package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.accesos.persistencia.UsuarioPorUsername;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.Optional;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación (socios.aplicacion) de {@code SocioActual} sobre el contexto de seguridad de la sesión del
 * servidor ({@code SecurityContextHolder}). Evita que la vista envíe el identificador del socio (RN-20): los
 * presentadores de P-05, P-24 y de reservas y pagos del socio obtienen la cuenta con {@code obtener()}.
 *
 * <p>El principal lo fija el módulo accesos al iniciar la sesión ({@code RegistroSesionVaadin}): un
 * {@code UserDetails} cuyo nombre es el id de la cuenta en texto, igual que lee {@code UsuarioActualSesion}. Se
 * interpreta primero como id (numérico o texto de dígitos); solo si no es numérico se busca como nombre de
 * usuario con {@code UsuarioPorUsername} (compatibilidad). Es transaccional de solo lectura porque la materialización del socio (con su
 * categoría y su grupo) necesita el mapa de identidad de una transacción.
 *
 * <p>Casos de uso: CU-05, CU-27, CU-29, CU-30, CU-31, CU-32, CU-35
 */
@Component
@Transactional(readOnly = true)
public class SocioActualSesion implements SocioActual {

    /** Obtiene el socio de la sesión. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea el componente (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param fachadaPersistencia fachada de persistencia
     */
    public SocioActualSesion(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Toma el identificador del principal de la sesión y devuelve {@code fachadaPersistencia.get(id, Socio)}; si la
     * cuenta no es de un socio lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: CU-05, CU-27, CU-29, CU-30, CU-31, CU-32, CU-35
     * @return {@code Socio} de la sesión
     * @throws AccesoDenegadoException si no hay sesión o la cuenta no es de un socio
     */
    @Override
    public Socio obtener() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            throw new AccesoDenegadoException("No hay una sesión iniciada.");
        }
        Object principal = autenticacion.getPrincipal();
        if (principal instanceof Number numero) {
            return socioPorId(numero.longValue());
        }
        String nombre = principal instanceof UserDetails detalle ? detalle.getUsername() : autenticacion.getName();
        if (nombre != null && nombre.matches("[0-9]+")) {
            return socioPorId(Long.valueOf(nombre));
        }
        Optional<Usuario> cuenta = fachadaPersistencia.buscarUno(new UsuarioPorUsername(nombre));
        if (cuenta.isPresent() && cuenta.get() instanceof Socio socio) {
            return socio;
        }
        throw new AccesoDenegadoException("La cuenta de la sesión no es de un socio.");
    }

    /**
     * {@code fachadaPersistencia.get(id, Socio)}, traduciendo la inexistencia (cuenta de administrador o borrada) a
     * {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: CU-05, CU-27, CU-29, CU-30, CU-31, CU-32, CU-35
     * @param id identificador de la cuenta
     * @return el socio
     */
    private Socio socioPorId(Long id) {
        try {
            return fachadaPersistencia.get(id, Socio.class);
        } catch (RecursoNoEncontradoException e) {
            throw new AccesoDenegadoException("La cuenta de la sesión no es de un socio.");
        }
    }
}
