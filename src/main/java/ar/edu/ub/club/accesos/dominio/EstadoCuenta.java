package ar.edu.ub.club.accesos.dominio;

/**
 * Estado persistido de una cuenta de acceso ({@code usuario.estado}). El bloqueo temporal no es un valor de este
 * enumerado: se deriva de {@code bloqueadoHasta} ({@code Usuario.estaBloqueada}). INACTIVA es final
 * ({@code est_cuenta.puml}).
 *
 * <p>Casos de uso: CU-03, CU-11, CU-15, CU-17
 */
public enum EstadoCuenta {

    /** Cuenta habilitada para iniciar sesión. */
    ACTIVA,
    /** Cuenta dada de baja (estado final; conserva el historial). */
    INACTIVA;
}
