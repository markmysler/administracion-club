package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;

/**
 * Interfaz pasiva de la pantalla P-05: Inicio del socio: resumen de la cuota vigente, próximas reservas y
 * accesos a reservar y pagar.
 *
 * <p>Casos de uso: CU-05, CU-29
 */
public interface PantallaInicioSocio extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-29
     * @param resumen valor de {@code resumen}
     */
    void mostrarResumen(ResumenPerfil resumen);
}
