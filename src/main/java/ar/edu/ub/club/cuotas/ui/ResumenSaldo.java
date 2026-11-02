package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.aplicacion.SaldoGrupo;
import java.util.List;

/**
 * Saldo a favor del grupo con sus movimientos (P-24, P-25 y P-28).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05, CU-37
 * @param saldo saldo a favor
 * @param movimientos movimientos
 */
public record ResumenSaldo(Dinero saldo, List<FilaMovimientoSaldo> movimientos) {

    /**
     * Convierte el saldo del grupo en el modelo de vista.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param saldo saldo del grupo
     * @return el resumen
     */
    public static ResumenSaldo desde(SaldoGrupo saldo) {
        return new ResumenSaldo(saldo.getSaldo(), saldo.getMovimientos().stream().map(FilaMovimientoSaldo::desde).toList());
    }
}
