package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-28: Listado de pagos con estado (incluidos los ACREDITADOS) y acceso al comprobante, y
 * saldo a favor del grupo.
 *
 * <p>Ruta: {@code pagos?periodo=…}. Permiso: {@code CONSULTAR_PAGOS_PROPIOS}.
 *
 * <p>Casos de uso: CU-37
 */
public class VistaPagosComprobantes extends VistaProtegida implements PantallaPagosComprobantes {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @return el resultado
     */
    protected Permiso permisoAlternativo() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarPagos(List<FilaPago> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param saldo valor de {@code saldo}
     */
    @Override
    public void mostrarSaldo(ResumenSaldo saldo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param visible valor de {@code visible}
     */
    @Override
    public void mostrarFiltroGrupo(boolean visible) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-37
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
