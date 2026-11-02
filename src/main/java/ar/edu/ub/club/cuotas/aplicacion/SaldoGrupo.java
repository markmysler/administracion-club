package ar.edu.ub.club.cuotas.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.util.List;

/**
 * Resultado de {@code ServicioCuotas.consultarSaldo}: el saldo a favor del grupo y sus movimientos de crédito y
 * débito (RN-21). La pantalla lo recibe convertido en {@code ResumenSaldo}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
public class SaldoGrupo {

    /** Saldo a favor (mayor o igual a 0). */
    private final Dinero saldo;

    /** Movimientos, del más reciente al más antiguo. */
    private final List<MovimientoSaldo> movimientos;

    /**
     * Crea el resultado.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param saldo Saldo a favor.
     * @param movimientos Movimientos del grupo.
     */
    public SaldoGrupo(Dinero saldo, List<MovimientoSaldo> movimientos) {
        this.saldo = saldo;
        this.movimientos = movimientos == null ? List.of() : List.copyOf(movimientos);
    }

    /**
     * Devuelve {@code saldo}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return el valor del atributo
     */
    public Dinero getSaldo() {
        return saldo;
    }

    /**
     * Devuelve {@code movimientos}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return el valor del atributo
     */
    public List<MovimientoSaldo> getMovimientos() {
        return movimientos;
    }
}
