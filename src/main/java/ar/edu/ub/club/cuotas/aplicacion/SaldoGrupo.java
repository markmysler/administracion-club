package ar.edu.ub.club.cuotas.aplicacion;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-37
 */
public class SaldoGrupo {

    /** Atributo saldo. */
    private Dinero saldo;

    /** Atributo movimientos. */
    private List<MovimientoSaldo> movimientos;

    /**
     * Crea una instancia de {@code SaldoGrupo}.
     *
     * <p>Casos de uso: CU-37
     * @param saldo valor de {@code saldo}
     * @param movimientos valor de {@code movimientos}
     */
    public SaldoGrupo(Dinero saldo, List<MovimientoSaldo> movimientos) {
        // PENDIENTE
    }
}
