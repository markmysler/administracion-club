package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PantallaBase;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import java.util.List;

/**
 * Interfaz pasiva de la pantalla P-07: Formulario de alta y modificación de socio.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Interfaz pasiva del patrón MVP (Vista Pasiva): la implementa la vista y la usa el presentador.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-16
 */
public interface PantallaFormularioSocio extends PantallaBase {

    /**
     * Carga las opciones del selector de categoría.
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param opciones valor de {@code opciones}
     */
    void mostrarCategorias(List<OpcionSeleccion> opciones);

    /**
     * Carga los datos en el formulario (en el alta incluye usuario y contraseña inicial).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param datos valor de {@code datos}
     * @param esAlta valor de {@code esAlta}
     */
    void mostrarFormulario(DatosSocio datos, boolean esAlta);
}
