package ar.edu.ub.club.pagos.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Configuración de la pasarela de pago (fila única, CU-34). Las credenciales no se guardan: solo se registra si
 * están configuradas. Su {@code modo} decide el adaptador activo ({@code ServicioPagos}).
 *
 * <p>Casos de uso: CU-34, CU-35
 */
public class ConfiguracionPasarela extends EntidadPersistente {

    /** Identificador de la única fila (atributo de clase, {@code {static}} en el diseño). */
    public static final Long ID_UNICO = 1L;

    /** Proveedor. */
    private String proveedor;

    /** Adaptador activo. */
    private ModoPasarela modo;

    /** Si se aceptan pagos online. */
    private Boolean habilitada;

    /** Si el entorno tiene credenciales válidas. */
    private Boolean credencialesConfiguradas;

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: CU-34, CU-35
     */
    private ConfiguracionPasarela() {
    }

    /**
     * Reconstituye la fila única de {@code configuracion_pasarela} (operación «persistencia»).
     *
     * <p>Casos de uso: CU-34, CU-35
     * @param id Identificador (1).
     * @param proveedor Proveedor.
     * @param modo Modo.
     * @param habilitada Si está habilitada.
     * @param credencialesConfiguradas Si hay credenciales.
     * @return la configuración
     */
    public static ConfiguracionPasarela reconstituir(Long id, String proveedor, ModoPasarela modo, Boolean habilitada, Boolean credencialesConfiguradas) {
        ConfiguracionPasarela c = new ConfiguracionPasarela();
        c.asignarId(id);
        c.proveedor = proveedor;
        c.modo = modo;
        c.habilitada = habilitada;
        c.credencialesConfiguradas = credencialesConfiguradas;
        return c;
    }

    /**
     * Agregado en la implementación: actualiza el modo, la habilitación y si hay credenciales (CU-34, pasos 6 a 9;
     * el diseño dice «habilitada = …» sin nombrar la operación). En MERCADO_PAGO sin credenciales queda
     * deshabilitada (CHECK {@code ck_pasarela_credenciales}).
     *
     * <p>Casos de uso: CU-34
     * @param modo Adaptador elegido.
     * @param habilitada Si se aceptan pagos online.
     * @param credencialesConfiguradas Si el entorno tiene credenciales.
     */
    public void configurar(ModoPasarela modo, boolean habilitada, boolean credencialesConfiguradas) {
        this.modo = modo;
        this.credencialesConfiguradas = credencialesConfiguradas;
        this.habilitada = habilitada && (modo == ModoPasarela.SIMULADA || credencialesConfiguradas);
    }

    /**
     * Devuelve {@code ID_UNICO}: Identificador de la única fila.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return 1
     */
    public Long getID_UNICO() {
        return ID_UNICO;
    }

    /**
     * Devuelve {@code proveedor}: Proveedor.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public String getProveedor() {
        return proveedor;
    }

    /**
     * Devuelve {@code modo}: Adaptador activo.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return el valor del atributo
     */
    public ModoPasarela getModo() {
        return modo;
    }

    /**
     * Devuelve {@code habilitada}: Si se aceptan pagos online.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @return el valor del atributo
     */
    public Boolean getHabilitada() {
        return habilitada;
    }

    /**
     * Devuelve {@code credencialesConfiguradas}: Si el entorno tiene credenciales válidas.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public Boolean getCredencialesConfiguradas() {
        return credencialesConfiguradas;
    }
}
