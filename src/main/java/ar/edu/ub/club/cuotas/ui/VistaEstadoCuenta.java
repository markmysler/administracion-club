package ar.edu.ub.club.cuotas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-25: Cuotas del grupo con su estado, crédito aplicado e importe a pagar, y saldo a favor
 * del grupo.
 *
 * <p>Ruta: {@code estado-cuenta}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Casos de uso: CU-05, CU-35
 */
public class VistaEstadoCuenta extends VistaProtegida implements PantallaEstadoCuenta {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param filas valor de {@code filas}
     * @param esTitular valor de {@code esTitular}
     */
    @Override
    public void mostrarCuotas(List<FilaCuota> filas, boolean esTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param saldo valor de {@code saldo}
     */
    @Override
    public void mostrarSaldo(ResumenSaldo saldo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05, CU-35
     * @param url valor de {@code url}
     */
    @Override
    public void redirigirA(String url) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
