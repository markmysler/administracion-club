package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenRecuperacion;
import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
 */
public class TokenRecuperacionPorHash extends ConsultaEntidades<TokenRecuperacion> {

    /** Atributo tokenHash. */
    private String tokenHash;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    public Class<TokenRecuperacion> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-07, CU-11, CU-13, CU-27, CU-28, CU-32, CU-35, CU-37; tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
