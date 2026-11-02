package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.accesos.dominio.Usuario;
import java.time.LocalDateTime;

/**
 * Resumen de una cuenta de cualquier tipo que devuelve {@code ServicioAccesos.buscarCuentas} (P-13). El tipo de
 * cuenta y el nombre se obtienen por polimorfismo ({@code tipoCuenta()}, {@code nombreCompleto()}), sin preguntar
 * por la clase.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-15, CU-17
 */
public class ResumenCuenta {

    /** Identificador de la cuenta. */
    private Long idUsuario;

    /** Nombre de usuario. */
    private String username;

    /** Apellido y nombre. */
    private String nombreCompleto;

    /** Correo. */
    private String email;

    /** "Socio" o "Administrador". */
    private String tipoCuenta;

    /** Estado de la cuenta. */
    private EstadoCuenta estado;

    /** Si está bloqueada temporalmente por intentos fallidos. */
    private boolean bloqueada;

    /**
     * Crea el resumen con todos sus valores.
     *
     * <p>Agregado en la implementación: el esqueleto no tenía constructor.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param idUsuario identificador
     * @param username nombre de usuario
     * @param nombreCompleto apellido y nombre
     * @param email correo
     * @param tipoCuenta tipo de cuenta
     * @param estado estado
     * @param bloqueada si está bloqueada
     */
    public ResumenCuenta(Long idUsuario, String username, String nombreCompleto, String email, String tipoCuenta,
                         EstadoCuenta estado, boolean bloqueada) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.nombreCompleto = nombreCompleto;
        this.email = email;
        this.tipoCuenta = tipoCuenta;
        this.estado = estado;
        this.bloqueada = bloqueada;
    }

    /**
     * Arma el resumen de una cuenta (polimorfismo de {@code tipoCuenta} y {@code nombreCompleto}).
     *
     * <p>Agregado en la implementación.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param usuario cuenta de cualquier tipo
     * @param ahora momento actual (para el bloqueo)
     * @return resumen
     */
    public static ResumenCuenta desde(Usuario usuario, LocalDateTime ahora) {
        return new ResumenCuenta(usuario.getId(), usuario.getUsername(), usuario.nombreCompleto(), usuario.getEmail(),
                usuario.tipoCuenta(), usuario.getEstado(), usuario.estaBloqueada(ahora));
    }

    /**
     * Devuelve {@code idUsuario}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Devuelve {@code username}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve {@code nombreCompleto}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Devuelve {@code email}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve {@code tipoCuenta}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * Devuelve {@code estado}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code bloqueada}.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @return el valor del atributo
     */
    public boolean getBloqueada() {
        return bloqueada;
    }
}
