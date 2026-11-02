package ar.edu.ub.club.socios.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;

/**
 * Categoría de socio con el importe que aporta cada integrante a la cuota mensual del grupo familiar.
 *
 * <p>Entidad del dominio ({@code socios.dominio}); la persiste {@code BrokerCategoriaSocio} (Database Broker).
 * Information Expert de sus propios datos: valida el nombre y el importe al crearse y al modificarse. Las cuotas
 * ya emitidas no cambian cuando se modifica el importe porque {@code ItemCuota} guarda una foto de la categoría
 * al emitir (RN-08).
 *
 * <p>Casos de uso: todos
 */
public class CategoriaSocio extends EntidadPersistente {

    /** Nombre (único entre las categorías). */
    private String nombre;

    /** Importe mensual que aporta cada integrante con esta categoría (mayor o igual a 0). */
    private Dinero importe;

    /** Asignable: solo una categoría activa puede asignarse a un socio. */
    private Boolean activa;

    /**
     * Crea una categoría nueva (CU-06, alta) validando nombre e importe.
     *
     * <p>Casos de uso: CU-06
     * @param nombre nombre de la categoría (obligatorio)
     * @param importe importe mensual (mayor o igual a 0)
     * @param activa si la categoría es asignable ({@code null} se toma como {@code true}, valor inicial del diseño)
     * @throws ErrorNegocio si el nombre está vacío o el importe falta o es negativo (CU-06 FA2)
     */
    public CategoriaSocio(String nombre, Dinero importe, Boolean activa) {
        validar(nombre, importe);
        this.nombre = nombre.trim();
        this.importe = importe;
        this.activa = activa == null ? Boolean.TRUE : activa;
    }

    /**
     * Constructor vacío que usa {@code reconstituir} para rehidratar la categoría desde la base sin validar.
     *
     * <p>Casos de uso: todos
     */
    private CategoriaSocio() {
    }

    /**
     * Aplica los cambios de CU-06 (nombre, importe y estado). No altera cuotas ya emitidas (RN-08). El control
     * de que no se desactive una categoría asignada a socios activos lo hace {@code ServicioSocios} porque
     * requiere consultar la base.
     *
     * <p>Casos de uso: CU-06
     * @param nombre Nombre.
     * @param importe Importe.
     * @param activa Estado.
     * @throws ErrorNegocio si el nombre está vacío o el importe es negativo (CU-06 FA2)
     */
    public void modificar(String nombre, Dinero importe, Boolean activa) {
        validar(nombre, importe);
        this.nombre = nombre.trim();
        this.importe = importe;
        this.activa = activa == null ? this.activa : activa;
    }

    /**
     * Indica si la categoría puede asignarse a un socio (CU-01 paso 7, CU-02 paso 8).
     *
     * <p>Agregado en la implementación: consulta de conveniencia sobre {@code activa} (evita comparar el
     * {@code Boolean} en cada fachada).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-06
     * @return true si {@code activa} es verdadero
     */
    public boolean estaActiva() {
        return Boolean.TRUE.equals(activa);
    }

    /**
     * Reconstituye la categoría desde una fila de {@code categoria_socio}. Lo usa solo
     * {@code BrokerCategoriaSocio.crearObjeto} (estereotipo «persistencia»); no valida porque los datos ya
     * cumplen las restricciones de la tabla.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param nombre nombre
     * @param importe importe mensual
     * @param activa estado
     * @return la categoría rehidratada
     */
    public static CategoriaSocio reconstituir(Long id, String nombre, Dinero importe, Boolean activa) {
        CategoriaSocio categoria = new CategoriaSocio();
        categoria.asignarId(id);
        categoria.nombre = nombre;
        categoria.importe = importe;
        categoria.activa = activa;
        return categoria;
    }

    /**
     * Valida nombre obligatorio e importe no negativo (CU-06 paso 6, constraint {@code ck_categoria_importe}).
     *
     * <p>Casos de uso: CU-06
     * @param nombre nombre propuesto
     * @param importe importe propuesto
     * @throws ErrorNegocio con el campo que falla
     */
    private static void validar(String nombre, Dinero importe) {
        if (nombre == null || nombre.isBlank()) {
            throw new ErrorNegocio("nombre", "El nombre de la categoría es obligatorio.");
        }
        if (importe == null || importe.esNegativo()) {
            throw new ErrorNegocio("importe", "El importe debe ser un monto mayor o igual a cero.");
        }
    }

    /**
     * Devuelve {@code nombre}: Nombre.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code importe}: Importe mensual.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Dinero getImporte() {
        return importe;
    }

    /**
     * Devuelve {@code activa}: Asignable.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Boolean getActiva() {
        return activa;
    }
}
