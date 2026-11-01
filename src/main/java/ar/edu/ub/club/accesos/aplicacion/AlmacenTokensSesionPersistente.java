package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

/**
 * Adaptador (patrón Adapter, accesos.persistencia) que implementa la interfaz {@code PersistentTokenRepository}
 * de Spring Security ({@code createNewToken}, {@code updateToken}, {@code getTokenForSeries}, {@code
 * removeUserTokens}) con la persistencia del sistema. Guarda siempre solo el hash del token. Detalle: documento
 * 07C.
 *
 * <p>Casos de uso: CU-11
 */
public class AlmacenTokensSesionPersistente implements PersistentTokenRepository {

    /** Acceso a {@code TokenSesionPersistente}. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * {@code fachadaPersistencia.buscarUno(TokenSesionPorSerie(serie))}.
     *
     * <p>Casos de uso: CU-11
     * @param serie valor de {@code serie}
     * @return el resultado
     */
    public Optional<TokenSesionPersistente> buscarPorSerie(String serie) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Busca el token por serie, {@code token.rotar(tokenHash, ultimoUso)} y {@code fachadaPersistencia.put(token)}
     * (rotación en cada uso).
     *
     * <p>Casos de uso: CU-11
     * @param serie valor de {@code serie}
     * @param tokenHash valor de {@code tokenHash}
     * @param ultimoUso valor de {@code ultimoUso}
     */
    public void actualizarToken(String serie, String tokenHash, LocalDateTime ultimoUso) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code fachadaPersistencia.buscar(TokensSesionDeUsuario(idUsuario))} y {@code eliminar} de cada uno; devuelve
     * la cantidad.
     *
     * <p>Casos de uso: CU-11
     * @param idUsuario valor de {@code idUsuario}
     * @return el resultado
     */
    public int eliminarDeUsuario(Long idUsuario) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Igual con {@code TokensSesionVencidos(ahora)}.
     *
     * <p>Casos de uso: CU-11
     * @param ahora valor de {@code ahora}
     * @return el resultado
     */
    public int eliminarVencidos(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     * @param t valor de {@code t}
     */
    public void createNewToken(PersistentRememberMeToken t) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     * @param serie valor de {@code serie}
     * @param valor valor de {@code valor}
     * @param fecha valor de {@code fecha}
     */
    public void updateToken(String serie, String valor, java.util.Date fecha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     * @param serie valor de {@code serie}
     * @return el resultado
     */
    public PersistentRememberMeToken getTokenForSeries(String serie) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-11
     * @param username valor de {@code username}
     */
    public void removeUserTokens(String username) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
