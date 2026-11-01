package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-01, CU-02
 */
public class DatosSocio {

    /** Atributo dni. */
    private String dni;

    /** Atributo nombre. */
    private String nombre;

    /** Atributo apellido. */
    private String apellido;

    /** Atributo fechaNacimiento. */
    private LocalDate fechaNacimiento;

    /** Atributo telefono. */
    private String telefono;

    /** Atributo direccion. */
    private String direccion;

    /** Atributo email. */
    private String email;

    /** Atributo idCategoria. */
    private Long idCategoria;

    /** Atributo username. */
    private String username;

    /** Atributo claveInicial. */
    private String claveInicial;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param socio valor de {@code socio}
     * @return el resultado
     */
    public static DatosSocio desde(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
