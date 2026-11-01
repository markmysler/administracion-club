package ar.edu.ub.club.compartido.ui;

/**
 * Contrato pasivo común de todas las pantallas (MVP, variante Vista Pasiva), sin tipos de Vaadin: lo que cualquier
 * presentador puede pedirle a su vista. Cada pantalla lo extiende con su interfaz {@code Pantalla*}.
 *
 * <p>Casos de uso: todos
 */
public interface PantallaBase {

    /**
     * Muestra un mensaje de error (zona de mensajes, {@code aria-live="assertive"}).
     *
     * <p>Casos de uso: todos
     * @param mensaje texto del error
     */
    void mostrarError(String mensaje);

    /**
     * Muestra un mensaje informativo o de éxito.
     *
     * <p>Casos de uso: todos
     * @param mensaje texto
     */
    void mostrarMensaje(String mensaje);

    /**
     * Marca un campo del formulario con un error de negocio ({@code ErrorNegocio} con campo).
     *
     * <p>Casos de uso: todos
     * @param campo propiedad del objeto {@code Datos*}
     * @param mensaje texto del error
     */
    void marcarCampo(String campo, String mensaje);

    /**
     * Navega a otra pantalla por su ruta (texto; sin depender de la clase de vista destino).
     *
     * <p>Casos de uso: todos
     * @param ruta ruta de la tabla de pantallas, por ejemplo {@code "socios/ficha/5"}
     */
    void navegarA(String ruta);
}
