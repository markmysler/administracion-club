package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-26 Resultado del pago. Sus operaciones pueden llamarse desde
 * el hilo del planificador: la vista las ejecuta dentro de {@code UI.access(...)} y Vaadin las envía por server push.
 *
 * <p>Casos de uso: CU-35
 */
public interface PantallaResultadoPago extends PantallaBase {

    /**
     * Muestra el estado del pago informado por la fachada (pasos 23 y 24, FA7, FA8, FA12).
     *
     * <p>Casos de uso: CU-35
     * @param resumen estado del pago
     */
    void mostrarEstado(ResumenPagoOnline resumen);

    /**
     * Informa que el pago sigue en proceso después de diez consultas (FA13, estado «Sin confirmar»).
     *
     * <p>Casos de uso: CU-35
     */
    void mostrarEsperaConfirmacion();
}
