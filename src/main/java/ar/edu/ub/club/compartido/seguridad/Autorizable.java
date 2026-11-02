package ar.edu.ub.club.compartido.seguridad;

import java.util.Set;

/**
 * Interfaz que implementa {@code Usuario}. Permite que {@code ControlAcceso} verifique permisos sin depender del
 * módulo accesos (D-10).
 *
 * <p>Casos de uso: todos
 */
public interface Autorizable {

    /**
     * Permisos de la cuenta; {@code Socio} y {@code Administrador} devuelven conjuntos distintos (polimorfismo).
     *
     * <p>Casos de uso: todos
     * @return conjunto de {@code Permiso}
     */
    Set<Permiso> obtenerPermisos();
}
