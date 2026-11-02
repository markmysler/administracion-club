package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.Autorizable;
import ar.edu.ub.club.compartido.seguridad.SesionActual;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación (accesos.aplicacion) de {@code UsuarioActual} y de {@code SesionActual} sobre el contexto de
 * seguridad de la sesión en memoria. Es el bean que inyectan las fachadas, los presentadores, {@code VistaProtegida},
 * {@code VistaRaiz} y {@code MenuNavegacion} (Adapter entre Spring Security y el dominio).
 *
 * <p>Principal: el nombre de la autenticación ({@code Authentication.getName()}) es el id de la cuenta en texto,
 * igual en el ingreso por formulario ({@code RegistroSesionVaadin.iniciar}) y por remember-me
 * ({@code CuentasParaRecordarme}); así la cuenta se obtiene siempre con {@code fachadaPersistencia.get(id, Usuario)}
 * y un cambio de nombre de usuario (CU-15) no rompe la sesión.
 *
 * <p>Casos de uso: todos
 */
@Component
public class UsuarioActualSesion implements UsuarioActual, SesionActual {

    /** Obtiene la cuenta de la sesión. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea el componente.
     *
     * <p>Casos de uso: todos
     * @param fachadaPersistencia fachada de persistencia
     */
    public UsuarioActualSesion(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Toma el identificador del principal de {@code SecurityContextHolder} y devuelve {@code
     * fachadaPersistencia.get(id, Usuario)} (de la subclase real). Sin sesión lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: todos
     * @return la cuenta de la sesión
     * @throws AccesoDenegadoException si no hay sesión o la cuenta ya no existe
     */
    @Override
    @Transactional(readOnly = true)
    public Usuario obtener() {
        Long id = idDeSesion();
        try {
            return fachadaPersistencia.get(id, Usuario.class);
        } catch (RecursoNoEncontradoException e) {
            throw new AccesoDenegadoException("No hay una cuenta identificada.");
        }
    }

    /**
     * {@code fachadaPersistencia.get(id, Administrador)}; si la cuenta no es de administrador lanza {@code
     * AccesoDenegadoException}.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17, CU-28, CU-32, CU-33, CU-36, CU-41
     * @return la cuenta como administrador
     * @throws AccesoDenegadoException si no hay sesión o la cuenta no es de administrador
     */
    @Override
    @Transactional(readOnly = true)
    public Administrador obtenerAdministrador() {
        Long id = idDeSesion();
        try {
            return fachadaPersistencia.get(id, Administrador.class);
        } catch (RecursoNoEncontradoException e) {
            throw new AccesoDenegadoException("La cuenta de la sesión no es de administrador.");
        }
    }

    /**
     * Indica si el contexto de seguridad tiene una autenticación válida (no anónima y con un id de cuenta).
     *
     * <p>Casos de uso: todos
     * @return true si hay sesión autenticada
     */
    @Override
    public boolean autenticada() {
        return idOpcional() != null;
    }

    /**
     * Devuelve {@code obtener()} como {@code Autorizable}.
     *
     * <p>Casos de uso: todos
     * @return la cuenta de la sesión
     * @throws AccesoDenegadoException si no hay sesión
     */
    @Override
    public Autorizable cuenta() {
        return obtener();
    }

    /**
     * Id de la cuenta de la sesión.
     *
     * <p>Casos de uso: todos
     * @return id
     * @throws AccesoDenegadoException si no hay sesión
     */
    private Long idDeSesion() {
        Long id = idOpcional();
        if (id == null) {
            throw new AccesoDenegadoException("No hay una cuenta identificada.");
        }
        return id;
    }

    /**
     * Lee el id del nombre del principal, o {@code null} si no hay autenticación válida.
     *
     * <p>Casos de uso: todos
     * @return id o {@code null}
     */
    private Long idOpcional() {
        Authentication autenticacion = SecurityContextHolder.getContext().getAuthentication();
        if (autenticacion == null || !autenticacion.isAuthenticated()
                || autenticacion instanceof AnonymousAuthenticationToken) {
            return null;
        }
        try {
            return Long.valueOf(autenticacion.getName());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
