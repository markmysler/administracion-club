package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ResumenCuenta {

    /** Atributo idUsuario. */
    private Long idUsuario;

    /** Atributo username. */
    private String username;

    /** Atributo nombreCompleto. */
    private String nombreCompleto;

    /** Atributo email. */
    private String email;

    /** Atributo tipoCuenta. */
    private String tipoCuenta;

    /** Atributo estado. */
    private EstadoCuenta estado;

    /** Atributo bloqueada. */
    private boolean bloqueada;

    /**
     * Devuelve {@code idUsuario}: idUsuario.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Devuelve {@code username}: username.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getUsername() {
        return username;
    }

    /**
     * Devuelve {@code nombreCompleto}: nombreCompleto.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getNombreCompleto() {
        return nombreCompleto;
    }

    /**
     * Devuelve {@code email}: email.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getEmail() {
        return email;
    }

    /**
     * Devuelve {@code tipoCuenta}: tipoCuenta.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTipoCuenta() {
        return tipoCuenta;
    }

    /**
     * Devuelve {@code estado}: estado.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code bloqueada}: bloqueada.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public boolean getBloqueada() {
        return bloqueada;
    }
}
