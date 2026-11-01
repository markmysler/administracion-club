package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import java.util.List;

/**
 * Pago online por el identificador que le asignó la pasarela (UNIQUE cuando no es nulo). Permite reconocer una
 * notificación repetida aunque la referencia no viaje. Consulta 55 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-35
 */
public class PagoPorIdPasarela extends ConsultaEntidades<PagoOnline> {

    /** Identificador del pago en la pasarela. */
    private final String idPagoPasarela;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-35
     * @param idPagoPasarela Identificador en la pasarela.
     */
    public PagoPorIdPasarela(String idPagoPasarela) {
        this.idPagoPasarela = idPagoPasarela;
    }

    /**
     * Clase del resultado: {@code PagoOnline}.
     *
     * <p>Casos de uso: CU-35
     * @return PagoOnline.class
     */
    @Override
    public Class<PagoOnline> claseResultado() {
        return PagoOnline.class;
    }

    /**
     * Condición que se agrega al SELECT con JOIN.
     *
     * <p>Casos de uso: CU-35
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE pago_online.id_pago_pasarela = ?";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-35
     * @return [idPagoPasarela]
     */
    @Override
    public List<Object> parametros() {
        return List.of(idPagoPasarela);
    }
}
