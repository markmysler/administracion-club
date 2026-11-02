package ar.edu.ub.club.reportes.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.reportes.persistencia.ConsultasReportes;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del patrón Template Method {@code Reporte}: el orden fijo de los pasos de {@code generar}, la validación
 * del filtro (FA1) y los pasos redefinidos por {@code ReporteUsoCanchas} y {@code ReporteCuotasDeudores}, con datos
 * falsos (sin base de datos).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
class ReporteTest {

    /** Filtro válido de noviembre de 2026. */
    private final FiltroReporte noviembre = new FiltroReporte(LocalDate.of(2026, 11, 1), LocalDate.of(2026, 11, 30));

    /**
     * Reporte falso que registra el orden en que el método plantilla llama a los pasos.
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    private static final class ReporteFalso extends Reporte {

        /** Pasos ejecutados, en orden. */
        private final List<String> pasos = new ArrayList<>();

        /**
         * Registra la validación y delega en la del método plantilla.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @param filtro filtro
         */
        @Override
        protected void validarFiltro(FiltroReporte filtro) {
            pasos.add("validarFiltro");
            super.validarFiltro(filtro);
        }

        /**
         * Devuelve dos filas falsas.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @param filtro filtro
         * @return filas
         */
        @Override
        @SuppressWarnings("rawtypes")
        protected List obtenerDatos(FiltroReporte filtro) {
            pasos.add("obtenerDatos");
            return List.of("a", "b");
        }

        /**
         * Cuenta las filas.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @param filas filas
         * @return totales
         */
        @Override
        @SuppressWarnings("rawtypes")
        protected Totales calcularTotales(List filas) {
            pasos.add("calcularTotales");
            return new Totales(Map.of("Filas", (long) filas.size()));
        }

        /**
         * Título falso.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @return título
         */
        @Override
        protected String titulo() {
            return "Falso";
        }

        /**
         * Una columna.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @return columnas
         */
        @Override
        protected List<String> columnas() {
            return List.of("Valor");
        }

        /**
         * La fila es su propia celda.
         *
         * <p>Casos de uso: CU-38, CU-39
         * @param fila fila
         * @return celdas
         */
        @Override
        protected List<Object> celdas(Object fila) {
            pasos.add("celdas");
            return List.of(fila);
        }
    }

    /**
     * {@code generar} ejecuta validar → obtener → totales → armar y asocia el filtro al resultado.
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    @Test
    void generarRespetaElOrdenDeLosPasos() {
        ReporteFalso reporte = new ReporteFalso();
        ResultadoReporte r = reporte.generar(noviembre);
        assertEquals(List.of("validarFiltro", "obtenerDatos", "calcularTotales", "celdas", "celdas"), reporte.pasos);
        assertEquals("Falso", r.getTitulo());
        assertEquals(noviembre, r.getFiltro());
        assertEquals(List.of(List.of("a"), List.of("b")), r.getFilas());
        assertEquals(2L, r.getTotales().getValores().get("Filas"));
    }

    /**
     * Filtro incompleto, invertido o de más de 12 meses: {@code ErrorNegocio} y no se consultan datos (FA1).
     *
     * <p>Casos de uso: CU-38, CU-39
     */
    @Test
    void filtroInvalidoCortaElMetodoPlantilla() {
        ReporteFalso reporte = new ReporteFalso();
        assertThrows(ErrorNegocio.class, () -> reporte.generar(new FiltroReporte(null, LocalDate.of(2026, 1, 1))));
        assertThrows(ErrorNegocio.class,
                () -> reporte.generar(new FiltroReporte(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1))));
        assertThrows(ErrorNegocio.class,
                () -> reporte.generar(new FiltroReporte(LocalDate.of(2025, 1, 1), LocalDate.of(2026, 6, 1))));
        assertTrue(reporte.pasos.stream().noneMatch("obtenerDatos"::equals));
    }

    /**
     * {@code ReporteUsoCanchas}: totales de reservas, horas y ocupación promedio; sin filas, todo en cero (FA2).
     *
     * <p>Casos de uso: CU-38
     */
    @Test
    void usoCanchasCalculaTotales() {
        ConsultasReportes consultas = mock(ConsultasReportes.class);
        when(consultas.usoCanchas(noviembre)).thenReturn(List.of(
                new FilaUsoCancha("Tenis", "Tenis 1", 2L, new BigDecimal("2.00"), new BigDecimal("10.00")),
                new FilaUsoCancha("Pádel", "Pádel 1", 1L, new BigDecimal("1.50"), new BigDecimal("5.00"))));
        ResultadoReporte r = new ReporteUsoCanchas(consultas).generar(noviembre);
        Map<String, Object> t = r.getTotales().getValores();
        assertEquals(3L, t.get(ReporteUsoCanchas.TOTAL_RESERVAS));
        assertEquals(new BigDecimal("3.50"), t.get(ReporteUsoCanchas.TOTAL_HORAS));
        assertEquals(new BigDecimal("7.50"), t.get(ReporteUsoCanchas.TOTAL_OCUPACION));
        assertEquals(2, r.getFilas().size());
        assertEquals(5, r.getColumnas().size());

        when(consultas.usoCanchas(noviembre)).thenReturn(List.of());
        ResultadoReporte vacio = new ReporteUsoCanchas(consultas).generar(noviembre);
        assertEquals(0L, vacio.getTotales().getValores().get(ReporteUsoCanchas.TOTAL_RESERVAS));
        assertEquals(BigDecimal.ZERO, vacio.getTotales().getValores().get(ReporteUsoCanchas.TOTAL_OCUPACION));
    }

    /**
     * {@code ReporteCuotasDeudores}: totales por estado y sección de deudores solo con grupos con deuda, por deuda
     * descendente (armarResultado redefinido).
     *
     * <p>Casos de uso: CU-39
     */
    @Test
    void cuotasDeudoresArmaSeccionDeDeudores() {
        ConsultasReportes consultas = mock(ConsultasReportes.class);
        when(consultas.cuotasYDeudores(noviembre)).thenReturn(List.of(
                new FilaCuotaDeudor(1L, "Pérez, Juan", "20111222", 1, Dinero.de("65000"), 1, Dinero.de("65000"),
                        0, Dinero.cero()),
                new FilaCuotaDeudor(2L, "García, Ana", "30444555", 2, Dinero.de("100000"), 0, Dinero.cero(), 1,
                        Dinero.de("50000")),
                new FilaCuotaDeudor(3L, "López, Carlos", "25777888", 0, Dinero.cero(), 1, Dinero.de("35000"), 0,
                        Dinero.cero())));
        ResultadoReporte r = new ReporteCuotasDeudores(consultas).generar(noviembre);
        Map<String, Object> t = r.getTotales().getValores();
        assertEquals(2L, t.get(ReporteCuotasDeudores.CUOTAS_COBRADAS));
        assertEquals(Dinero.de("100000"), t.get(ReporteCuotasDeudores.IMPORTE_COBRADO));
        assertEquals(2L, t.get(ReporteCuotasDeudores.CUOTAS_PENDIENTES));
        assertEquals(Dinero.de("115000"), t.get(ReporteCuotasDeudores.IMPORTE_PENDIENTE));
        assertEquals(1L, t.get(ReporteCuotasDeudores.CUOTAS_VENCIDAS));
        assertEquals(2L, t.get(ReporteCuotasDeudores.GRUPOS_DEUDORES));
        assertEquals(2, r.getFilas().size());
        assertEquals(2L, r.getFilas().get(0).get(0));
        assertEquals(1L, r.getFilas().get(1).get(0));
    }

    /**
     * {@code ServicioReportes} no llega a consultar si el filtro es inválido (verificación de la colaboración).
     *
     * <p>Casos de uso: CU-38
     */
    @Test
    void filtroInvalidoNoConsulta() {
        ConsultasReportes consultas = mock(ConsultasReportes.class);
        FiltroReporte invertido = new FiltroReporte(LocalDate.of(2026, 2, 1), LocalDate.of(2026, 1, 1));
        assertThrows(ErrorNegocio.class, () -> new ReporteUsoCanchas(consultas).generar(invertido));
        verify(consultas, never()).usoCanchas(invertido);
    }
}
