package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import java.util.List;

/**
 * Comprobante de un pago (a lo sumo uno: UNIQUE {@code pago_id}). La usa {@code BrokerPago.cargarAsociaciones}.
 * Consulta 59 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-35, CU-36, CU-37
 */
public class ComprobanteDePago extends ConsultaEntidades<Comprobante> {

    /** Pago. */
    private final Long idPago;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-35, CU-36, CU-37
     * @param idPago Pago.
     */
    public ComprobanteDePago(Long idPago) {
        this.idPago = idPago;
    }

    /**
     * Clase del resultado: {@code Comprobante}.
     *
     * <p>Casos de uso: CU-35, CU-36, CU-37
     * @return Comprobante.class
     */
    @Override
    public Class<Comprobante> claseResultado() {
        return Comprobante.class;
    }

    /**
     * Condición.
     *
     * <p>Casos de uso: CU-35, CU-36, CU-37
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE comprobante.pago_id = ?";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-35, CU-36, CU-37
     * @return [idPago]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idPago);
    }
}
