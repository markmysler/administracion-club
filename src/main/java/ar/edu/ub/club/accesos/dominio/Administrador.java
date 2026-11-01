package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Subclase de {@code Usuario} (accesos.dominio) para las cuentas de administrador.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class Administrador extends Usuario {

    /** Nombre. */
    private String nombre;

    /** Apellido. */
    private String apellido;

    /**
     * Constructor que usa {@code ServicioAccesos.crearAdministrador} (CU-14, CU-16).
     *
     * <p>Casos de uso: CU-14, CU-16
     * @param nombre valor de {@code nombre}
     * @param apellido valor de {@code apellido}
     * @param username valor de {@code username}
     * @param email valor de {@code email}
     * @param passwordHash valor de {@code passwordHash}
     */
    public Administrador(String nombre, String apellido, String username, String email, String passwordHash) {
        // PENDIENTE
    }

    /**
     * Permisos de administrador.
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos de administrador.
     */
    public Set<Permiso> obtenerPermisos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve falso: los administradores siempre inician sesión con usuario y contraseña.
     *
     * <p>Casos de uso: todos
     * @return false.
     */
    public boolean admiteSesionPersistente() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve "Administrador".
     *
     * <p>Casos de uso: todos
     * @return "Administrador".
     */
    public String tipoCuenta() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param username valor de {@code username}
     * @param email valor de {@code email}
     * @param passwordHash valor de {@code passwordHash}
     * @param estado valor de {@code estado}
     * @param intentosFallidos valor de {@code intentosFallidos}
     * @param bloqueadoHasta valor de {@code bloqueadoHasta}
     * @param fechaCreacion valor de {@code fechaCreacion}
     * @param nombre valor de {@code nombre}
     * @param apellido valor de {@code apellido}
     * @return el resultado
     */
    public static Administrador reconstituir(Long id, String username, String email, String passwordHash, EstadoCuenta estado, Integer intentosFallidos, LocalDateTime bloqueadoHasta, LocalDateTime fechaCreacion, String nombre, String apellido) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
