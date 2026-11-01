package ar.edu.ub.club.agenda.aplicacion;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class DatosReglaReserva {

    /** Atributo anticipacionMinimaHoras. */
    private Integer anticipacionMinimaHoras;

    /** Atributo anticipacionMaximaDias. */
    private Integer anticipacionMaximaDias;

    /** Atributo duracionTurnoMinutos. */
    private Integer duracionTurnoMinutos;

    /** Atributo limiteReservasVigentesPorSocio. */
    private Integer limiteReservasVigentesPorSocio;

    /** Atributo anticipacionMinimaCancelacionHoras. */
    private Integer anticipacionMinimaCancelacionHoras;

    /** Atributo anticipacionMinimaModificacionHoras. */
    private Integer anticipacionMinimaModificacionHoras;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param regla valor de {@code regla}
     * @return el resultado
     */
    public static DatosReglaReserva desde(ReglaReserva regla) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code anticipacionMinimaHoras}: anticipacionMinimaHoras.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaHoras() {
        return anticipacionMinimaHoras;
    }

    /**
     * Asigna {@code anticipacionMinimaHoras} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param anticipacionMinimaHoras nuevo valor
     */
    public void setAnticipacionMinimaHoras(Integer anticipacionMinimaHoras) {
        this.anticipacionMinimaHoras = anticipacionMinimaHoras;
    }

    /**
     * Devuelve {@code anticipacionMaximaDias}: anticipacionMaximaDias.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getAnticipacionMaximaDias() {
        return anticipacionMaximaDias;
    }

    /**
     * Asigna {@code anticipacionMaximaDias} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param anticipacionMaximaDias nuevo valor
     */
    public void setAnticipacionMaximaDias(Integer anticipacionMaximaDias) {
        this.anticipacionMaximaDias = anticipacionMaximaDias;
    }

    /**
     * Devuelve {@code duracionTurnoMinutos}: duracionTurnoMinutos.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getDuracionTurnoMinutos() {
        return duracionTurnoMinutos;
    }

    /**
     * Asigna {@code duracionTurnoMinutos} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param duracionTurnoMinutos nuevo valor
     */
    public void setDuracionTurnoMinutos(Integer duracionTurnoMinutos) {
        this.duracionTurnoMinutos = duracionTurnoMinutos;
    }

    /**
     * Devuelve {@code limiteReservasVigentesPorSocio}: limiteReservasVigentesPorSocio.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getLimiteReservasVigentesPorSocio() {
        return limiteReservasVigentesPorSocio;
    }

    /**
     * Asigna {@code limiteReservasVigentesPorSocio} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param limiteReservasVigentesPorSocio nuevo valor
     */
    public void setLimiteReservasVigentesPorSocio(Integer limiteReservasVigentesPorSocio) {
        this.limiteReservasVigentesPorSocio = limiteReservasVigentesPorSocio;
    }

    /**
     * Devuelve {@code anticipacionMinimaCancelacionHoras}: anticipacionMinimaCancelacionHoras.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaCancelacionHoras() {
        return anticipacionMinimaCancelacionHoras;
    }

    /**
     * Asigna {@code anticipacionMinimaCancelacionHoras} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param anticipacionMinimaCancelacionHoras nuevo valor
     */
    public void setAnticipacionMinimaCancelacionHoras(Integer anticipacionMinimaCancelacionHoras) {
        this.anticipacionMinimaCancelacionHoras = anticipacionMinimaCancelacionHoras;
    }

    /**
     * Devuelve {@code anticipacionMinimaModificacionHoras}: anticipacionMinimaModificacionHoras.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Integer getAnticipacionMinimaModificacionHoras() {
        return anticipacionMinimaModificacionHoras;
    }

    /**
     * Asigna {@code anticipacionMinimaModificacionHoras} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: PENDIENTE
     * @param anticipacionMinimaModificacionHoras nuevo valor
     */
    public void setAnticipacionMinimaModificacionHoras(Integer anticipacionMinimaModificacionHoras) {
        this.anticipacionMinimaModificacionHoras = anticipacionMinimaModificacionHoras;
    }
}
