package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenSesionPersistente;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker concreto de {@code TokenSesionPersistente} (tabla {@code token_sesion_persistente}). Database Broker
 * (Larman, cap. 34): completa los ganchos del Template Method de {@code BrokerBaseDatos}; su particularidad es la
 * clave natural {@code serie} ({@code columnaClave()} y {@code asignarClave} redefinidos, 07C).
 *
 * <p>Casos de uso: CU-11, CU-12, CU-13, CU-15, CU-17
 */
@Component
public class BrokerTokenSesionPersistente extends BrokerBaseDatos<TokenSesionPersistente> {

    /** Id de la cuenta leído en {@code crearObjeto}, pendiente de resolver en {@code cargarAsociaciones}. */
    private final Map<TokenSesionPersistente, Long> usuariosPendientes =
            Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-11
     * @param proveedor proveedor de conexiones
     */
    public BrokerTokenSesionPersistente(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * La clave de la tabla es la serie.
     *
     * <p>Casos de uso: CU-11
     * @return {@code "serie"}
     */
    @Override
    protected String columnaClave() {
        return "serie";
    }

    /**
     * Tras el INSERT la serie ya la tenía el objeto: solo lo marca persistido ({@code token.marcarPersistido()}),
     * así el próximo {@code put} es un UPDATE.
     *
     * <p>Casos de uso: CU-11
     * @param t token insertado
     * @param valor serie devuelta por {@code RETURNING}
     */
    @Override
    protected void asignarClave(TokenSesionPersistente t, Object valor) {
        t.marcarPersistido();
    }

    /**
     * Resuelve la cuenta dueña con {@code fachada().get(usuarioId, Usuario)}.
     *
     * <p>Casos de uso: CU-11
     * @param t token recién creado
     */
    @Override
    protected void cargarAsociaciones(TokenSesionPersistente t) {
        Long idUsuario = usuariosPendientes.remove(t);
        if (idUsuario != null) {
            t.restaurarAsociaciones(fachada().get(idUsuario, Usuario.class));
        }
    }

    /**
     * Clase que maneja.
     *
     * <p>Casos de uso: CU-11
     * @return {@code TokenSesionPersistente.class}
     */
    @Override
    public Class<TokenSesionPersistente> claseObjeto() {
        return TokenSesionPersistente.class;
    }

    /**
     * Tabla: {@code token_sesion_persistente}.
     *
     * <p>Casos de uso: CU-11
     * @return nombre de la tabla
     */
    @Override
    protected String tabla() {
        return "token_sesion_persistente";
    }

    /**
     * Desmaterializa con {@code TokenSesionPersistente.reconstituir(...)} y guarda el {@code usuario_id}.
     *
     * <p>Casos de uso: CU-11
     * @param fila fila del resultado
     * @return token sin asociaciones
     */
    @Override
    protected TokenSesionPersistente crearObjeto(FilaResultado fila) {
        TokenSesionPersistente t = TokenSesionPersistente.reconstituir(fila.texto("serie"), fila.texto("token_hash"),
                fila.fechaHora("ultimo_uso"), fila.fechaHora("expira_en"));
        usuariosPendientes.put(t, fila.largo("usuario_id"));
        return t;
    }

    /**
     * Columnas del INSERT (incluye la serie, que es la clave).
     *
     * <p>Casos de uso: CU-11
     * @param objeto token nuevo
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(TokenSesionPersistente objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("serie", objeto.getSerie());
        valores.put("usuario_id", objeto.getUsuario());
        valores.put("token_hash", objeto.getTokenHash());
        valores.put("ultimo_uso", objeto.getUltimoUso());
        valores.put("expira_en", objeto.getExpiraEn());
        return valores;
    }

    /**
     * Columnas del UPDATE de la rotación: hash, último uso y vencimiento.
     *
     * <p>Casos de uso: CU-11
     * @param objeto token persistido
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(TokenSesionPersistente objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("token_hash", objeto.getTokenHash());
        valores.put("ultimo_uso", objeto.getUltimoUso());
        valores.put("expira_en", objeto.getExpiraEn());
        return valores;
    }
}
