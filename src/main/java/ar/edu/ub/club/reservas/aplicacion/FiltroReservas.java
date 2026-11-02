package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.time.LocalDate;

/**
 * Objeto de datos de la capa de aplicación con los criterios de búsqueda de reservas de P-21 (CU-29) y P-22
 * (CU-31). Todos los criterios son opcionales; {@code ReservasPorFiltro} arma una condición por cada criterio
 * informado.
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public class FiltroReservas {

    /** Fecha mínima del turno ({@code null}: sin límite). */
    private LocalDate desde;

    /** Fecha máxima del turno ({@code null}: sin límite). */
    private LocalDate hasta;

    /** Deporte de la cancha. */
    private Long idDeporte;

    /** Cancha. */
    private Long idCancha;

    /** Socio titular de la reserva. */
    private Long idSocio;

    /** Estado de la reserva: CONFIRMADA, CANCELADA o FINALIZADA ({@code null}: todos). */
    private String estado;

    /** Texto de búsqueda del socio (DNI, apellido o nombre) del filtro de P-22. */
    private String textoSocio;

    /**
     * Constructor vacío (lo usa el {@code Binder} y las vistas).
     *
     * <p>Casos de uso: CU-29, CU-31
     */
    public FiltroReservas() {
        // todos los criterios opcionales
    }

    /**
     * Si {@code desde} y {@code hasta} están informados y {@code desde} es posterior a {@code hasta}, lanza
     * {@code ErrorNegocio} (CU-29 FA2, CU-31 FA1).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @throws ErrorNegocio si el rango de fechas es inválido
     */
    public void validar() {
        if (desde != null && hasta != null && desde.isAfter(hasta)) {
            throw new ErrorNegocio("La fecha desde no puede ser posterior a la fecha hasta.");
        }
    }

    /**
     * Devuelve un filtro con {@code idSocio} fijado al socio de la sesión (RN-20): el alcance lo determina la
     * sesión, nunca la pantalla. {@code ServicioReservas.consultarReservasPropias} le copia después las fechas y
     * el estado del filtro recibido.
     *
     * <p>Casos de uso: CU-29
     * @param idSocio socio de la sesión
     * @return filtro restringido al socio
     */
    public static FiltroReservas paraSocio(Long idSocio) {
        FiltroReservas filtro = new FiltroReservas();
        filtro.idSocio = idSocio;
        return filtro;
    }

    /**
     * Devuelve {@code desde}: fecha mínima del turno.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public LocalDate getDesde() {
        return desde;
    }

    /**
     * Asigna {@code desde} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param desde nuevo valor
     */
    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    /**
     * Devuelve {@code hasta}: fecha máxima del turno.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Asigna {@code hasta} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param hasta nuevo valor
     */
    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    /**
     * Devuelve {@code idDeporte}: deporte de la cancha.
     *
     * <p>Casos de uso: CU-31
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Asigna {@code idDeporte} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-31
     * @param idDeporte nuevo valor
     */
    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve {@code idCancha}: cancha.
     *
     * <p>Casos de uso: CU-31
     * @return el valor del atributo
     */
    public Long getIdCancha() {
        return idCancha;
    }

    /**
     * Asigna {@code idCancha} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-31
     * @param idCancha nuevo valor
     */
    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }

    /**
     * Devuelve {@code idSocio}: socio titular.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public Long getIdSocio() {
        return idSocio;
    }

    /**
     * Asigna {@code idSocio} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-31
     * @param idSocio nuevo valor
     */
    public void setIdSocio(Long idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * Devuelve {@code estado}: estado de la reserva.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Asigna {@code estado} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param estado nuevo valor
     */
    public void setEstado(String estado) {
        this.estado = estado;
    }

    /**
     * Devuelve {@code textoSocio}: texto de búsqueda del socio.
     *
     * <p>Casos de uso: CU-31
     * @return el valor del atributo
     */
    public String getTextoSocio() {
        return textoSocio;
    }

    /**
     * Asigna {@code textoSocio} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-31
     * @param textoSocio nuevo valor
     */
    public void setTextoSocio(String textoSocio) {
        this.textoSocio = textoSocio;
    }
}
