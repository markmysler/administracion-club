package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import java.util.List;

/**
 * Interfaz pasiva (MVP Vista Pasiva) de la pantalla P-28 Pagos y comprobantes.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
public interface PantallaPagosComprobantes extends PantallaBase {

    /**
     * Muestra la grilla de pagos (pasos 4 y 7).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param filas pagos, más recientes primero
     */
    void mostrarPagos(List<FilaPago> filas);

    /**
     * Muestra el saldo a favor del grupo y sus movimientos (paso 5); {@code null} oculta la línea de saldo.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param saldo saldo del grupo o {@code null}
     */
    void mostrarSaldo(ResumenSaldo saldo);

    /**
     * Muestra u oculta el filtro por grupo y la columna de grupo (solo para el administrador).
     *
     * <p>Casos de uso: CU-37
     * @param visible {@code true} para el administrador
     */
    void mostrarFiltroGrupo(boolean visible);

    /**
     * Muestra los grupos sugeridos para el filtro del administrador.
     *
     * <p>Casos de uso: CU-37
     * @param filas grupos encontrados
     */
    void mostrarGrupos(List<FilaGrupo> filas);
}
