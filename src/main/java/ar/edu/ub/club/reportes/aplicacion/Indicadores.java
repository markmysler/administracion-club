package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class Indicadores {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo reservasHoy. */
    private Long reservasHoy;

    /** Atributo reservasSemana. */
    private Long reservasSemana;

    /** Atributo recaudacionMes. */
    private Dinero recaudacionMes;

    /** Atributo cuotasPagadasMes. */
    private Long cuotasPagadasMes;

    /** Atributo sociosActivos. */
    private Long sociosActivos;

    /** Atributo cuotasPendientes. */
    private Long cuotasPendientes;

    /** Atributo cuotasVencidas. */
    private Long cuotasVencidas;

    /** Atributo canchasMasUsadas. */
    private List<FilaUsoCancha> canchasMasUsadas;

    /** Atributo noDisponibles. */
    private List<String> noDisponibles;

    /**
     * Devuelve {@code fecha}: fecha.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code reservasHoy}: reservasHoy.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getReservasHoy() {
        return reservasHoy;
    }

    /**
     * Devuelve {@code reservasSemana}: reservasSemana.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getReservasSemana() {
        return reservasSemana;
    }

    /**
     * Devuelve {@code recaudacionMes}: recaudacionMes.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getRecaudacionMes() {
        return recaudacionMes;
    }

    /**
     * Devuelve {@code cuotasPagadasMes}: cuotasPagadasMes.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getCuotasPagadasMes() {
        return cuotasPagadasMes;
    }

    /**
     * Devuelve {@code sociosActivos}: sociosActivos.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getSociosActivos() {
        return sociosActivos;
    }

    /**
     * Devuelve {@code cuotasPendientes}: cuotasPendientes.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getCuotasPendientes() {
        return cuotasPendientes;
    }

    /**
     * Devuelve {@code cuotasVencidas}: cuotasVencidas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getCuotasVencidas() {
        return cuotasVencidas;
    }

    /**
     * Devuelve {@code canchasMasUsadas}: canchasMasUsadas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public List<FilaUsoCancha> getCanchasMasUsadas() {
        return canchasMasUsadas;
    }

    /**
     * Devuelve {@code noDisponibles}: noDisponibles.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public List<String> getNoDisponibles() {
        return noDisponibles;
    }
}
