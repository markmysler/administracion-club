package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.reportes.persistencia.ConsultasReportes;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Subclase concreta del patrón <b>Template Method</b> {@code Reporte} para el reporte de cuotas y deudores (P-33):
 * redefine {@code obtenerDatos} (cuotas emitidas en el período agrupadas por grupo familiar, con {@code
 * ConsultasReportes.cuotasYDeudores}), {@code calcularTotales} (cantidad e importe de cuotas cobradas, pendientes y
 * vencidas) y {@code armarResultado} (agrega la sección de grupos deudores: solo los grupos con deuda, por deuda
 * descendente). Diagrama sec_cu39_p2.
 *
 * <p>Casos de uso: CU-39
 */
@Component
public class ReporteCuotasDeudores extends Reporte {

    /** Nombre del total de cuotas cobradas. */
    public static final String CUOTAS_COBRADAS = "Cuotas cobradas";

    /** Nombre del importe cobrado. */
    public static final String IMPORTE_COBRADO = "Importe cobrado";

    /** Nombre del total de cuotas pendientes. */
    public static final String CUOTAS_PENDIENTES = "Cuotas pendientes";

    /** Nombre del importe pendiente. */
    public static final String IMPORTE_PENDIENTE = "Importe pendiente";

    /** Nombre del total de cuotas vencidas. */
    public static final String CUOTAS_VENCIDAS = "Cuotas vencidas";

    /** Nombre del importe vencido. */
    public static final String IMPORTE_VENCIDO = "Importe vencido";

    /** Nombre de la cantidad de grupos deudores. */
    public static final String GRUPOS_DEUDORES = "Grupos deudores";

    /** Consultas de lectura de reportes (inyectada por constructor). */
    private final ConsultasReportes consultas;

    /**
     * Crea el reporte con sus consultas de lectura.
     *
     * <p>Casos de uso: CU-39
     * @param consultas consultas de reportes
     */
    public ReporteCuotasDeudores(ConsultasReportes consultas) {
        this.consultas = consultas;
    }

    /**
     * Paso 5 de CU-39: {@code consultas.cuotasYDeudores(filtro)}: una fila por grupo con cuotas emitidas en el período.
     *
     * <p>Casos de uso: CU-39
     * @param filtro período de emisión
     * @return lista de {@code FilaCuotaDeudor}
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected List obtenerDatos(FiltroReporte filtro) {
        return consultas.cuotasYDeudores(filtro);
    }

    /**
     * Paso 6 de CU-39: cantidad e importe de cuotas cobradas (PAGADAS, incluidas las canceladas con saldo a favor),
     * pendientes y vencidas. Las pendientes son las adeudadas menos las vencidas; los importes de deuda usan el
     * importe a pagar (RN-21). Sin filas todo queda en cero (FA2).
     *
     * <p>Casos de uso: CU-39
     * @param filas filas {@code FilaCuotaDeudor}
     * @return totales del reporte
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected Totales calcularTotales(List filas) {
        long cobradas = 0;
        long adeudadas = 0;
        long vencidas = 0;
        Dinero cobrado = Dinero.cero();
        Dinero deuda = Dinero.cero();
        Dinero vencido = Dinero.cero();
        for (Object o : filas) {
            FilaCuotaDeudor f = (FilaCuotaDeudor) o;
            cobradas += f.getCuotasCobradas();
            adeudadas += f.getCuotasAdeudadas();
            vencidas += f.getCuotasVencidas();
            cobrado = cobrado.sumar(f.getImporteCobrado());
            deuda = deuda.sumar(f.getDeudaTotal());
            vencido = vencido.sumar(f.getImporteVencido());
        }
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put(CUOTAS_COBRADAS, cobradas);
        valores.put(IMPORTE_COBRADO, cobrado);
        valores.put(CUOTAS_PENDIENTES, adeudadas - vencidas);
        valores.put(IMPORTE_PENDIENTE, deuda.restar(vencido));
        valores.put(CUOTAS_VENCIDAS, vencidas);
        valores.put(IMPORTE_VENCIDO, vencido);
        return new Totales(valores);
    }

    /**
     * Paso 7 de CU-39, redefinido: arma la sección de grupos deudores con los grupos que adeudan al menos una cuota,
     * ordenados por deuda descendente, y agrega a los totales la cantidad de grupos deudores. Sin deudores la
     * sección queda vacía (FA2).
     *
     * <p>Casos de uso: CU-39
     * @param filas filas {@code FilaCuotaDeudor} de todos los grupos del período
     * @param totales totales del paso 6
     * @return resultado con la sección de deudores
     */
    @Override
    @SuppressWarnings("rawtypes")
    protected ResultadoReporte armarResultado(List filas, Totales totales) {
        List<FilaCuotaDeudor> deudores = new ArrayList<>();
        for (Object o : filas) {
            FilaCuotaDeudor f = (FilaCuotaDeudor) o;
            if (f.getCuotasAdeudadas() > 0) {
                deudores.add(f);
            }
        }
        deudores.sort(Comparator.comparing(FilaCuotaDeudor::getDeudaTotal).reversed()
                .thenComparing(FilaCuotaDeudor::getIdGrupo));
        Map<String, Object> valores = new LinkedHashMap<>(totales.getValores());
        valores.put(GRUPOS_DEUDORES, (long) deudores.size());
        return super.armarResultado(deudores, new Totales(valores));
    }

    /**
     * Título del reporte.
     *
     * <p>Casos de uso: CU-39
     * @return título
     */
    @Override
    protected String titulo() {
        return "Cuotas y grupos deudores";
    }

    /**
     * Columnas de la sección de deudores: grupo, titular, DNI del titular, cuotas adeudadas y deuda total.
     *
     * <p>Casos de uso: CU-39
     * @return encabezados
     */
    @Override
    protected List<String> columnas() {
        return List.of("Grupo", "Titular", "DNI del titular", "Cuotas adeudadas", "Deuda total");
    }

    /**
     * Celdas de una {@code FilaCuotaDeudor}, en el orden de {@code columnas()}. Un grupo sin titular (no vigente)
     * muestra el titular y el DNI vacíos.
     *
     * <p>Casos de uso: CU-39
     * @param fila la fila
     * @return celdas
     */
    @Override
    protected List<Object> celdas(Object fila) {
        FilaCuotaDeudor f = (FilaCuotaDeudor) fila;
        List<Object> celdas = new ArrayList<>();
        celdas.add(f.getIdGrupo());
        celdas.add(f.getTitular() == null ? "" : f.getTitular());
        celdas.add(f.getDniTitular() == null ? "" : f.getDniTitular());
        celdas.add(f.getCuotasAdeudadas());
        celdas.add(f.getDeudaTotal());
        return celdas;
    }
}
