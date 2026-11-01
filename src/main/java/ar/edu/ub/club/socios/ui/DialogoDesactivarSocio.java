package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.DialogoBase;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import java.util.List;

/**
 * Vista de la pantalla P-09: Diálogo de baja de socio en dos pasos: muestra las reservas futuras que se
 * cancelarán y, si el socio es titular de un grupo con otros integrantes activos, pide elegir el nuevo titular.
 *
 * <p>Casos de uso: CU-03
 */
public class DialogoDesactivarSocio extends DialogoBase implements PantallaDesactivarSocio {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     */
    @Override
    public void mostrarSocio(String nombre, String dni) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03
     * @param reservas valor de {@code reservas}
     * @param candidatosTitular valor de {@code candidatosTitular}
     */
    @Override
    public void mostrarAfectadas(List<FilaResumenReserva> reservas, List<OpcionSeleccion> candidatosTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03
     */
    @Override
    public void cerrarConExito() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
