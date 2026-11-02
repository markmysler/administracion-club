package ar.edu.ub.club.compartido.excepciones;

/**
 * Excepción de regla de negocio incumplida. En la versión 6.0 puede indicar el campo del formulario al que se
 * refiere, para que {@code PresentadorBase} lo marque con {@code pantalla.marcarCampo}.
 *
 * <p>La lanzan las fachadas {@code Servicio*}, el dominio y los validadores de la cadena de reservas; la traduce
 * {@code PresentadorBase.ejecutar}/{@code consultar} sin que la vista conozca la regla.
 *
 * <p>Casos de uso: todos
 */
public class ErrorNegocio extends RuntimeException {

    /** Mensaje para el usuario. */
    private final String mensaje;

    /** Propiedad del objeto de comando a la que se refiere (opcional). */
    private final String campo;

    /**
     * Crea el error sin campo asociado: el presentador lo muestra como mensaje general.
     *
     * <p>Casos de uso: todos
     * @param mensaje mensaje para el usuario
     */
    public ErrorNegocio(String mensaje) {
        this(null, mensaje);
    }

    /**
     * Crea el error asociado a un campo del formulario, que el presentador marca con {@code marcarCampo}.
     *
     * <p>Casos de uso: todos
     * @param campo propiedad del objeto {@code Datos*} a la que se refiere el error
     * @param mensaje mensaje para el usuario
     */
    public ErrorNegocio(String campo, String mensaje) {
        super(mensaje);
        this.campo = campo;
        this.mensaje = mensaje;
    }

    /**
     * Devuelve el mensaje para el usuario.
     *
     * <p>Casos de uso: todos
     * @return mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * Devuelve el campo del formulario al que se refiere el error, o {@code null}.
     *
     * <p>Casos de uso: todos
     * @return nombre de la propiedad o {@code null}
     */
    public String getCampo() {
        return campo;
    }
}
