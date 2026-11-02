package ar.edu.ub.club.reportes.aplicacion;

import java.math.BigDecimal;

/**
 * Fila del reporte de uso de canchas: deporte, cancha, cantidad de reservas CONFIRMADAS y FINALIZADAS del período,
 * horas reservadas y porcentaje de ocupación sobre las horas de atención del club. La leen los objetos consulta de
 * proyección {@code UsoDeCanchas} (CU-38) y {@code CanchasMasUsadas} (indicador de P-04, CU-40).
 *
 * <p>Objeto de datos de salida de la capa de aplicación (inmutable).
 *
 * <p>Casos de uso: CU-38, CU-40
 */
public class FilaUsoCancha {

    /** Nombre del deporte de la cancha. */
    private String deporte;

    /** Nombre de la cancha. */
    private String cancha;

    /** Cantidad de reservas confirmadas o finalizadas en el período. */
    private Long cantidadReservas;

    /** Horas reservadas en el período. */
    private BigDecimal horasReservadas;

    /** Porcentaje de ocupación: horas reservadas / horas de atención del club en el período × 100. */
    private BigDecimal porcentajeOcupacion;

    /**
     * Crea la fila con todos sus datos.
     *
     * <p>Agregado en la implementación: constructor con los atributos del diseño.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @param deporte nombre del deporte
     * @param cancha nombre de la cancha
     * @param cantidadReservas cantidad de reservas
     * @param horasReservadas horas reservadas
     * @param porcentajeOcupacion porcentaje de ocupación (0 a 100)
     */
    public FilaUsoCancha(String deporte, String cancha, Long cantidadReservas, BigDecimal horasReservadas,
                         BigDecimal porcentajeOcupacion) {
        this.deporte = deporte;
        this.cancha = cancha;
        this.cantidadReservas = cantidadReservas;
        this.horasReservadas = horasReservadas;
        this.porcentajeOcupacion = porcentajeOcupacion;
    }

    /**
     * Devuelve el nombre del deporte.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @return deporte
     */
    public String getDeporte() {
        return deporte;
    }

    /**
     * Devuelve el nombre de la cancha.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @return cancha
     */
    public String getCancha() {
        return cancha;
    }

    /**
     * Devuelve la cantidad de reservas del período.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @return cantidad de reservas
     */
    public Long getCantidadReservas() {
        return cantidadReservas;
    }

    /**
     * Devuelve las horas reservadas del período.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @return horas reservadas
     */
    public BigDecimal getHorasReservadas() {
        return horasReservadas;
    }

    /**
     * Devuelve el porcentaje de ocupación.
     *
     * <p>Casos de uso: CU-38, CU-40
     * @return porcentaje de 0 a 100
     */
    public BigDecimal getPorcentajeOcupacion() {
        return porcentajeOcupacion;
    }
}
