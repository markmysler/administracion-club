package ar.edu.ub.club.configuracion.aplicacion;

import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-41
 */
public class DatosConfiguracionClub {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo direccion. */
    private String direccion;

    /** Atributo telefono. */
    private String telefono;

    /** Atributo email. */
    private String email;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-41
     * @param configuracion valor de {@code configuracion}
     * @return el resultado
     */
    public static DatosConfiguracionClub desde(ConfiguracionClub configuracion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
