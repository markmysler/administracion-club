package ar.edu.ub.club.compartido.seguridad;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import org.springframework.stereotype.Component;

/**
 * Verifica la autorización de cada operación. Los permisos los devuelve {@code Autorizable.obtenerPermisos()},
 * que {@code Socio} y {@code Administrador} redefinen (polimorfismo): no existe atributo de rol ni comparación
 * por tipo. La pertenencia (RN-20) la verifican además las fachadas con las reglas de su dominio.
 *
 * <p>El modelo la declara como interfaz sin implementación nombrada; se implementa como clase concreta (bean
 * único de Spring) con exactamente las dos operaciones del diseño.
 *
 * <p>Casos de uso: todos
 */
@Component
public class ControlAcceso {

    /**
     * Si {@code usuario.obtenerPermisos()} no contiene el permiso lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: todos
     * @param usuario cuenta de la sesión
     * @param permiso permiso de la operación
     * @throws AccesoDenegadoException si la cuenta no tiene el permiso
     */
    public void verificar(Autorizable usuario, Permiso permiso) {
        if (!tienePermiso(usuario, permiso)) {
            throw new AccesoDenegadoException("Falta el permiso " + permiso);
        }
    }

    /**
     * Misma consulta polimórfica que {@code verificar}, sin lanzar excepción. La usan {@code VistaProtegida}
     * (permiso de la pantalla), {@code MenuNavegacion} (opciones visibles), {@code VistaRaiz} y los presentadores
     * que adaptan la pantalla a los permisos (P-19, P-28).
     *
     * <p>Casos de uso: todos
     * @param usuario cuenta de la sesión (puede ser {@code null})
     * @param permiso permiso consultado
     * @return true si la cuenta tiene el permiso
     */
    public boolean tienePermiso(Autorizable usuario, Permiso permiso) {
        return usuario != null && usuario.obtenerPermisos().contains(permiso);
    }
}
