package ar.edu.ub.club.compartido.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;

/**
 * Opción del menú lateral (record inmutable) que registra cada módulo con su {@code Menu<Módulo>}; el menú solo
 * muestra las opciones cuyo permiso tiene la cuenta.
 *
 * <p>Casos de uso: todos
 *
 * @param etiqueta texto visible
 * @param ruta ruta de la pantalla
 * @param permiso permiso necesario para verla
 * @param area agrupación en el menú
 * @param orden orden dentro del menú
 */
public record OpcionMenu(String etiqueta, String ruta, Permiso permiso, String area, Integer orden) {
}
