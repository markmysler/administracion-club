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

    /**
     * Devuelve {@code idPreferencia}: idPreferencia.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getIdPreferencia() {
        return idPreferencia;
    }

    /**
     * Devuelve {@code referenciaExterna}: referenciaExterna.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code concepto}: concepto.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Devuelve {@code importe}: importe.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code venceEn}: venceEn.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public LocalDateTime getVenceEn() {
        return venceEn;
    }

    /**
     * Devuelve {@code urlRetorno}: urlRetorno.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getUrlRetorno() {
        return urlRetorno;
    }
}
