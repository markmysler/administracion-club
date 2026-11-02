package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * Interfaz (puerto) que resuelve la cuenta autenticada a partir del contexto de seguridad de la sesión del
 * servidor. La implementa {@code UsuarioActualSesion} (accesos.aplicacion), que también implementa {@code
 * SesionActual}: toma el identificador del principal de {@code SecurityContextHolder} y obtiene la cuenta con
 * {@code fachadaPersistencia.get(id, Usuario)}. Las fachadas y los presentadores dependen de la interfaz, lo que
 * permite probarlos sin servidor web (Protected Variations, inversión de dependencias).
 *
 * <p>Casos de uso: todos
 */
public interface UsuarioActual {

    /**
     * Devuelve la cuenta de la sesión (de la subclase real) o lanza {@code AccesoDenegadoException} si no hay
     * sesión.
     *
     * <p>Casos de uso: todos
     * @return {@code Usuario}.
     */
    Usuario obtener();

    /**
     * Devuelve la cuenta como {@code Administrador} con {@code fachadaPersistencia.get(id, Administrador)}; si la
     * cuenta no es de administrador lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: CU-28, CU-32, CU-33, CU-36
     * @return {@code Administrador}.
     */
    Administrador obtenerAdministrador();
}
