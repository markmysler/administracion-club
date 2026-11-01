package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.ResumenCuenta;
import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import java.time.LocalDateTime;

/**
 * Fila de la tabla de cuentas de P-13: administradores (listado inicial) o cuentas de cualquier tipo (búsqueda,
 * columna Tipo de cuenta).
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador; la vista solo lo muestra.
 *
 * <p>Casos de uso: CU-14, CU-15, CU-17
 *
 * @param id identificador de la cuenta
 * @param username nombre de usuario
 * @param nombreCompleto apellido y nombre
 * @param email correo
 * @param tipoCuenta "Socio" o "Administrador"
 * @param estado "Activa" o "Inactiva"
 * @param bloqueada si está bloqueada temporalmente por intentos fallidos
 */
public record FilaCuenta(Long id, String username, String nombreCompleto, String email, String tipoCuenta, String estado, boolean bloqueada) {

    /**
     * Fila de un administrador del listado inicial.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param administrador cuenta de administrador
     * @param ahora momento actual (para calcular el bloqueo)
     * @return fila
     */
    public static FilaCuenta desde(Administrador administrador, LocalDateTime ahora) {
        return new FilaCuenta(administrador.getId(), administrador.getUsername(), administrador.nombreCompleto(),
                administrador.getEmail(), administrador.tipoCuenta(), textoEstado(administrador.getEstado()),
                administrador.estaBloqueada(ahora));
    }

    /**
     * Fila de una cuenta de cualquier tipo encontrada por la búsqueda.
     *
     * <p>Casos de uso: CU-15, CU-17
     * @param resumen resumen de la cuenta
     * @return fila
     */
    public static FilaCuenta desde(ResumenCuenta resumen) {
        return new FilaCuenta(resumen.getIdUsuario(), resumen.getUsername(), resumen.getNombreCompleto(),
                resumen.getEmail(), resumen.getTipoCuenta(), textoEstado(resumen.getEstado()), resumen.getBloqueada());
    }

    /**
     * Texto para mostrar del estado de una cuenta.
     *
     * <p>Casos de uso: CU-14, CU-15, CU-17
     * @param estado estado
     * @return "Activa" o "Inactiva"
     */
    static String textoEstado(EstadoCuenta estado) {
        return estado == EstadoCuenta.ACTIVA ? "Activa" : "Inactiva";
    }
}
