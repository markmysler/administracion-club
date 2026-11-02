package ar.edu.ub.club.accesos.persistencia;

import ar.edu.ub.club.accesos.dominio.TokenRecuperacion;
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
 * Broker concreto de {@code TokenRecuperacion} (tabla {@code token_recuperacion}). Database Broker (Larman, cap.
 * 34): completa los ganchos del Template Method de {@code BrokerBaseDatos}. La asociación a uno {@code usuario}
 * se resuelve con {@code fachada().get(id, Usuario)} (polimórfica: Socio o Administrador).
 *
 * <p>Casos de uso: CU-13
 */
@Component
public class BrokerTokenRecuperacion extends BrokerBaseDatos<TokenRecuperacion> {

    /** Id de la cuenta leído en {@code crearObjeto}, pendiente de resolver en {@code cargarAsociaciones}. */
    private final Map<TokenRecuperacion, Long> usuariosPendientes =
            Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-13
     * @param proveedor proveedor de conexiones
     */
    public BrokerTokenRecuperacion(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve la cuenta dueña con {@code fachada().get(usuarioId, Usuario)} y la entrega con
     * {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: CU-13
     * @param t token recién creado
     */
    @Override
    protected void cargarAsociaciones(TokenRecuperacion t) {
        Long idUsuario = usuariosPendientes.remove(t);
        if (idUsuario != null) {
            t.restaurarAsociaciones(fachada().get(idUsuario, Usuario.class));
        }
    }

    /**
     * Clase que maneja.
     *
     * <p>Casos de uso: CU-13
     * @return {@code TokenRecuperacion.class}
     */
    @Override
    public Class<TokenRecuperacion> claseObjeto() {
        return TokenRecuperacion.class;
    }

    /**
     * Tabla: {@code token_recuperacion}.
     *
     * <p>Casos de uso: CU-13
     * @return nombre de la tabla
     */
    @Override
    protected String tabla() {
        return "token_recuperacion";
    }

    /**
     * Desmaterializa con {@code TokenRecuperacion.reconstituir(...)} y guarda el {@code usuario_id}.
     *
     * <p>Casos de uso: CU-13
     * @param fila fila del resultado
     * @return token sin asociaciones
     */
    @Override
    protected TokenRecuperacion crearObjeto(FilaResultado fila) {
        TokenRecuperacion t = TokenRecuperacion.reconstituir(fila.largo("id"), fila.texto("token_hash"),
                fila.fechaHora("expira_en"), fila.booleano("usado"));
        usuariosPendientes.put(t, fila.largo("usuario_id"));
        return t;
    }

    /**
     * Columnas del INSERT.
     *
     * <p>Casos de uso: CU-13
     * @param objeto token nuevo
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(TokenRecuperacion objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("usuario_id", objeto.getUsuario());
        valores.put("token_hash", objeto.getTokenHash());
        valores.put("expira_en", objeto.getExpiraEn());
        valores.put("usado", objeto.getUsado());
        return valores;
    }

    /**
     * Columnas del UPDATE (solo cambia {@code usado}).
     *
     * <p>Casos de uso: CU-13
     * @param objeto token persistido
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(TokenRecuperacion objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("usado", objeto.getUsado());
        return valores;
    }
}
