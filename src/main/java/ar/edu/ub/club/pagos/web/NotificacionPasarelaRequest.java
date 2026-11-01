package ar.edu.ub.club.pagos.web;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Map;

/**
 * Cuerpo JSON de la notificación de la pasarela que recibe el webhook. Acepta los nombres propios ({@code tipo},
 * {@code idPagoPasarela}) y el formato de Mercado Pago ({@code type} y {@code data.id}); el resto del cuerpo se
 * ignora: el resultado siempre se consulta a la pasarela (RN-17).
 *
 * <p>Casos de uso: CU-35
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificacionPasarelaRequest {

    /** Tipo de evento informado. */
    @JsonAlias("type")
    private String tipo;

    /** Identificador del pago en la pasarela. */
    private String idPagoPasarela;

    /**
     * Constructor vacío que usa Jackson.
     *
     * <p>Casos de uso: CU-35
     */
    public NotificacionPasarelaRequest() {
        // lo completa Jackson
    }

    /**
     * Agregado en la implementación: crea la notificación (pruebas).
     *
     * <p>Casos de uso: CU-35
     * @param tipo tipo de evento
     * @param idPagoPasarela identificador del pago
     */
    public NotificacionPasarelaRequest(String tipo, String idPagoPasarela) {
        this.tipo = tipo;
        this.idPagoPasarela = idPagoPasarela;
    }

    /**
     * Devuelve {@code tipo}: Tipo de evento informado.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * Asigna {@code tipo} (Jackson).
     *
     * <p>Casos de uso: CU-35
     * @param tipo nuevo valor
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * Devuelve {@code idPagoPasarela}: Identificador del pago en la pasarela.
     *
     * <p>Casos de uso: CU-35
     * @return el valor del atributo
     */
    public String getIdPagoPasarela() {
        return idPagoPasarela;
    }

    /**
     * Asigna {@code idPagoPasarela} (Jackson).
     *
     * <p>Casos de uso: CU-35
     * @param idPagoPasarela nuevo valor
     */
    public void setIdPagoPasarela(String idPagoPasarela) {
        this.idPagoPasarela = idPagoPasarela;
    }

    /**
     * Lee el {@code data.id} del formato de Mercado Pago.
     *
     * <p>Casos de uso: CU-35
     * @param data objeto {@code data} de la notificación
     */
    @JsonProperty("data")
    public void setData(Map<String, Object> data) {
        if (data != null && data.get("id") != null && idPagoPasarela == null) {
            this.idPagoPasarela = data.get("id").toString();
        }
    }
}
