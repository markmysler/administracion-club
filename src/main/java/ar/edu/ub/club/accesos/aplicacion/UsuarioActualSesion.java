package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.Autorizable;

/**
 * Implementación (accesos.aplicacion) de {@code UsuarioActual} y de {@code SesionActual} sobre el contexto de
 * seguridad de la sesión en memoria.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class UsuarioActualSesion implements UsuarioActual {

    /** Obtiene la cuenta de la sesión. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * Toma el identificador del principal de {@code SecurityContextHolder} y devuelve {@code
     * fachadaPersistencia.get(id, Usuario)} (de la subclase real). Sin sesión lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Usuario obtener() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code fachadaPersistencia.get(id, Administrador)}; si la cuenta no es de administrador lanza {@code
     * AccesoDenegadoException}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Administrador obtenerAdministrador() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si el contexto de seguridad tiene una autenticación válida.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public boolean autenticada() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code obtener()} como {@code Autorizable}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Autorizable cuenta() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
