package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.cuotas.aplicacion.SaldoGrupo;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
public record ResumenSaldo(Dinero saldo, List<FilaMovimientoSaldo> movimientos) {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param saldo valor de {@code saldo}
     * @return el resultado
     */
    public static ResumenSaldo desde(SaldoGrupo saldo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
