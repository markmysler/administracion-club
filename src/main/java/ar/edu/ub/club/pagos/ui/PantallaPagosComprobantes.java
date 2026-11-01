package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-28: Listado de pagos con estado (incluidos los ACREDITADOS) y acceso al
 * comprobante, y saldo a favor del grupo.
 *
 * <p>Casos de uso: CU-37
 */
public interface PantallaPagosComprobantes extends PantallaBase {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param filas valor de {@code filas}
     */
    void mostrarPagos(List<FilaPago> filas);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param saldo valor de {@code saldo}
     */
    void mostrarSaldo(ResumenSaldo saldo);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param visible valor de {@code visible}
     */
    void mostrarFiltroGrupo(boolean visible);

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param filas valor de {@code filas}
     */
    void mostrarGrupos(List<FilaGrupo> filas);
}
