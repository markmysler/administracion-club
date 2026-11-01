package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.compartido.seguridad.Autorizable;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.time.LocalDateTime;
import java.util.Set;

/**
 * Clase abstracta que representa una cuenta de acceso; se especializa en {@code Socio} y {@code Administrador} e
 * implementa la interfaz {@code Autorizable}. Es Information Expert de sus intentos fallidos, de su bloqueo y de
 * sus datos de acceso.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public abstract class Usuario extends EntidadPersistente implements Autorizable {

    /** Nombre de usuario. */
    private String username;

    /** Correo. */
    private String email;

    /** Hash BCrypt. */
    private String passwordHash;

    /** Estado de la cuenta. */
    private EstadoCuenta estado;

    /** Intentos fallidos consecutivos. */
    private Integer intentosFallidos;

    /** Fin del bloqueo temporal. */
    private LocalDateTime bloqueadoHasta;

    /** Alta de la cuenta. */
    private LocalDateTime fechaCreacion;

    /**
     * Verifica la clave con {@code codificador.coincide(clave, passwordHash)}.
     *
     * <p>Casos de uso: CU-11
     * @param clave Clave ingresada.
     * @param codificador Codificador BCrypt.
     * @return true si coincide.
     */
    public boolean autenticar(String clave, CodificadorClave codificador) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Incrementa los intentos; al quinto fija {@code bloqueadoHasta = ahora + 15 minutos} (PoliticaBloqueo).
     *
     * <p>Casos de uso: CU-11
     * @param ahora Momento actual.
     */
    public void registrarIntentoFallido(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si {@code bloqueadoHasta} es posterior a ahora.
     *
     * <p>Casos de uso: CU-11
     * @param ahora Momento actual.
     * @return true si está bloqueada.
     */
    public boolean estaBloqueada(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Pone los intentos en 0 y borra el bloqueo.
     *
     * <p>Casos de uso: CU-11, CU-13
     */
    public void reiniciarIntentos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si el estado es ACTIVA.
     *
     * <p>Casos de uso: CU-11, CU-15
     * @return true si está activa.
     */
    public boolean estaActiva() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Reemplaza el hash de la contraseña (CU-13, CU-15).
     *
     * <p>Casos de uso: CU-13, CU-15
     * @param hash Hash ya calculado por {@code CodificadorClave}.
     */
    public void cambiarClave(String hash) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Modifica usuario y correo (CU-15); la unicidad la verifica la fachada.
     *
     * <p>Casos de uso: CU-15
     * @param username Nuevo usuario.
     * @param email Nuevo correo.
     */
    public void actualizarDatosAcceso(String username, String email) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Pasa la cuenta a INACTIVA (final).
     *
     * <p>Casos de uso: todos
     */
    public void desactivar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Operación abstracta de {@code Autorizable}; cada subclase devuelve sus permisos.
     *
     * <p>Casos de uso: todos
     * @return Conjunto de permisos.
     */
    public abstract Set<Permiso> obtenerPermisos();

    /**
     * Operación abstracta: {@code Socio} devuelve verdadero y {@code Administrador} falso. Decide, sin preguntar por
     * el tipo, si se emite el token de «Mantener la sesión iniciada».
     *
     * <p>Casos de uso: CU-11
     * @return true si la cuenta admite sesión persistente.
     */
    public abstract boolean admiteSesionPersistente();

    /**
     * Operación abstracta: apellido y nombre de la cuenta ({@code Socio} y {@code Administrador} tienen sus propios
     * {@code nombre} y {@code apellido}). La usan {@code DetalleCuenta} y {@code FilaCuenta} sin preguntar por la
     * clase.
     *
     * <p>Casos de uso: todos
     * @return Apellido y nombre.
     */
    public String nombreCompleto() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Operación abstracta: {@code Socio} devuelve "Socio" y {@code Administrador} "Administrador". La usan los
     * modelos de vista {@code FilaCuenta} y {@code DetalleCuenta} para mostrar el tipo de cuenta sin preguntar por
     * la clase.
     *
     * <p>Casos de uso: todos
     * @return Tipo de cuenta para mostrar.
     */
    public String tipoCuenta() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code username}: Nombre de usuario.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve {@code email}: Correo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve {@code passwordHash}: Hash BCrypt.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Devuelve {@code estado}: Estado de la cuenta.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code intentosFallidos}: Intentos fallidos consecutivos.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Integer getIntentosFallidos() {
        return intentosFallidos;
    }

    /**
     * Devuelve {@code bloqueadoHasta}: Fin del bloqueo temporal.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getBloqueadoHasta() {
        return bloqueadoHasta;
    }

    /**
     * Devuelve {@code fechaCreacion}: Alta de la cuenta.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }
}
