package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y
 * saldo a favor del grupo.
 *
 * <p>Casos de uso: CU-05, CU-35
 */
public interface PantallaEstadoCuenta extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param filas valor de {@code filas}
     * @param esTitular valor de {@code esTitular}
     */
    void mostrarCuotas(List<FilaCuota> filas, boolean esTitular);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param saldo valor de {@code saldo}
     */
    void mostrarSaldo(ResumenSaldo saldo);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param url valor de {@code url}
     */
    void redirigirA(String url);
}
