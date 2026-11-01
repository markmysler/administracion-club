package ar.edu.ub.club.pagos.integracion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import java.util.UUID;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ResultadoPasarela {

    /** Atributo idPagoPasarela. */
    private String idPagoPasarela;

    /** Atributo referenciaExterna. */
    private UUID referenciaExterna;

    /** Atributo estado. */
    private String estado;

    /** Atributo importe. */
    private Dinero importe;

    /**
     * Devuelve {@code idPagoPasarela}: idPagoPasarela.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getIdPagoPasarela() {
        return idPagoPasarela;
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
     * Devuelve {@code estado}: estado.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getEstado() {
        return estado;
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
}
