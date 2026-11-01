package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-15
 */
public class DatosAcceso {

    /** Atributo username. */
    private String username;

    /** Atributo email. */
    private String email;

    /** Atributo nuevaClave. */
    private String nuevaClave;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-15
     * @param usuario valor de {@code usuario}
     * @return el resultado
     */
    public static DatosAcceso desde(Usuario usuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
