package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;

/**
 * Vista de la pantalla P-24: Datos personales del socio, integrantes de su grupo, indicación de titular y saldo
 * a favor del grupo.
 *
 * <p>Ruta: {@code mi-perfil}. Permiso: {@code CONSULTAR_PERFIL_PROPIO}.
 *
 * <p>Casos de uso: CU-05
 */
public class VistaMiPerfil extends VistaProtegida implements PantallaMiPerfil {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param resumen valor de {@code resumen}
     */
    @Override
    public void mostrarPerfil(ResumenPerfil resumen) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-05
     * @param saldo valor de {@code saldo}
     */
    @Override
    public void mostrarSaldo(ResumenSaldo saldo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
