package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase abstracta del patrón <b>Template Method</b> (reportes.aplicacion) que usa {@code ServicioReportes}: el método
 * plantilla {@code generar} fija el orden de los pasos (validar filtro → obtener datos → calcular totales → armar
 * resultado) y cada subclase concreta ({@code ReporteUsoCanchas}, {@code ReporteCuotasDeudores}) completa la
 * obtención de datos y los totales. Es un bean sin estado compartido guardado en {@code ServicioReportes.reportes},
 * por eso los pasos reciben sus datos por parámetro (diagramas sec_cu38_p3 y sec_cu39_p2, pat_reportes).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public abstract class Reporte {

    /** Período máximo admitido en meses (lo verifica la fachada, 07A P-32). */
    private static final int MESES_MAXIMOS = 12;

    /**
     * Crea el reporte (constructor protegido de la clase abstracta).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    protected Reporte() {
    }

    /**
     * Método plantilla (final): {@code validarFiltro(filtro)}; {@code filas = obtenerDatos(filtro)}; {@code totales
     * = calcularTotales(filas)}; {@code return armarResultado(filas, totales)}. Las subclases no pueden cambiar el
     * orden, solo redefinir los pasos.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filtro período y, en CU-38, deporte y cancha
     * @return el resultado del reporte
     * @throws ErrorNegocio si el filtro es inválido (FA1)
     */
    @SuppressWarnings("rawtypes")
    public final ResultadoReporte generar(FiltroReporte filtro) {
        validarFiltro(filtro);
        List filas = obtenerDatos(filtro);
        Totales totales = calcularTotales(filas);
        return armarResultado(filas, totales, filtro);
    }

    /**
     * Paso 4 de CU-38 y CU-39: lanza {@code ErrorNegocio} si el período está incompleto, si {@code desde > hasta} o
     * si supera los 12 meses (FA1 «Filtro inválido»). Paso con implementación por omisión que las subclases pueden
     * redefinir.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filtro filtro a validar
     * @throws ErrorNegocio si el filtro es inválido
     */
    protected void validarFiltro(FiltroReporte filtro) {
        if (filtro == null || filtro.getDesde() == null || filtro.getHasta() == null) {
            throw new ErrorNegocio("Complete el período");
        }
        if (filtro.getDesde().isAfter(filtro.getHasta())) {
            throw new ErrorNegocio("La fecha desde no puede ser posterior a la fecha hasta");
        }
        if (filtro.getDesde().plusMonths(MESES_MAXIMOS).isBefore(filtro.getHasta())) {
            throw new ErrorNegocio("El período no puede superar los 12 meses");
        }
    }

    /**
     * Paso 5 (abstracto): filas del reporte, obtenidas de {@code ConsultasReportes}.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filtro filtro ya validado
     * @return filas del reporte (vacía si no hay datos, FA2)
     */
    @SuppressWarnings("rawtypes")
    protected abstract List obtenerDatos(FiltroReporte filtro);

    /**
     * Paso 6 (abstracto): totales del reporte.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filas filas obtenidas en el paso anterior
     * @return totales (en cero si no hay filas, FA2)
     */
    @SuppressWarnings("rawtypes")
    protected abstract Totales calcularTotales(List filas);

    /**
     * Paso 7: arma el {@code ResultadoReporte} con título, columnas, filas y totales; las subclases pueden
     * redefinirlo ({@code ReporteCuotasDeudores} lo hace para quedarse con los grupos deudores). Usa los ganchos
     * {@code titulo()}, {@code columnas()} y {@code celdas(fila)}.
     *
     * <p>El filtro no figura en la firma del diseño; se toma de {@link #armarResultado(List, Totales, FiltroReporte)},
     * que lo llama con el filtro actual. Esta versión de dos parámetros arma el resultado sin filtro.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filas filas del reporte
     * @param totales totales calculados
     * @return el resultado sin filtro asociado
     */
    @SuppressWarnings("rawtypes")
    protected ResultadoReporte armarResultado(List filas, Totales totales) {
        List<List<Object>> celdas = new ArrayList<>();
        for (Object fila : filas) {
            celdas.add(celdas(fila));
        }
        return new ResultadoReporte(titulo(), null, columnas(), celdas, totales);
    }

    /**
     * Completa el paso 7 asociando al resultado el filtro con que se generó (el diseño declara {@code
     * ResultadoReporte.filtro} pero {@code armarResultado} no lo recibe): llama a la versión redefinible {@code
     * armarResultado(filas, totales)} y copia su contenido con el filtro.
     *
     * <p>Agregado en la implementación (método privado auxiliar del método plantilla).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param filas filas del reporte
     * @param totales totales calculados
     * @param filtro filtro aplicado
     * @return el resultado con su filtro
     */
    @SuppressWarnings("rawtypes")
    private ResultadoReporte armarResultado(List filas, Totales totales, FiltroReporte filtro) {
        ResultadoReporte r = armarResultado(filas, totales);
        return new ResultadoReporte(r.getTitulo(), filtro, r.getColumnas(), r.getFilas(), r.getTotales());
    }

    /**
     * Gancho del paso 7: título del reporte.
     *
     * <p>Agregado en la implementación: el diseño no dice de dónde toma {@code armarResultado} el título.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return título
     */
    protected abstract String titulo();

    /**
     * Gancho del paso 7: encabezados de las columnas del detalle.
     *
     * <p>Agregado en la implementación: el diseño no dice de dónde toma {@code armarResultado} las columnas.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return encabezados
     */
    protected abstract List<String> columnas();

    /**
     * Gancho del paso 7: convierte una fila del reporte en sus celdas, en el orden de {@code columnas()}.
     *
     * <p>Agregado en la implementación: el diseño no dice cómo {@code armarResultado} convierte las filas.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param fila fila obtenida en {@code obtenerDatos}
     * @return celdas de la fila
     */
    protected abstract List<Object> celdas(Object fila);
}
