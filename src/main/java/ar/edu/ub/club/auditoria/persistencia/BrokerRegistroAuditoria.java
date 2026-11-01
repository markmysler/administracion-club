package ar.edu.ub.club.auditoria.persistencia;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.dominio.RegistroAuditoria;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Broker concreto de {@code RegistroAuditoria} (tabla {@code registro_auditoria}). Database Broker (Larman, cap.
 * 34): completa los ganchos del Template Method de {@code BrokerBaseDatos}. Los registros solo se insertan (no
 * hay UPDATE); el detalle se guarda como JSONB pasando el texto JSON.
 *
 * <p>Casos de uso: todos
 */
@Component
public class BrokerRegistroAuditoria extends BrokerBaseDatos<RegistroAuditoria> {

    /** Id de la cuenta leído en {@code crearObjeto}, pendiente de resolver en {@code cargarAsociaciones}. */
    private final Map<RegistroAuditoria, Long> usuariosPendientes =
            Collections.synchronizedMap(new IdentityHashMap<>());

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    public BrokerRegistroAuditoria(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Resuelve la cuenta que actuó con {@code fachada().get(id, Usuario)} (si la hay).
     *
     * <p>Casos de uso: todos
     * @param r registro recién creado
     */
    @Override
    protected void cargarAsociaciones(RegistroAuditoria r) {
        Long idUsuario = usuariosPendientes.remove(r);
        r.restaurarAsociaciones(idUsuario == null ? null : fachada().get(idUsuario, Usuario.class));
    }

    /**
     * Clase que maneja.
     *
     * <p>Casos de uso: todos
     * @return {@code RegistroAuditoria.class}
     */
    @Override
    public Class<RegistroAuditoria> claseObjeto() {
        return RegistroAuditoria.class;
    }

    /**
     * Tabla: {@code registro_auditoria}.
     *
     * <p>Casos de uso: todos
     * @return nombre de la tabla
     */
    @Override
    protected String tabla() {
        return "registro_auditoria";
    }

    /**
     * Desmaterializa con {@code RegistroAuditoria.reconstituir(...)} y guarda el {@code usuario_id}.
     *
     * <p>Casos de uso: todos
     * @param fila fila del resultado
     * @return registro sin asociaciones
     */
    @Override
    protected RegistroAuditoria crearObjeto(FilaResultado fila) {
        RegistroAuditoria r = RegistroAuditoria.reconstituir(fila.largo("id"), fila.fechaHora("fecha_hora"),
                fila.texto("accion"), fila.texto("entidad"), fila.largo("entidad_id"), fila.texto("detalle"));
        Long idUsuario = fila.largo("usuario_id");
        if (idUsuario != null) {
            usuariosPendientes.put(r, idUsuario);
        }
        return r;
    }

    /**
     * Columnas del INSERT.
     *
     * <p>Casos de uso: todos
     * @param objeto registro nuevo
     * @return mapa ordenado columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(RegistroAuditoria objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("fecha_hora", objeto.getFechaHora());
        valores.put("usuario_id", objeto.getUsuario());
        valores.put("accion", objeto.getAccion());
        valores.put("entidad", objeto.getEntidad());
        valores.put("entidad_id", objeto.getIdEntidad());
        valores.put("detalle", objeto.getDetalle());
        return valores;
    }

    /**
     * Sin columnas: los registros de auditoría no se modifican.
     *
     * <p>Casos de uso: todos
     * @param objeto registro persistido
     * @return mapa vacío
     */
    @Override
    protected Map<String, Object> valoresActualizacion(RegistroAuditoria objeto) {
        return new LinkedHashMap<>();
    }
}
