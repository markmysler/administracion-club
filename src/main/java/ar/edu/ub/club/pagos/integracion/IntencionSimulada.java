package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Intención de pago que guarda en memoria {@code PasarelaSimuladaAdapter} (modo SIMULADA); P-27 la muestra con
 * {@code ResumenIntencionSimulada}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-35
 */
public class IntencionSimulada {

    /** Identificador de la preferencia simulada. */
    private final String idPreferencia;

    /** Referencia externa del pago. */
    private final UUID referenciaExterna;

    /** Concepto (cuota y período). */
    private final String concepto;

    /** Importe a pagar. */
    private final Dinero importe;

    /** Vencimiento de la intención (24 horas, RN-22). */
    private final LocalDateTime venceEn;

    /** URL de retorno ({@code /pagos/retorno?referencia=...}). */
    private final String urlRetorno;

    /**
     * Agregado en la implementación (el modelo no lista el constructor): crea la intención simulada.
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia Identificador.
     * @param referenciaExterna Referencia del pago.
     * @param concepto Concepto.
     * @param importe Importe.
     * @param venceEn Vencimiento.
     * @param urlRetorno URL de retorno.
     */
    public IntencionSimulada(String idPreferencia, UUID referenciaExterna, String concepto, Dinero importe, LocalDateTime venceEn, String urlRetorno) {
        this.idPreferencia = idPreferencia;
        this.referenciaExterna = referenciaExterna;
        this.concepto = concepto;
        this.importe = importe;
        this.venceEn = venceEn;
        this.urlRetorno = urlRetorno;
    }

    /**
     * Devuelve {@code idPreferencia}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getIdPreferencia() {
        return idPreferencia;
    }

    /**
     * Devuelve {@code referenciaExterna}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public UUID getReferenciaExterna() {
        return referenciaExterna;
    }

    /**
     * Devuelve {@code concepto}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * Devuelve {@code importe}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code venceEn}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public LocalDateTime getVenceEn() {
        return venceEn;
    }

    /**
     * Devuelve {@code urlRetorno}.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getUrlRetorno() {
        return urlRetorno;
    }
}
