package ar.edu.ub.club.compartido.persistencia;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import ar.edu.ub.club.compartido.excepciones.ConcurrenciaException;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import java.sql.Connection;
import java.sql.Statement;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.transaction.support.TransactionTemplate;
import org.testcontainers.DockerClientFactory;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Prueba del método plantilla de {@code BrokerBaseDatos} (Database Broker) y de {@code FachadaPersistencia} contra un
 * PostgreSQL 16 real levantado con Testcontainers: inserción con clave generada, materialización, mapa de identidad
 * por transacción, actualización con control optimista ({@code ConcurrenciaException}), borrado, objetos consulta de
 * entidades y de proyección, y carga diferida con {@code ProxyListaVirtual}. Si Docker no está disponible la prueba
 * se omite.
 *
 * <p>Casos de uso: todos
 */
class BrokerBaseDatosTest {

    /** Base de datos de prueba. */
    private static PostgreSQLContainer<?> base;

    /** Fachada bajo prueba. */
    private static FachadaPersistencia fachada;

    /** Transacciones de Spring sobre la base de prueba. */
    private static TransactionTemplate transaccion;

    /** Origen de datos de la base de prueba. */
    private static DriverManagerDataSource origen;

    /**
     * Entidad mínima de prueba con control optimista.
     *
     * <p>Casos de uso: todos
     */
    static class Pelota extends EntidadPersistente {

        /** Color de la pelota. */
        private String color;

        /**
         * Crea una pelota nueva (versión 0: usa control optimista).
         *
         * <p>Casos de uso: todos
         * @param color color
         */
        Pelota(String color) {
            this.color = color;
            asignarVersion(0);
        }

        /**
         * Devuelve el color.
         *
         * <p>Casos de uso: todos
         * @return color
         */
        String getColor() {
            return color;
        }

        /**
         * Cambia el color.
         *
         * <p>Casos de uso: todos
         * @param color nuevo color
         */
        void pintar(String color) {
            this.color = color;
        }
    }

    /**
     * Broker concreto de prueba: completa los ganchos del método plantilla.
     *
     * <p>Casos de uso: todos
     */
    static class BrokerPelota extends BrokerBaseDatos<Pelota> {

        /**
         * Crea el broker.
         *
         * <p>Casos de uso: todos
         * @param proveedor proveedor de conexiones
         */
        BrokerPelota(ProveedorConexiones proveedor) {
            super(proveedor);
        }

        /**
         * Clase que maneja.
         *
         * <p>Casos de uso: todos
         * @return Pelota
         */
        @Override
        public Class<Pelota> claseObjeto() {
            return Pelota.class;
        }

        /**
         * Tabla de la clase.
         *
         * <p>Casos de uso: todos
         * @return "pelota"
         */
        @Override
        protected String tabla() {
            return "pelota";
        }

        /**
         * Reconstituye la pelota desde la fila.
         *
         * <p>Casos de uso: todos
         * @param fila fila
         * @return pelota
         */
        @Override
        protected Pelota crearObjeto(FilaResultado fila) {
            Pelota p = new Pelota(fila.texto("color"));
            p.asignarId(fila.largo("id"));
            p.asignarVersion(fila.entero("version"));
            return p;
        }

        /**
         * Valores del INSERT.
         *
         * <p>Casos de uso: todos
         * @param objeto pelota
         * @return columnas
         */
        @Override
        protected Map<String, Object> valoresInsercion(Pelota objeto) {
            Map<String, Object> v = new LinkedHashMap<>();
            v.put("color", objeto.getColor());
            v.put("version", objeto.getVersion());
            return v;
        }

        /**
         * Valores del UPDATE.
         *
         * <p>Casos de uso: todos
         * @param objeto pelota
         * @return columnas
         */
        @Override
        protected Map<String, Object> valoresActualizacion(Pelota objeto) {
            Map<String, Object> v = new LinkedHashMap<>();
            v.put("color", objeto.getColor());
            return v;
        }
    }

    /**
     * Objeto consulta de entidades: pelotas de un color.
     *
     * <p>Casos de uso: todos
     */
    static class PelotasDeColor extends ConsultaEntidades<Pelota> {

        /** Color buscado. */
        private final String color;

        /**
         * Crea la consulta.
         *
         * <p>Casos de uso: todos
         * @param color color buscado
         */
        PelotasDeColor(String color) {
            this.color = color;
        }

        /**
         * Clase de resultado.
         *
         * <p>Casos de uso: todos
         * @return Pelota
         */
        @Override
        public Class<Pelota> claseResultado() {
            return Pelota.class;
        }

        /**
         * Condición.
         *
         * <p>Casos de uso: todos
         * @return WHERE
         */
        @Override
        public String sql() {
            return "WHERE pelota.color = ? ORDER BY pelota.id";
        }

        /**
         * Parámetros.
         *
         * <p>Casos de uso: todos
         * @return color
         */
        @Override
        public List<Object> parametros() {
            return List.of(color);
        }
    }

    /**
     * Objeto consulta de proyección: cantidad de pelotas.
     *
     * <p>Casos de uso: todos
     */
    static class CantidadPelotas extends ConsultaProyeccion<Long> {

        /**
         * Clase de resultado.
         *
         * <p>Casos de uso: todos
         * @return Long
         */
        @Override
        public Class<Long> claseResultado() {
            return Long.class;
        }

        /**
         * Sentencia completa.
         *
         * <p>Casos de uso: todos
         * @return SELECT
         */
        @Override
        public String sql() {
            return "SELECT COUNT(*) AS cantidad FROM pelota";
        }

        /**
         * Sin parámetros.
         *
         * <p>Casos de uso: todos
         * @return lista vacía
         */
        @Override
        public List<Object> parametros() {
            return List.of();
        }

        /**
         * Lee la cantidad.
         *
         * <p>Casos de uso: todos
         * @param fila fila
         * @return cantidad
         */
        @Override
        public Long leerFila(FilaResultado fila) {
            return fila.largo("cantidad");
        }
    }

    /**
     * Levanta PostgreSQL, crea la tabla y arma la fachada con el broker de prueba.
     *
     * <p>Casos de uso: todos
     * @throws Exception si no se puede crear la tabla
     */
    @BeforeAll
    static void iniciar() throws Exception {
        org.junit.jupiter.api.Assumptions.assumeTrue(dockerDisponible(), "Docker no disponible");
        base = new PostgreSQLContainer<>("postgres:16");
        base.start();
        origen = new DriverManagerDataSource(base.getJdbcUrl() + "&stringtype=unspecified", base.getUsername(),
                base.getPassword());
        try (Connection c = origen.getConnection(); Statement s = c.createStatement()) {
            s.execute("CREATE TABLE pelota (id BIGSERIAL PRIMARY KEY, color VARCHAR(20) NOT NULL, version INTEGER NOT NULL)");
        }
        ProveedorConexiones proveedor = new ProveedorConexiones(origen);
        fachada = new FachadaPersistencia(List.of(new BrokerPelota(proveedor)), proveedor, new DataSourceTransactionManager(origen));
        transaccion = new TransactionTemplate(new DataSourceTransactionManager(origen));
    }

    /**
     * Detiene la base de prueba.
     *
     * <p>Casos de uso: todos
     */
    @AfterAll
    static void terminar() {
        if (base != null) {
            base.stop();
        }
    }

    /**
     * Vacía la tabla antes de cada prueba.
     *
     * <p>Casos de uso: todos
     * @throws Exception si falla el borrado
     */
    @BeforeEach
    void vaciar() throws Exception {
        try (Connection c = origen.getConnection(); Statement s = c.createStatement()) {
            s.execute("DELETE FROM pelota");
        }
    }

    /**
     * Indica si hay un Docker disponible para Testcontainers.
     *
     * <p>Casos de uso: todos
     * @return true si Docker responde
     */
    private static boolean dockerDisponible() {
        try {
            return DockerClientFactory.instance().isDockerAvailable();
        } catch (Throwable e) {
            return false;
        }
    }

    /**
     * {@code put} de un objeto nuevo inserta, asigna el id y la versión 0; {@code get} en otra transacción lo
     * materializa desde la base.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void insertaYMaterializa() {
        Pelota roja = new Pelota("roja");
        transaccion.executeWithoutResult(t -> fachada.put(roja));
        assertThat(roja.getId()).isNotNull();
        assertThat(roja.getVersion()).isZero();
        Pelota leida = transaccion.execute(t -> fachada.get(roja.getId(), Pelota.class));
        assertThat(leida).isNotSameAs(roja);
        assertThat(leida.getColor()).isEqualTo("roja");
    }

    /**
     * Dentro de una transacción, dos lecturas de la misma fila devuelven la misma instancia (mapa de identidad),
     * también cuando la segunda llega por un objeto consulta.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void mapaDeIdentidadDevuelveLaMismaInstancia() {
        Pelota azul = new Pelota("azul");
        transaccion.executeWithoutResult(t -> fachada.put(azul));
        transaccion.executeWithoutResult(t -> {
            Pelota a = fachada.get(azul.getId(), Pelota.class);
            Pelota b = fachada.get(azul.getId(), Pelota.class);
            Pelota c = fachada.buscarUno(new PelotasDeColor("azul")).orElseThrow();
            assertThat(a).isSameAs(b).isSameAs(c);
        });
    }

    /**
     * {@code actualizar} incrementa la versión; una actualización con una versión vieja lanza
     * {@code ConcurrenciaException} (control optimista, CU-32).
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     */
    @Test
    void controlOptimistaDetectaElConflicto() {
        Pelota verde = new Pelota("verde");
        transaccion.executeWithoutResult(t -> fachada.put(verde));
        Pelota copiaA = transaccion.execute(t -> fachada.get(verde.getId(), Pelota.class));
        Pelota copiaB = transaccion.execute(t -> fachada.get(verde.getId(), Pelota.class));
        copiaA.pintar("negra");
        transaccion.executeWithoutResult(t -> fachada.put(copiaA));
        assertThat(copiaA.getVersion()).isEqualTo(1);
        copiaB.pintar("blanca");
        assertThatThrownBy(() -> transaccion.executeWithoutResult(t -> fachada.put(copiaB)))
                .isInstanceOf(ConcurrenciaException.class);
    }

    /**
     * {@code eliminar} borra la fila y {@code get} lanza {@code RecursoNoEncontradoException}.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void eliminaYLuegoNoEncuentra() {
        Pelota gris = new Pelota("gris");
        transaccion.executeWithoutResult(t -> fachada.put(gris));
        transaccion.executeWithoutResult(t -> fachada.eliminar(fachada.get(gris.getId(), Pelota.class)));
        assertThatThrownBy(() -> transaccion.execute(t -> fachada.get(gris.getId(), Pelota.class)))
                .isInstanceOf(RecursoNoEncontradoException.class);
    }

    /**
     * Objetos consulta de entidades y de proyección, {@code contar} y la carga diferida del proxy virtual.
     *
     * <p>Casos de uso: todos
     */
    @Test
    void consultasYProxyVirtual() {
        transaccion.executeWithoutResult(t -> {
            fachada.put(new Pelota("roja"));
            fachada.put(new Pelota("roja"));
            fachada.put(new Pelota("azul"));
        });
        transaccion.executeWithoutResult(t -> {
            assertThat(fachada.buscar(new PelotasDeColor("roja"))).hasSize(2);
            assertThat(fachada.contar(new PelotasDeColor("roja"))).isEqualTo(2);
            assertThat(fachada.buscarUno(new CantidadPelotas())).contains(3L);
            ProxyListaVirtual<Pelota> proxy = new ProxyListaVirtual<>(fachada, new PelotasDeColor("azul"));
            assertThat(proxy.estaCargada()).isFalse();
            assertThat(proxy).hasSize(1);
            assertThat(proxy.estaCargada()).isTrue();
        });
    }
}
