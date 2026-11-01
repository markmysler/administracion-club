package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;

/**
 * Interfaz pasiva de la pantalla P-24: Datos personales del socio, integrantes de su grupo, indicación de
 * titular y saldo a favor del grupo.
 *
 * <p>Casos de uso: CU-05
 */
public interface PantallaMiPerfil extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param resumen valor de {@code resumen}
     */
    void mostrarPerfil(ResumenPerfil resumen);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param saldo valor de {@code saldo}
     */
    void mostrarSaldo(ResumenSaldo saldo);
}
