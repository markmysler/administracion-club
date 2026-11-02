package ar.edu.ub.club.compartido.persistencia;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Intermediario de base de datos (Database Broker, Larman cap. 34) de una clase persistente: materializa y
 * desmaterializa sus objetos con SQL explícito sobre JDBC. Es el método plantilla (GoF Template Method): los pasos
 * fijos ({@code materializar}, {@code materializarTodos}, {@code contar}, {@code materializarDesdeFila},
 * {@code insertar}, {@code actualizar}, {@code borrar}) son {@code final} y llaman a los ganchos que completa cada
 * broker concreto ({@code tabla}, {@code crearObjeto}, {@code valoresInsercion}, {@code valoresActualizacion},
 * {@code cargarAsociaciones}, {@code guardarPartes}, ...). Pure Fabrication: no es un concepto del dominio.
 * Cada broker concreto es un {@code @Component} del paquete {@code persistencia} de su módulo; nadie fuera de
 * {@code compartido.persistencia} lo llama directamente (se usa {@code FachadaPersistencia}).
 *
 * <p>Convención de SQL: las columnas se califican con el nombre de la tabla (sin alias), así los objetos consulta
 * escriben condiciones como {@code WHERE reserva.fecha = ?}.
 *
 * @param <T> clase persistente que maneja
 *
 * <p>Casos de uso: todos
 */
public abstract class BrokerBaseDatos<T extends ObjetoPersistente> {

    /** Bitácora del SQL ejecutado (nivel DEBUG; útil para seguir un caso de uso). */
    private static final Logger BITACORA = LoggerFactory.getLogger(BrokerBaseDatos.class);

    /** SQLState de PostgreSQL para la violación de una restricción de exclusión. */
    private static final String VIOLACION_EXCLUSION = "23P01";

    /** Acceso a la conexión y al mapa de identidad de la transacción. */
    private final ProveedorConexiones proveedor;

    /** Fachada que registró al broker (la recibe por {@code vincular}, no por constructor, para evitar el ciclo). */
    private FachadaPersistencia fachada;

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: todos
     * @param proveedor proveedor de conexiones
     */
    protected BrokerBaseDatos(ProveedorConexiones proveedor) {
        this.proveedor = proveedor;
    }

    /**
     * Recibe la fachada que lo registró (lo llama el constructor de {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: todos
     * @param fachada fachada de persistencia
     */
    void vincular(FachadaPersistencia fachada) {
        this.fachada = fachada;
    }

    /**
     * Clase concreta que maneja el broker.
     *
     * <p>Casos de uso: todos
     * @return clase de los objetos
     */
    public abstract Class<T> claseObjeto();

    /**
     * Clase raíz de la jerarquía, clave del mapa de identidad (por omisión {@code claseObjeto()};
     * {@code BrokerUsuario} → {@code Usuario}, {@code BrokerPago} → {@code Pago}).
     *
     * <p>Casos de uso: todos
     * @return clase raíz
     */
    public Class<?> claseRaiz() {
        return claseObjeto();
    }

    /**
     * Materializa el objeto con esa clave: {@code consultaBase() + " WHERE <tabla>.<clave> = ?"}; si hay fila,
     * {@code materializarDesdeFila}.
     *
     * <p>Casos de uso: todos
     * @param id clave primaria
     * @return el objeto o {@code null} si no hay fila
     */
    public final T materializar(Long id) {
        String sql = consultaBase() + " WHERE " + tabla() + "." + columnaClave() + " = ?";
        List<T> filas = ejecutarConsulta(sql, List.of(id));
        return filas.isEmpty() ? null : filas.get(0);
    }

    /**
     * Materializa todos los objetos de una consulta: {@code consultaBase() + " " + consulta.sql()}; cada fila pasa
     * por {@code materializarDesdeFila}.
     *
     * <p>Casos de uso: todos
     * @param consulta objeto consulta de entidades
     * @return objetos materializados (instancias del mapa de identidad si ya estaban)
     */
    @SuppressWarnings("unchecked")
    public final List<T> materializarTodos(ConsultaEntidades<? super T> consulta) {
        return ejecutarConsulta(consultaBase() + " " + consulta.sql(), consulta.parametros());
    }

    /**
     * Cuenta los objetos de una consulta: {@code SELECT COUNT(*) FROM (consultaBase() + consulta.sql()) q}.
     *
     * <p>Casos de uso: todos
     * @param consulta objeto consulta de entidades
     * @return cantidad
     */
    public final long contar(ConsultaEntidades<? super T> consulta) {
        String sql = "SELECT COUNT(*) FROM (" + consultaBase() + " " + consulta.sql() + ") q";
        Connection conexion = proveedor.conexionActual();
        BITACORA.debug("SQL {} : {} {}", getClass().getSimpleName(), sql, consulta.parametros());
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            vincular(sentencia, consulta.parametros());
            try (ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            throw traducir(e);
        } finally {
            proveedor.liberar(conexion);
        }
    }

    /**
     * Materializa un objeto desde una fila: si el mapa de identidad ya tiene la clave devuelve ese objeto (no lo
     * pisa); si no, {@code crearObjeto(fila)}, lo registra en el mapa ANTES de cargar las asociaciones (así una
     * asociación circular, socio → grupo → titular, encuentra el objeto) y llama a {@code cargarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param fila fila del resultado (con o sin prefijo)
     * @return el objeto
     */
    public final T materializarDesdeFila(FilaResultado fila) {
        Object clave = fila.valor(columnaClave());
        MapaIdentidad mapa = mapa();
        ObjetoPersistente existente = mapa.buscar(claseRaiz(), clave);
        if (existente != null && claseObjeto().isInstance(existente)) {
            return claseObjeto().cast(existente);
        }
        T objeto = crearObjeto(fila);
        mapa.registrar(claseRaiz(), clave, objeto);
        cargarAsociaciones(objeto);
        return objeto;
    }

    /**
     * Inserta el objeto: {@code INSERT INTO tabla() (valoresInsercion) RETURNING columnaClave()}; asigna la clave,
     * pone la versión en 0 si la clase usa control optimista, lo registra en el mapa y llama a
     * {@code guardarPartes(objeto, true)}.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto nuevo
     * @throws ErrorNegocio si la base rechaza un turno superpuesto (restricción de exclusión)
     */
    public final void insertar(T objeto) {
        Map<String, Object> valores = valoresInsercion(objeto);
        String columnas = String.join(", ", valores.keySet());
        String marcas = String.join(", ", java.util.Collections.nCopies(valores.size(), "?"));
        String sql = "INSERT INTO " + tabla() + " (" + columnas + ") VALUES (" + marcas + ") RETURNING " + columnaClave();
        Connection conexion = proveedor.conexionActual();
        BITACORA.debug("SQL {} : {} {}", getClass().getSimpleName(), sql, valores.values());
        Object clave;
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            vincular(sentencia, new ArrayList<>(valores.values()));
            try (ResultSet rs = sentencia.executeQuery()) {
                rs.next();
                clave = rs.getObject(1);
            }
        } catch (SQLException e) {
            throw traducir(e);
        } finally {
            proveedor.liberar(conexion);
        }
        asignarClave(objeto, clave);
        if (objeto.getVersion() != null) {
            objeto.asignarVersion(0);
        }
        mapa().registrar(claseRaiz(), clave, objeto);
        guardarPartes(objeto, true);
    }

    /**
     * Actualiza el objeto: {@code UPDATE tabla() SET valoresActualizacion WHERE clave = ?}; si la clase usa control
     * optimista agrega {@code , version = version + 1 ... AND version = ?}: si no cambió ninguna fila lanza
     * {@code ConcurrenciaException}, si no asigna la versión siguiente. Luego {@code guardarPartes(objeto, false)}.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto ya persistido
     * @throws ConcurrenciaException si otro usuario modificó la fila (CU-30, CU-32, CU-33)
     */
    public final void actualizar(T objeto) {
        Map<String, Object> valores = valoresActualizacion(objeto);
        Integer version = objeto.getVersion();
        List<String> asignaciones = new ArrayList<>();
        for (String columna : valores.keySet()) {
            asignaciones.add(columna + " = ?");
        }
        if (version != null) {
            asignaciones.add("version = version + 1");
        }
        List<Object> parametros = new ArrayList<>(valores.values());
        parametros.add(claveDe(objeto));
        String sql = "UPDATE " + tabla() + " SET " + String.join(", ", asignaciones)
                + " WHERE " + columnaClave() + " = ?";
        if (version != null) {
            sql += " AND version = ?";
            parametros.add(version);
        }
        if (!asignaciones.isEmpty()) {
            int filas = ejecutarActualizacion(sql, parametros);
            if (version != null) {
                if (filas == 0) {
                    throw new ConcurrenciaException(claseObjeto().getSimpleName(), objeto.getId());
                }
                objeto.asignarVersion(version + 1);
            }
        }
        guardarPartes(objeto, false);
    }

    /**
     * Borra el objeto: {@code borrarPartes(objeto)}; {@code DELETE FROM tabla() WHERE clave = ?}; lo quita del
     * mapa de identidad.
     *
     * <p>Casos de uso: CU-09, CU-11, CU-12, CU-13, CU-24
     * @param objeto objeto persistido
     */
    public final void borrar(T objeto) {
        borrarPartes(objeto);
        Object clave = claveDe(objeto);
        ejecutarActualizacion("DELETE FROM " + tabla() + " WHERE " + columnaClave() + " = ?", List.of(clave));
        mapa().quitar(claseRaiz(), clave);
    }

    /**
     * Fachada de persistencia, para resolver asociaciones con {@code get} o consultas en los ganchos.
     *
     * <p>Casos de uso: todos
     * @return fachada
     */
    protected FachadaPersistencia fachada() {
        return fachada;
    }

    /**
     * Tabla principal de la clase.
     *
     * <p>Casos de uso: todos
     * @return nombre de la tabla
     */
    protected abstract String tabla();

    /**
     * SELECT base al que se agregan la condición de la clave o el {@code sql()} de una consulta. Por omisión
     * {@code "SELECT * FROM " + tabla()}; los brokers con herencia JOINED o con JOIN la redefinen.
     *
     * <p>Casos de uso: todos
     * @return SELECT sin WHERE
     */
    protected String consultaBase() {
        return "SELECT * FROM " + tabla();
    }

    /**
     * Desmaterialización: reconstituye el objeto ({@code Clase.reconstituir(...)}) con los valores de la fila.
     *
     * <p>Casos de uso: todos
     * @param fila fila del resultado
     * @return objeto nuevo (sin asociaciones)
     */
    protected abstract T crearObjeto(FilaResultado fila);

    /**
     * Columnas y valores del INSERT (en orden).
     *
     * <p>Casos de uso: todos
     * @param objeto objeto nuevo
     * @return mapa ordenado columna → valor
     */
    protected abstract Map<String, Object> valoresInsercion(T objeto);

    /**
     * Columnas y valores del UPDATE (sin la clave ni la versión).
     *
     * <p>Casos de uso: todos
     * @param objeto objeto persistido
     * @return mapa ordenado columna → valor
     */
    protected abstract Map<String, Object> valoresActualizacion(T objeto);

    /**
     * Gancho: resuelve las asociaciones a uno con {@code fachada().get(id, Clase)} y las entrega con
     * {@code restaurarAsociaciones(...)}. Por omisión nada.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto recién creado
     */
    protected void cargarAsociaciones(T objeto) {
        // por omisión, la clase no tiene asociaciones fuera de la fila
    }

    /**
     * Gancho: guarda las partes del agregado (tabla hija de la herencia, filas dependientes). Por omisión nada.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto guardado
     * @param alta true si se acaba de insertar
     */
    protected void guardarPartes(T objeto, boolean alta) {
        // por omisión, la clase no tiene partes
    }

    /**
     * Gancho: borra las partes del agregado antes de borrar la fila principal. Por omisión nada.
     *
     * <p>Casos de uso: CU-09, CU-24
     * @param objeto objeto que se borra
     */
    protected void borrarPartes(T objeto) {
        // por omisión, la clase no tiene partes
    }

    /**
     * Gancho: nombre de la columna clave (por omisión {@code "id"}; {@code token_sesion_persistente} usa
     * {@code "serie"}).
     *
     * <p>Casos de uso: todos
     * @return columna clave
     */
    protected String columnaClave() {
        return "id";
    }

    /**
     * Gancho: asigna la clave generada (por omisión {@code objeto.asignarId((Long) valor)}).
     *
     * <p>Casos de uso: todos
     * @param objeto objeto insertado
     * @param valor valor devuelto por {@code RETURNING}
     */
    protected void asignarClave(T objeto, Object valor) {
        objeto.asignarId(((Number) valor).longValue());
    }

    /**
     * Convierte un valor del dominio en un parámetro JDBC: {@code Dinero} → monto decimal; {@code DateTime} →
     * {@code timestamptz} en la zona del club; enumerado → nombre; estado del patrón State → {@code codigo()}.
     *
     * <p>Casos de uso: todos
     * @param valor valor del dominio
     * @return valor para {@code PreparedStatement.setObject}
     */
    protected Object aParametro(Object valor) {
        return convertirParametro(valor);
    }

    // ------------------------------------------------------------------ apoyo interno del paquete

    /**
     * Conversión de parámetros compartida con {@code ConsultaProyeccion} (ver {@code aParametro}).
     *
     * <p>Casos de uso: todos
     * @param valor valor del dominio
     * @return valor JDBC
     */
    static Object convertirParametro(Object valor) {
        if (valor == null) {
            return null;
        }
        if (valor instanceof Dinero d) {
            return d.getMonto();
        }
        if (valor instanceof LocalDateTime f) {
            return f.atZone(RelojSistema.ZONA_CLUB).toOffsetDateTime();
        }
        if (valor instanceof Enum<?> e) {
            return e.name();
        }
        if (valor instanceof ObjetoPersistente o) {
            return o.getId();
        }
        try {
            Method codigo = valor.getClass().getMethod("codigo");
            if (codigo.getReturnType() == String.class) {
                return codigo.invoke(valor);
            }
        } catch (NoSuchMethodException e) {
            // no es un estado del patrón State: se pasa tal cual
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("No se pudo leer el código del estado " + valor, e);
        }
        return valor;
    }

    /**
     * Vincula los parámetros {@code ?} de una sentencia (convertidos con {@code convertirParametro}).
     *
     * <p>Casos de uso: todos
     * @param sentencia sentencia preparada
     * @param parametros valores en orden
     * @throws SQLException si JDBC rechaza un valor
     */
    static void vincularParametros(PreparedStatement sentencia, List<Object> parametros) throws SQLException {
        for (int i = 0; i < parametros.size(); i++) {
            sentencia.setObject(i + 1, convertirParametro(parametros.get(i)));
        }
    }

    /**
     * Vincula los parámetros {@code ?} de una sentencia del broker pasando cada valor por el gancho
     * {@code aParametro} (así un broker concreto puede redefinir la conversión).
     *
     * <p>Casos de uso: todos
     * @param sentencia sentencia preparada
     * @param parametros valores en orden
     * @throws SQLException si JDBC rechaza un valor
     */
    private void vincular(PreparedStatement sentencia, List<Object> parametros) throws SQLException {
        for (int i = 0; i < parametros.size(); i++) {
            sentencia.setObject(i + 1, aParametro(parametros.get(i)));
        }
    }

    /**
     * Ejecuta un SELECT y materializa cada fila.
     *
     * <p>Casos de uso: todos
     * @param sql sentencia
     * @param parametros parámetros
     * @return objetos materializados
     */
    private List<T> ejecutarConsulta(String sql, List<Object> parametros) {
        Connection conexion = proveedor.conexionActual();
        BITACORA.debug("SQL {} : {} {}", getClass().getSimpleName(), sql, parametros);
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            vincular(sentencia, parametros);
            List<T> resultado = new ArrayList<>();
            try (ResultSet rs = sentencia.executeQuery()) {
                while (rs.next()) {
                    resultado.add(materializarDesdeFila(new FilaResultado(rs, "")));
                }
            }
            return resultado;
        } catch (SQLException e) {
            throw traducir(e);
        } finally {
            proveedor.liberar(conexion);
        }
    }

    /**
     * Ejecuta un INSERT, UPDATE o DELETE sin resultado (lo usan también las subclases en {@code guardarPartes}).
     *
     * <p>Casos de uso: todos
     * @param sql sentencia
     * @param parametros parámetros
     * @return filas afectadas
     */
    protected final int ejecutarActualizacion(String sql, List<Object> parametros) {
        Connection conexion = proveedor.conexionActual();
        BITACORA.debug("SQL {} : {} {}", getClass().getSimpleName(), sql, parametros);
        try (PreparedStatement sentencia = conexion.prepareStatement(sql)) {
            vincular(sentencia, parametros);
            return sentencia.executeUpdate();
        } catch (SQLException e) {
            throw traducir(e);
        } finally {
            proveedor.liberar(conexion);
        }
    }

    /**
     * Valor de la clave del objeto: {@code getId()} o, si la clave no es {@code id}, el valor de esa columna en
     * {@code valoresInsercion}.
     *
     * <p>Casos de uso: todos
     * @param objeto objeto persistido
     * @return valor de la clave
     */
    private Object claveDe(T objeto) {
        if ("id".equals(columnaClave())) {
            return objeto.getId();
        }
        return valoresInsercion(objeto).get(columnaClave());
    }

    /**
     * Mapa de identidad de la transacción en curso.
     *
     * <p>Casos de uso: todos
     * @return mapa
     */
    private MapaIdentidad mapa() {
        return proveedor.mapaIdentidadActual();
    }

    /**
     * Traduce una {@code SQLException}: la violación de la restricción de exclusión (SQLState 23P01) pasa a
     * {@code ErrorNegocio("El turno ya fue reservado; elija otro turno.")}; el resto a una excepción técnica.
     *
     * <p>Casos de uso: todos
     * @param e excepción de JDBC
     * @return excepción no comprobada
     */
    private RuntimeException traducir(SQLException e) {
        if (VIOLACION_EXCLUSION.equals(e.getSQLState())) {
            return new ErrorNegocio("El turno ya fue reservado; elija otro turno.");
        }
        return new IllegalStateException("Error de base de datos en " + getClass().getSimpleName() + ": " + e.getMessage(), e);
    }
}
