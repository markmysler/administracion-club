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

    /**
     * Devuelve {@code desde}: desde.
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
     * Devuelve {@code hasta}: hasta.
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
     * Devuelve {@code idDeporte}: idDeporte.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Asigna {@code idDeporte} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param idDeporte nuevo valor
     */
    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve {@code idCancha}: idCancha.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public Long getIdCancha() {
        return idCancha;
    }

    /**
     * Asigna {@code idCancha} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param idCancha nuevo valor
     */
    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }

    /**
     * Devuelve {@code idSocio}: idSocio.
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
     * <p>Casos de uso: CU-29, CU-31
     * @param idSocio nuevo valor
     */
    public void setIdSocio(Long idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * Devuelve {@code estado}: estado.
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
     * Devuelve {@code textoSocio}: textoSocio.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return el valor del atributo
     */
    public String getTextoSocio() {
        return textoSocio;
    }

    /**
     * Asigna {@code textoSocio} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param textoSocio nuevo valor
     */
    public void setTextoSocio(String textoSocio) {
        this.textoSocio = textoSocio;
    }
}
