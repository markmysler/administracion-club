package ar.edu.ub.club.reportes.aplicacion;

import java.time.LocalDate;

/**
 * Filtro de los reportes: período (desde y hasta, obligatorios) y, solo para el reporte de uso de canchas, deporte y
 * cancha opcionales. Lo arma la vista con {@code FiltroFechas} y los combos y lo valida el paso {@code validarFiltro}
 * del método plantilla {@code Reporte.generar}.
 *
 * <p>Objeto de comando de la capa de aplicación (mutable, con constructor vacío y propiedades, MODELO 11.6.2).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public class FiltroReporte {

    /** Fecha inicial del período (inclusive). */
    private LocalDate desde;

    /** Fecha final del período (inclusive). */
    private LocalDate hasta;

    /** Deporte por el que se filtra, o {@code null} para todos (solo CU-38). */
    private Long idDeporte;

    /** Cancha por la que se filtra, o {@code null} para todas (solo CU-38). */
    private Long idCancha;

    /**
     * Crea un filtro vacío (lo requiere el {@code Binder}; lo completa la vista).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    public FiltroReporte() {
    }

    /**
     * Crea un filtro de período sin deporte ni cancha.
     *
     * <p>Agregado en la implementación: constructor de conveniencia para las vistas y las pruebas.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param desde fecha inicial
     * @param hasta fecha final
     */
    public FiltroReporte(LocalDate desde, LocalDate hasta) {
        this.desde = desde;
        this.hasta = hasta;
    }

    /**
     * Devuelve la fecha inicial del período.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return fecha desde
     */
    public LocalDate getDesde() {
        return desde;
    }

    /**
     * Fija la fecha inicial del período.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param desde fecha desde
     */
    public void setDesde(LocalDate desde) {
        this.desde = desde;
    }

    /**
     * Devuelve la fecha final del período.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return fecha hasta
     */
    public LocalDate getHasta() {
        return hasta;
    }

    /**
     * Fija la fecha final del período.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param hasta fecha hasta
     */
    public void setHasta(LocalDate hasta) {
        this.hasta = hasta;
    }

    /**
     * Devuelve el deporte del filtro.
     *
     * <p>Casos de uso: CU-38
     * @return id del deporte o {@code null}
     */
    public Long getIdDeporte() {
        return idDeporte;
    }

    /**
     * Fija el deporte del filtro.
     *
     * <p>Casos de uso: CU-38
     * @param idDeporte id del deporte o {@code null}
     */
    public void setIdDeporte(Long idDeporte) {
        this.idDeporte = idDeporte;
    }

    /**
     * Devuelve la cancha del filtro.
     *
     * <p>Casos de uso: CU-38
     * @return id de la cancha o {@code null}
     */
    public Long getIdCancha() {
        return idCancha;
    }

    /**
     * Fija la cancha del filtro.
     *
     * <p>Casos de uso: CU-38
     * @param idCancha id de la cancha o {@code null}
     */
    public void setIdCancha(Long idCancha) {
        this.idCancha = idCancha;
    }
}
