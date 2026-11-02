package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-09: Diálogo de baja de socio en dos pasos: muestra las reservas futuras que
 * se cancelarán y, si el socio es titular de un grupo con otros integrantes activos, pide elegir el nuevo
 * titular.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-03
 */
public interface PantallaDesactivarSocio extends PantallaBase {

    /**
     * Muestra el nombre y el DNI del socio que se dará de baja.
     *
     * <p>Casos de uso: CU-03
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     */
    void mostrarSocio(String nombre, String dni);

    /**
     * Muestra las reservas futuras que se cancelarán y, si hay candidatos, el selector de nuevo titular.
     *
     * <p>Casos de uso: CU-03
     * @param reservas valor de {@code reservas}
     * @param candidatosTitular valor de {@code candidatosTitular}
     */
    void mostrarAfectadas(List<FilaResumenReserva> reservas, List<OpcionSeleccion> candidatosTitular);

    /**
     * Cierra el diálogo y dispara {@code DialogoResueltoEvento} para que P-08 recargue la ficha.
     *
     * <p>Casos de uso: CU-03
     */
    void cerrarConExito();
}
