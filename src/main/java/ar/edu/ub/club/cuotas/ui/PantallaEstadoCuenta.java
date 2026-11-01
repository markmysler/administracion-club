package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y
 * saldo a favor del grupo (MVP, Vista Pasiva; sin tipos de Vaadin).
 *
 * <p>Casos de uso: CU-05, CU-35
 */
public interface PantallaEstadoCuenta extends PantallaBase {

    /**
     * Muestra las cuotas del grupo; el botón «Pagar online» aparece en las filas con {@code admitePagoOnline}.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param filas cuotas del grupo
     * @param esTitular true si la cuenta es titular del grupo
     */
    void mostrarCuotas(List<FilaCuota> filas, boolean esTitular);

    /**
     * Muestra el saldo a favor del grupo y sus movimientos.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param saldo saldo y movimientos
     */
    void mostrarSaldo(ResumenSaldo saldo);

    /**
     * Lleva el navegador fuera de la aplicación (página de pago de la pasarela o P-27).
     *
     * <p>Casos de uso: CU-35
     * @param url dirección de destino
     */
    void redirigirA(String url);
}
