package ar.edu.ub.club.reportes.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.reportes.aplicacion.FilaUsoCancha;
import ar.edu.ub.club.reportes.aplicacion.Indicadores;
import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de vista (record inmutable) del panel P-04: valores de los indicadores (en {@code null} los no
 * disponibles), las canchas más usadas como texto y los nombres de los indicadores no disponibles (MODELO 11.6.1).
 *
 * <p>Casos de uso: CU-40
 * @param fecha fecha de referencia
 * @param reservasHoy reservas del día
 * @param reservasSemana reservas de la semana
 * @param recaudacionMes cobrado en el mes
 * @param cuotasPagadasMes cuotas pagadas en el mes
 * @param sociosActivos socios activos
 * @param cuotasPendientes cuotas pendientes
 * @param cuotasVencidas cuotas vencidas
 * @param canchasMasUsadas «Deporte · Cancha: n reservas», hasta cinco
 * @param noDisponibles nombres de los indicadores que fallaron (constantes de {@code Indicadores})
 */
public record ResumenIndicadores(LocalDate fecha, Long reservasHoy, Long reservasSemana, Dinero recaudacionMes,
                                 Long cuotasPagadasMes, Long sociosActivos, Long cuotasPendientes,
                                 Long cuotasVencidas, List<String> canchasMasUsadas, List<String> noDisponibles) {

    /**
     * Fábrica: copia los valores de {@code Indicadores} y convierte cada cancha más usada en texto.
     *
     * <p>Casos de uso: CU-40
     * @param indicadores indicadores calculados por la fachada
     * @return el modelo de vista
     */
    public static ResumenIndicadores desde(Indicadores indicadores) {
        List<String> canchas = indicadores.getCanchasMasUsadas().stream()
                .map(ResumenIndicadores::textoCancha)
                .toList();
        return new ResumenIndicadores(indicadores.getFecha(), indicadores.getReservasHoy(),
                indicadores.getReservasSemana(), indicadores.getRecaudacionMes(), indicadores.getCuotasPagadasMes(),
                indicadores.getSociosActivos(), indicadores.getCuotasPendientes(), indicadores.getCuotasVencidas(),
                canchas, List.copyOf(indicadores.getNoDisponibles()));
    }

    /**
     * Indica si un indicador figura como no disponible (la vista muestra «No disponible» en su tarjeta).
     *
     * <p>Agregado en la implementación: consulta de conveniencia del record.
     *
     * <p>Casos de uso: CU-40
     * @param nombre nombre del indicador
     * @return {@code true} si su consulta falló
     */
    public boolean noDisponible(String nombre) {
        return noDisponibles.contains(nombre);
    }

    /**
     * Texto de una cancha más usada.
     *
     * <p>Casos de uso: CU-40
     * @param fila fila de uso de la cancha
     * @return «Deporte · Cancha: n reservas»
     */
    private static String textoCancha(FilaUsoCancha fila) {
        long n = fila.getCantidadReservas();
        return fila.getDeporte() + " · " + fila.getCancha() + ": " + n + (n == 1 ? " reserva" : " reservas");
    }
}
