package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.accesos.persistencia.TokenSesionPorSerie;
import ar.edu.ub.club.accesos.persistencia.TokensSesionDeUsuario;
import ar.edu.ub.club.accesos.persistencia.TokensSesionVencidos;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador (patrón Adapter, accesos.persistencia) que implementa la interfaz {@code PersistentTokenRepository}
 * de Spring Security ({@code createNewToken}, {@code updateToken}, {@code getTokenForSeries}, {@code
 * removeUserTokens}) con la persistencia del sistema. Guarda siempre solo el hash del token. Detalle: documento
 * 07C.
 *
 * <p>Correspondencia con Spring Security: el «username» de {@code PersistentRememberMeToken} es el id de la cuenta
 * en texto (el nombre del principal, ver {@code GestorSesiones}) y su {@code tokenValue} es el hash guardado;
 * {@code ServicioRecordarmeClub} aplica el mismo hash al token de la cookie antes de compararlos. Cada método
 * público es una transacción (los llama Spring Security fuera de las fachadas; dentro de una fachada se unen a su
 * transacción).
 *
 * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
 */
@Component
@Transactional
public class AlmacenTokensSesionPersistente implements PersistentTokenRepository {

    /** Acceso a {@code TokenSesionPersistente}. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea el adaptador.
     *
     * <p>Casos de uso: CU-11
     * @param fachadaPersistencia fachada de persistencia
     */
    public AlmacenTokensSesionPersistente(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * {@code fachadaPersistencia.buscarUno(TokenSesionPorSerie(serie))}.
     *
     * <p>Casos de uso: CU-11
     * @param serie serie leída de la cookie
     * @return el token o vacío
     */
    public Optional<TokenSesionPersistente> buscarPorSerie(String serie) {
        return fachadaPersistencia.buscarUno(new TokenSesionPorSerie(serie));
    }

    /**
     * Busca el token por serie, {@code token.rotar(tokenHash, ultimoUso)} y {@code fachadaPersistencia.put(token)}
     * (rotación en cada uso, {@code sec_cu11_p4.puml}).
     *
     * <p>Casos de uso: CU-11
     * @param serie serie del token
     * @param tokenHash hash del nuevo valor del token
     * @param ultimoUso momento del uso
     */
    public void actualizarToken(String serie, String tokenHash, LocalDateTime ultimoUso) {
        buscarPorSerie(serie).ifPresent(token -> {
            token.rotar(tokenHash, ultimoUso);
            fachadaPersistencia.put(token);
        });
    }

    /**
     * {@code fachadaPersistencia.buscar(TokensSesionDeUsuario(idUsuario))} y {@code eliminar} de cada uno; devuelve
     * la cantidad.
     *
     * <p>Casos de uso: CU-03, CU-11, CU-12, CU-13, CU-15, CU-17
     * @param idUsuario cuenta dueña
     * @return cantidad de tokens borrados
     */
    public int eliminarDeUsuario(Long idUsuario) {
        return eliminarTodos(fachadaPersistencia.buscar(new TokensSesionDeUsuario(idUsuario)));
    }

    /**
     * Igual con {@code TokensSesionVencidos(ahora)} (limpieza de mantenimiento).
     *
     * <p>Casos de uso: CU-11
     * @param ahora momento actual
     * @return cantidad de tokens borrados
     */
    public int eliminarVencidos(LocalDateTime ahora) {
        return eliminarTodos(fachadaPersistencia.buscar(new TokensSesionVencidos(ahora)));
    }

    /**
     * Operación de {@code PersistentTokenRepository}: crea el token de una serie nueva guardando el hash del valor
     * (Spring Security la usa solo si el ingreso se hiciera por su propio formulario; en este sistema el token lo
     * emite {@code GestorSesiones.emitirTokenPersistente}).
     *
     * <p>Casos de uso: CU-11
     * @param t token de Spring Security (username = id de la cuenta)
     */
    @Override
    public void createNewToken(PersistentRememberMeToken t) {
        Usuario usuario = fachadaPersistencia.get(Long.valueOf(t.getUsername()), Usuario.class);
        fachadaPersistencia.put(new TokenSesionPersistente(t.getSeries(), usuario,
                GestorSesiones.hashSha256(t.getTokenValue()), aLocal(t.getDate())));
    }

    /**
     * Operación de {@code PersistentTokenRepository}: rotación del token tras un ingreso por remember-me;
     * delega en {@code actualizarToken(serie, hash(valor), fecha)}.
     *
     * <p>Casos de uso: CU-11
     * @param serie serie
     * @param valor nuevo valor del token (en claro: se guarda su hash)
     * @param fecha momento del uso
     */
    @Override
    public void updateToken(String serie, String valor, java.util.Date fecha) {
        actualizarToken(serie, GestorSesiones.hashSha256(valor), aLocal(fecha));
    }

    /**
     * Operación de {@code PersistentTokenRepository}: delega en {@code buscarPorSerie} y adapta el resultado
     * (username = id de la cuenta, tokenValue = hash guardado, fecha = último uso).
     *
     * <p>Casos de uso: CU-11
     * @param serie serie de la cookie
     * @return token de Spring Security o {@code null} si la serie no existe
     */
    @Override
    @Transactional(readOnly = true)
    public PersistentRememberMeToken getTokenForSeries(String serie) {
        return buscarPorSerie(serie)
                .map(t -> new PersistentRememberMeToken(String.valueOf(t.getUsuario().getId()), t.getSerie(),
                        t.getTokenHash(), Date.from(t.getUltimoUso().atZone(RelojSistema.ZONA_CLUB).toInstant())))
                .orElse(null);
    }

    /**
     * Operación de {@code PersistentTokenRepository}: revoca todas las series de la cuenta (posible robo del token,
     * CU-11 FA2); delega en {@code eliminarDeUsuario}.
     *
     * <p>Casos de uso: CU-11
     * @param username nombre del principal (id de la cuenta en texto)
     */
    @Override
    public void removeUserTokens(String username) {
        try {
            eliminarDeUsuario(Long.valueOf(username));
        } catch (NumberFormatException e) {
            // no es un principal de este sistema: no hay tokens que borrar
        }
    }

    /**
     * Borra cada token de la lista con {@code fachadaPersistencia.eliminar}.
     *
     * <p>Casos de uso: CU-11
     * @param tokens tokens a borrar
     * @return cantidad
     */
    private int eliminarTodos(List<TokenSesionPersistente> tokens) {
        for (TokenSesionPersistente t : tokens) {
            fachadaPersistencia.eliminar(t);
        }
        return tokens.size();
    }

    /**
     * Convierte una fecha de Spring Security a la hora local del club.
     *
     * <p>Casos de uso: CU-11
     * @param fecha fecha {@code java.util.Date}
     * @return fecha y hora local
     */
    private static LocalDateTime aLocal(java.util.Date fecha) {
        return LocalDateTime.ofInstant(fecha.toInstant(), RelojSistema.ZONA_CLUB);
    }
}
