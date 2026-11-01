package ar.edu.ub.club.reservas.aplicacion;

import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public class FiltroReservas {

    /** Atributo desde. */
    private LocalDate desde;

    /** Atributo hasta. */
    private LocalDate hasta;

    /** Atributo idDeporte. */
    private Long idDeporte;

    /** Atributo idCancha. */
    private Long idCancha;

    /** Atributo idSocio. */
    private Long idSocio;

    /** Atributo estado. */
    private String estado;

    /** Atributo textoSocio. */
    private String textoSocio;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29, CU-31
     */
    public void validar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-29
     * @param idSocio valor de {@code idSocio}
     * @return el resultado
     */
    public static FiltroReservas paraSocio(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
