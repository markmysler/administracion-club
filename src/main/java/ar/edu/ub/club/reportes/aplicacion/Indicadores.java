package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDate;
import java.util.List;

/**
 * Indicadores del panel de administración P-04: reservas del día y de la semana, recaudación y cuotas pagadas del
 * mes, socios activos, cuotas pendientes y vencidas y canchas más usadas del mes. Los calcula {@code
 * ConsultasReportes.indicadores} con un objeto consulta de proyección por indicador; el valor de un indicador cuya
 * consulta falló queda en {@code null} y su nombre se agrega a {@code noDisponibles} (CU-40, FA1).
 *
 * <p>Objeto de datos de salida de la capa de aplicación (inmutable).
 *
 * <p>Casos de uso: CU-40
 */
public class Indicadores {

    /** Nombre del indicador de reservas del día (se usa en {@code noDisponibles}). */
    public static final String RESERVAS_HOY = "reservasHoy";

    /** Nombre del indicador de reservas de la semana. */
    public static final String RESERVAS_SEMANA = "reservasSemana";

    /** Nombre del indicador de recaudación del mes. */
    public static final String RECAUDACION_MES = "recaudacionMes";

    /** Nombre del indicador de cuotas pagadas en el mes. */
    public static final String CUOTAS_PAGADAS_MES = "cuotasPagadasMes";

    /** Nombre del indicador de socios activos. */
    public static final String SOCIOS_ACTIVOS = "sociosActivos";

    /** Nombre del indicador de cuotas pendientes. */
    public static final String CUOTAS_PENDIENTES = "cuotasPendientes";

    /** Nombre del indicador de cuotas vencidas. */
    public static final String CUOTAS_VENCIDAS = "cuotasVencidas";

    /** Nombre del indicador de canchas más usadas del mes. */
    public static final String CANCHAS_MAS_USADAS = "canchasMasUsadas";

    /** Fecha de referencia (hoy, según el {@code Reloj}). */
    private LocalDate fecha;

    /** Reservas confirmadas o finalizadas del día. */
    private Long reservasHoy;

    /** Reservas confirmadas o finalizadas de la semana (lunes a domingo). */
    private Long reservasSemana;

    /** Importe de los pagos APROBADOS desde el inicio del mes hasta hoy. */
    private Dinero recaudacionMes;

    /** Cuotas pagadas desde el inicio del mes hasta hoy. */
    private Long cuotasPagadasMes;

    /** Socios con la cuenta ACTIVA. */
    private Long sociosActivos;

    /** Cuotas en estado Pendiente. */
    private Long cuotasPendientes;

    /** Cuotas en estado VENCIDA. */
    private Long cuotasVencidas;

    /** Hasta cinco canchas con más reservas en el mes. */
    private List<FilaUsoCancha> canchasMasUsadas;

    /** Nombres de los indicadores cuya consulta falló (CU-40, FA1). */
    private List<String> noDisponibles;

    /**
     * Crea los indicadores con todos sus valores.
     *
     * <p>Agregado en la implementación: constructor con los atributos del diseño.
     *
     * <p>Casos de uso: CU-40
     * @param fecha fecha de referencia
     * @param reservasHoy reservas del día o {@code null} si no está disponible
     * @param reservasSemana reservas de la semana o {@code null}
     * @param recaudacionMes recaudación del mes o {@code null}
     * @param cuotasPagadasMes cuotas pagadas en el mes o {@code null}
     * @param sociosActivos socios activos o {@code null}
     * @param cuotasPendientes cuotas pendientes o {@code null}
     * @param cuotasVencidas cuotas vencidas o {@code null}
     * @param canchasMasUsadas canchas más usadas (vacía si no está disponible)
     * @param noDisponibles nombres de los indicadores que fallaron
     */
    public Indicadores(LocalDate fecha, Long reservasHoy, Long reservasSemana, Dinero recaudacionMes,
                       Long cuotasPagadasMes, Long sociosActivos, Long cuotasPendientes, Long cuotasVencidas,
                       List<FilaUsoCancha> canchasMasUsadas, List<String> noDisponibles) {
        this.fecha = fecha;
        this.reservasHoy = reservasHoy;
        this.reservasSemana = reservasSemana;
        this.recaudacionMes = recaudacionMes;
        this.cuotasPagadasMes = cuotasPagadasMes;
        this.sociosActivos = sociosActivos;
        this.cuotasPendientes = cuotasPendientes;
        this.cuotasVencidas = cuotasVencidas;
        this.canchasMasUsadas = List.copyOf(canchasMasUsadas);
        this.noDisponibles = List.copyOf(noDisponibles);
    }

    /**
     * Devuelve la fecha de referencia.
     *
     * <p>Casos de uso: CU-40
     * @return fecha
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Devuelve las reservas del día.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getReservasHoy() {
        return reservasHoy;
    }

    /**
     * Devuelve las reservas de la semana.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getReservasSemana() {
        return reservasSemana;
    }

    /**
     * Devuelve la recaudación del mes.
     *
     * <p>Casos de uso: CU-40
     * @return importe o {@code null} si no está disponible
     */
    public Dinero getRecaudacionMes() {
        return recaudacionMes;
    }

    /**
     * Devuelve las cuotas pagadas en el mes.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getCuotasPagadasMes() {
        return cuotasPagadasMes;
    }

    /**
     * Devuelve los socios activos.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getSociosActivos() {
        return sociosActivos;
    }

    /**
     * Devuelve las cuotas pendientes.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getCuotasPendientes() {
        return cuotasPendientes;
    }

    /**
     * Devuelve las cuotas vencidas.
     *
     * <p>Casos de uso: CU-40
     * @return cantidad o {@code null} si no está disponible
     */
    public Long getCuotasVencidas() {
        return cuotasVencidas;
    }

    /**
     * Devuelve las canchas más usadas del mes.
     *
     * <p>Casos de uso: CU-40
     * @return hasta cinco filas
     */
    public List<FilaUsoCancha> getCanchasMasUsadas() {
        return canchasMasUsadas;
    }

    /**
     * Devuelve los nombres de los indicadores no disponibles.
     *
     * <p>Casos de uso: CU-40
     * @return nombres (constantes de esta clase)
     */
    public List<String> getNoDisponibles() {
        return noDisponibles;
    }
}
