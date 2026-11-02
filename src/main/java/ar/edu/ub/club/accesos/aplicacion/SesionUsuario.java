package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.util.Collections;
import java.util.EnumSet;
import java.util.Set;

/**
 * Resultado de {@code ServicioAccesos.iniciarSesion} y de {@code consultarSesionActual}: identifica la cuenta
 * autenticada (id, nombre visible y permisos resueltos por polimorfismo) y, si el socio marcó «Mantener la sesión
 * iniciada», el valor de la cookie remember-me ({@code "serie:token"}). El presentador se la pasa a
 * {@code RegistroSesionWeb.iniciar}, que arma con ella la sesión del servidor.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-11, CU-12
 */
public class SesionUsuario {

    /** Identificador de la cuenta autenticada (es también el nombre del principal de Spring Security). */
    private Long idUsuario;

    /** Apellido y nombre de la cuenta ({@code Usuario.nombreCompleto()}). */
    private String nombreVisible;

    /** Permisos de la cuenta ({@code Usuario.obtenerPermisos()}), inmutables. */
    private Set<Permiso> permisos;

    /** Valor de la cookie de sesión persistente ({@code "serie:token"}) o {@code null} si no se emitió. */
    private String tokenPersistente;

    /**
     * Crea la sesión sin token persistente (CU-11 paso 9, «create» en {@code sec_cu11_p3.puml}).
     *
     * <p>Casos de uso: CU-11, CU-12
     * @param idUsuario identificador de la cuenta
     * @param nombreVisible apellido y nombre
     * @param permisos permisos de la cuenta
     */
    public SesionUsuario(Long idUsuario, String nombreVisible, Set<Permiso> permisos) {
        this.idUsuario = idUsuario;
        this.nombreVisible = nombreVisible;
        this.permisos = permisos == null || permisos.isEmpty()
                ? Collections.emptySet() : Collections.unmodifiableSet(EnumSet.copyOf(permisos));
    }

    /**
     * Agrega el valor de la cookie del token persistente (CU-11 paso 10: «agrega el token persistente»).
     *
     * <p>Agregado en la implementación: el diseño no nombraba la operación del mensaje.
     *
     * <p>Casos de uso: CU-11
     * @param valorCookie valor {@code "serie:token"} que devolvió {@code GestorSesiones.emitirTokenPersistente}
     */
    public void agregarTokenPersistente(String valorCookie) {
        this.tokenPersistente = valorCookie;
    }

    /**
     * Devuelve {@code idUsuario}: identificador de la cuenta.
     *
     * <p>Casos de uso: CU-11, CU-12
     * @return el valor del atributo
     */
    public Long getIdUsuario() {
        return idUsuario;
    }

    /**
     * Devuelve {@code nombreVisible}: apellido y nombre.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public String getNombreVisible() {
        return nombreVisible;
    }

    /**
     * Devuelve {@code permisos}: permisos de la cuenta.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public Set<Permiso> getPermisos() {
        return permisos;
    }

    /**
     * Devuelve {@code tokenPersistente}: valor de la cookie remember-me o {@code null}.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public String getTokenPersistente() {
        return tokenPersistente;
    }
}
