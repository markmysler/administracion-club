package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class IntencionSimulada {

    /** Atributo idPreferencia. */
    private String idPreferencia;

    /** Atributo referenciaExterna. */
    private UUID referenciaExterna;

    /** Atributo concepto. */
    private String concepto;

    /** Atributo importe. */
    private Dinero importe;

    /** Atributo venceEn. */
    private LocalDateTime venceEn;

    /** Atributo urlRetorno. */
    private String urlRetorno;
}
