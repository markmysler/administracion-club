package ar.edu.ub.club.compartido.ui;

import java.util.List;

/**
 * Interfaz que implementa el bean {@code Menu<Módulo>} de cada módulo para aportar sus opciones al menú común, de
 * modo que {@code compartido.ui} no depende de ningún módulo (D-10).
 *
 * <p>Casos de uso: todos
 */
public interface ProveedorOpcionesMenu {

    /**
     * Opciones de menú del módulo.
     *
     * <p>Casos de uso: todos
     * @return opciones
     */
    List<OpcionMenu> opciones();
}
