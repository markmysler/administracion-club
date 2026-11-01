package ar.edu.ub.club.reportes.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDate;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class Indicadores {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo reservasHoy. */
    private Long reservasHoy;

    /** Atributo reservasSemana. */
    private Long reservasSemana;

    /** Atributo recaudacionMes. */
    private Dinero recaudacionMes;

    /** Atributo cuotasPagadasMes. */
    private Long cuotasPagadasMes;

    /** Atributo sociosActivos. */
    private Long sociosActivos;

    /** Atributo cuotasPendientes. */
    private Long cuotasPendientes;

    /** Atributo cuotasVencidas. */
    private Long cuotasVencidas;

    /** Atributo canchasMasUsadas. */
    private List<FilaUsoCancha> canchasMasUsadas;

    /** Atributo noDisponibles. */
    private List<String> noDisponibles;
}
