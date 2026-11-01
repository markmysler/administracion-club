package ar.edu.ub.club.accesos.dominio;

import java.util.Set;

/**
 * Conjunto de contraseñas comunes o filtradas, cargado desde un recurso de la aplicación al iniciar.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ListaContrasenasProhibidas {

    /** Contraseñas prohibidas normalizadas a minúsculas. */
    private Set<String> contrasenas;

    /**
     * Indica si la clave, normalizada, está en la lista.
     *
     * <p>Casos de uso: PENDIENTE
     * @param clave Contraseña propuesta.
     * @return true si está prohibida.
     */
    public boolean contiene(String clave) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
