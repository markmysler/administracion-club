package ar.edu.ub.club.accesos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.Duration;
import java.time.LocalDateTime;

/**
 * Token de «Mantener la sesión iniciada» (remember-me con tokens persistentes y rotación de Spring Security), solo
 * para cuentas de socio ({@code Usuario.admiteSesionPersistente()}). La cookie lleva la serie y el token; aquí se
 * guarda solo el hash SHA-256 del token. Su clave es la serie (no un id generado), por eso redefine
 * {@code esNuevo()} con el indicador {@code persistido}.
 *
 * <p>Entidad del dominio; la persiste {@code BrokerTokenSesionPersistente} ({@code columnaClave() = "serie"}).
 *
 * <p>Casos de uso: CU-11, CU-12, CU-13, CU-15, CU-17
 */
public class TokenSesionPersistente extends EntidadPersistente {

    /** Vigencia del token desde su último uso (30 días, 07C). */
    public static final Duration VIGENCIA = Duration.ofDays(30);

    /** Identificador aleatorio de la serie (clave primaria). */
    private String serie;

    /** Cuenta dueña del token. */
    private Usuario usuario;

    /** Hash SHA-256 del valor vigente del token. */
    private String tokenHash;

    /** Momento de la última rotación. */
    private LocalDateTime ultimoUso;

    /** Vencimiento: 30 días desde el último uso. */
    private LocalDateTime expiraEn;

    /** Indica si la fila ya existe en la base (la serie no es generada por la base). */
    private boolean persistido;

    /**
     * Crea un token nuevo, todavía no persistido (CU-11 paso 10, {@code GestorSesiones.emitirTokenPersistente}).
     *
     * <p>Agregado en la implementación: el diseño solo definía {@code reconstituir}.
     *
     * <p>Casos de uso: CU-11
     * @param serie serie aleatoria
     * @param usuario cuenta de socio
     * @param tokenHash hash SHA-256 del token
     * @param ahora momento de emisión (último uso); vence a los 30 días
     */
    public TokenSesionPersistente(String serie, Usuario usuario, String tokenHash, LocalDateTime ahora) {
        this.serie = serie;
        this.usuario = usuario;
        this.tokenHash = tokenHash;
        this.ultimoUso = ahora;
        this.expiraEn = ahora.plus(VIGENCIA);
        this.persistido = false;
    }

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-11
     */
    private TokenSesionPersistente() {
        // lo completa reconstituir
    }

    /**
     * Rotación en cada uso: reemplaza el hash y extiende {@code ultimoUso} y {@code expiraEn} (07C, ciclo de vida;
     * {@code sec_cu11_p4.puml} mensaje 1.2).
     *
     * <p>Agregado en la implementación: lo nombran 07C y el diagrama, pero no estaba en el modelo de clases.
     *
     * <p>Casos de uso: CU-11
     * @param nuevoHash hash del nuevo valor del token
     * @param ahora momento del uso
     */
    public void rotar(String nuevoHash, LocalDateTime ahora) {
        this.tokenHash = nuevoHash;
        this.ultimoUso = ahora;
        this.expiraEn = ahora.plus(VIGENCIA);
    }

    /**
     * Indica que la fila ya existe en la base (lo llama {@code BrokerTokenSesionPersistente.asignarClave} tras el
     * INSERT, 07C).
     *
     * <p>Agregado en la implementación: lo nombra 07C, no estaba en el modelo de clases.
     *
     * <p>Casos de uso: CU-11
     */
    public void marcarPersistido() {
        this.persistido = true;
    }

    /**
     * Un token es nuevo mientras no se insertó (su clave es la serie, que ya existe antes del INSERT).
     *
     * <p>Casos de uso: CU-11
     * @return true si todavía no está en la base
     */
    @Override
    public boolean esNuevo() {
        return !persistido;
    }

    /**
     * Reconstituye el token desde la fila de {@code token_sesion_persistente} (ya persistido; la cuenta llega
     * luego por {@code restaurarAsociaciones}).
     *
     * <p>Casos de uso: CU-11, CU-12, CU-13, CU-15, CU-17
     * @param serie serie (clave)
     * @param tokenHash hash del token vigente
     * @param ultimoUso último uso
     * @param expiraEn vencimiento
     * @return el token reconstituido
     */
    public static TokenSesionPersistente reconstituir(String serie, String tokenHash, LocalDateTime ultimoUso, LocalDateTime expiraEn) {
        TokenSesionPersistente t = new TokenSesionPersistente();
        t.serie = serie;
        t.tokenHash = tokenHash;
        t.ultimoUso = ultimoUso;
        t.expiraEn = expiraEn;
        t.persistido = true;
        return t;
    }

    /**
     * Entrega la cuenta dueña (la resuelve {@code BrokerTokenSesionPersistente.cargarAsociaciones}).
     *
     * <p>Casos de uso: CU-11, CU-12, CU-13, CU-15, CU-17
     * @param usuario cuenta dueña
     */
    public void restaurarAsociaciones(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Devuelve {@code serie}: identificador aleatorio de la serie.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public String getSerie() {
        return serie;
    }

    /**
     * Devuelve {@code usuario}: cuenta dueña del token.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Devuelve {@code tokenHash}: hash SHA-256 del token vigente.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public String getTokenHash() {
        return tokenHash;
    }

    /**
     * Devuelve {@code ultimoUso}: momento de la última rotación.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public LocalDateTime getUltimoUso() {
        return ultimoUso;
    }

    /**
     * Devuelve {@code expiraEn}: vencimiento.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public LocalDateTime getExpiraEn() {
        return expiraEn;
    }

    /**
     * Devuelve {@code persistido}: si la fila ya existe en la base.
     *
     * <p>Casos de uso: CU-11
     * @return el valor del atributo
     */
    public boolean getPersistido() {
        return persistido;
    }
}
