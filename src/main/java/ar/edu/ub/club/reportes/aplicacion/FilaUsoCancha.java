package ar.edu.ub.club.reportes.aplicacion;

import java.math.BigDecimal;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class FilaUsoCancha {

    /** Atributo deporte. */
    private String deporte;

    /** Atributo cancha. */
    private String cancha;

    /** Atributo cantidadReservas. */
    private Long cantidadReservas;

    /** Atributo horasReservadas. */
    private BigDecimal horasReservadas;

    /** Atributo porcentajeOcupacion. */
    private BigDecimal porcentajeOcupacion;

    /**
     * Devuelve {@code deporte}: deporte.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getDeporte() {
        return deporte;
    }

    /**
     * Devuelve {@code cancha}: cancha.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getCancha() {
        return cancha;
    }

    /**
     * Devuelve {@code cantidadReservas}: cantidadReservas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getCantidadReservas() {
        return cantidadReservas;
    }

    /**
     * Devuelve {@code horasReservadas}: horasReservadas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public BigDecimal getHorasReservadas() {
        return horasReservadas;
    }

    /**
     * Devuelve {@code porcentajeOcupacion}: porcentajeOcupacion.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public BigDecimal getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }
}
