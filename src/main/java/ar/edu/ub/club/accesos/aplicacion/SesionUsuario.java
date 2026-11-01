package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.util.Set;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-11
 */
public class SesionUsuario {

    /** Atributo idUsuario. */
    private Long idUsuario;

    /** Atributo nombreVisible. */
    private String nombreVisible;

    /** Atributo permisos. */
    private Set<Permiso> permisos;

    /** Atributo tokenPersistente. */
    private String tokenPersistente;

    /**
     * Crea una instancia de {@code SesionUsuario}.
     *
     * <p>Casos de uso: CU-11
     * @param idUsuario valor de {@code idUsuario}
     * @param nombreVisible valor de {@code nombreVisible}
     * @param permisos valor de {@code permisos}
     */
    public SesionUsuario(Long idUsuario, String nombreVisible, Set<Permiso> permisos) {
        // PENDIENTE
    }
}
