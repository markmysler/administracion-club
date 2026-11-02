package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;

/**
 * Interfaz pasiva de la pantalla P-24: Datos personales del socio, integrantes de su grupo, indicación de
 * titular y saldo a favor del grupo.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-05
 */
public interface PantallaMiPerfil extends PantallaBase {

    /**
     * Muestra los datos personales, el grupo con sus integrantes y la cuota vigente.
     *
     * <p>Casos de uso: CU-05
     * @param resumen valor de {@code resumen}
     */
    void mostrarPerfil(ResumenPerfil resumen);

    /**
     * Muestra el saldo a favor del grupo y sus movimientos.
     *
     * <p>Casos de uso: CU-05
     * @param saldo valor de {@code saldo}
     */
    void mostrarSaldo(ResumenSaldo saldo);
}
