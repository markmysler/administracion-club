package ar.edu.ub.club.reportes.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.reportes.aplicacion.FilaCuotaDeudor;
import ar.edu.ub.club.reportes.aplicacion.FilaUsoCancha;
import ar.edu.ub.club.reportes.aplicacion.FiltroReporte;
import ar.edu.ub.club.reportes.aplicacion.Indicadores;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Consultas de lectura de reportes (reportes.persistencia; clase, ya no interfaz: MODELO 11.8.5). Resuelve objetos
 * consulta de proyección con la {@code FachadaPersistencia}: {@code usoCanchas} con {@code UsoDeCanchas}, {@code
 * cuotasYDeudores} con {@code CuotasYDeudores} e {@code indicadores} con un objeto consulta por indicador ({@code
 * ReservasEntre}, {@code RecaudacionEntre}, {@code CuotasPagadasEntre}, {@code SociosActivos}, {@code
 * CuotasEnEstado}, {@code CanchasMasUsadas}); el que falla se agrega a {@code noDisponibles} y el resto se calcula
 * igual (CU-40, FA1).
 *
 * <p>Cada indicador se consulta dentro de una transacción anidada ({@code PROPAGATION_NESTED}, un punto de guardado
 * JDBC): en PostgreSQL una sentencia que falla anula la transacción entera y, sin el punto de guardado, las consultas
 * siguientes también fallarían.
 *
 * <p>Casos de uso: CU-38, CU-39, CU-40
 */
@Component
public class ConsultasReportes {

    /** Cantidad de canchas del indicador «Canchas más utilizadas». */
    private static final int LIMITE_CANCHAS = 5;

    /** Bitácora para registrar los indicadores que fallan. */
    private static final Logger BITACORA = LoggerFactory.getLogger(ConsultasReportes.class);

    /** Fachada de persistencia (Singleton) que resuelve los objetos consulta. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Agregado en la implementación: plantilla de transacción anidada (punto de guardado) para aislar cada indicador.
     */
    private final TransactionTemplate transaccionAnidada;

    /**
     * Crea las consultas con la fachada de persistencia y el administrador de transacciones.
     *
     * <p>Casos de uso: CU-38, CU-39, CU-40
     * @param fachadaPersistencia fachada de persistencia
     * @param administradorTransacciones administrador de transacciones de Spring (agregado en la implementación)
     */
    public ConsultasReportes(FachadaPersistencia fachadaPersistencia,
                             PlatformTransactionManager administradorTransacciones) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.transaccionAnidada = new TransactionTemplate(administradorTransacciones);
        this.transaccionAnidada.setPropagationBehavior(TransactionDefinition.PROPAGATION_NESTED);
        this.transaccionAnidada.setReadOnly(true);
    }

    /**
     * Uso de canchas del período: {@code buscar(UsoDeCanchas(filtro))} (paso 5 de CU-38, sec_cu38_p3).
     *
     * <p>Casos de uso: CU-38
     * @param filtro filtro validado
     * @return filas por deporte y cancha
     */
    public List<FilaUsoCancha> usoCanchas(FiltroReporte filtro) {
        return fachadaPersistencia.buscar(new UsoDeCanchas(filtro));
    }

    /**
     * Cuotas del período por grupo: {@code buscar(CuotasYDeudores(filtro))} (paso 5 de CU-39, sec_cu39_p2).
     *
     * <p>Casos de uso: CU-39
     * @param filtro filtro validado
     * @return filas por grupo familiar
     */
    public List<FilaCuotaDeudor> cuotasYDeudores(FiltroReporte filtro) {
        return fachadaPersistencia.buscar(new CuotasYDeudores(filtro));
    }

    /**
     * Indicadores del panel (pasos 3 a 7 de CU-40, sec_cu40): {@code buscarUno(ReservasEntre(hoy, hoy))}, {@code
     * buscarUno(ReservasEntre(inicioSemana, finSemana))}, {@code buscarUno(RecaudacionEntre(inicioMes, hoy))}, {@code
     * buscarUno(CuotasPagadasEntre(inicioMes, hoy))}, {@code buscarUno(SociosActivos())}, {@code
     * buscarUno(CuotasEnEstado("Pendiente"))}, {@code buscarUno(CuotasEnEstado("VENCIDA"))} y {@code
     * buscar(CanchasMasUsadas(inicioMes, hoy, 5))}. La semana va de lunes a domingo.
     *
     * <p>Casos de uso: CU-40
     * @param hoy fecha de referencia
     * @return indicadores; los que fallaron, en {@code noDisponibles}
     */
    public Indicadores indicadores(LocalDate hoy) {
        LocalDate inicioSemana = hoy.with(DayOfWeek.MONDAY);
        LocalDate finSemana = hoy.with(DayOfWeek.SUNDAY);
        LocalDate inicioMes = hoy.withDayOfMonth(1);
        List<String> noDisponibles = new ArrayList<>();
        Long reservasHoy = indicador(Indicadores.RESERVAS_HOY, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new ReservasEntre(hoy, hoy)).orElse(0L));
        Long reservasSemana = indicador(Indicadores.RESERVAS_SEMANA, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new ReservasEntre(inicioSemana, finSemana)).orElse(0L));
        Dinero recaudacion = indicador(Indicadores.RECAUDACION_MES, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new RecaudacionEntre(inicioMes, hoy)).orElse(Dinero.cero()));
        Long cuotasPagadas = indicador(Indicadores.CUOTAS_PAGADAS_MES, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new CuotasPagadasEntre(inicioMes, hoy)).orElse(0L));
        Long socios = indicador(Indicadores.SOCIOS_ACTIVOS, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new SociosActivos()).orElse(0L));
        Long pendientes = indicador(Indicadores.CUOTAS_PENDIENTES, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new CuotasEnEstado("PENDIENTE")).orElse(0L));
        Long vencidas = indicador(Indicadores.CUOTAS_VENCIDAS, noDisponibles,
                () -> fachadaPersistencia.buscarUno(new CuotasEnEstado("VENCIDA")).orElse(0L));
        List<FilaUsoCancha> canchas = indicador(Indicadores.CANCHAS_MAS_USADAS, noDisponibles,
                () -> fachadaPersistencia.buscar(new CanchasMasUsadas(inicioMes, hoy, LIMITE_CANCHAS)));
        return new Indicadores(hoy, reservasHoy, reservasSemana, recaudacion, cuotasPagadas, socios, pendientes,
                vencidas, canchas == null ? List.of() : canchas, noDisponibles);
    }

    /**
     * Calcula un indicador en su propia transacción anidada; si la consulta falla, registra el error, agrega el
     * nombre a {@code noDisponibles} y devuelve {@code null} (CU-40, FA1).
     *
     * <p>Casos de uso: CU-40
     * @param <T> tipo del valor del indicador
     * @param nombre nombre del indicador (constante de {@code Indicadores})
     * @param noDisponibles lista de indicadores que fallaron
     * @param consulta cálculo del indicador
     * @return el valor o {@code null} si no está disponible
     */
    private <T> T indicador(String nombre, List<String> noDisponibles, Supplier<T> consulta) {
        try {
            return transaccionAnidada.execute(estado -> consulta.get());
        } catch (RuntimeException e) {
            BITACORA.warn("No se pudo calcular el indicador {}", nombre, e);
            noDisponibles.add(nombre);
            return null;
        }
    }
}
