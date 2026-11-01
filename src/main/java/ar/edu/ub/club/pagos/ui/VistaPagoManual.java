package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-30: Registro de un pago manual: búsqueda del grupo, elección de la cuota impaga, medio
 * habilitado e importe (igual al importe a pagar de la cuota).
 *
 * <p>Ruta: {@code pagos/manual}. Permiso: {@code REGISTRAR_PAGO_MANUAL}.
 *
 * <p>Casos de uso: CU-36
 */
public class VistaPagoManual extends VistaProtegida implements PantallaPagoManual {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param opciones valor de {@code opciones}
     */
    @Override
    public void mostrarMedios(List<OpcionSeleccion> opciones) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-36
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarCuotas(List<FilaCuota> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
