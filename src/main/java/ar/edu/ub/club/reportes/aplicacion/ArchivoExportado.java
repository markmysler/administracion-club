package ar.edu.ub.club.reportes.aplicacion;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ArchivoExportado {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo tipoContenido. */
    private String tipoContenido;

    /** Atributo contenido. */
    private byte[] contenido;

    /**
     * Devuelve {@code nombre}: nombre.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve {@code tipoContenido}: tipoContenido.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public String getTipoContenido() {
        return tipoContenido;
    }

    /**
     * Devuelve {@code contenido}: contenido.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public byte[] getContenido() {
        return contenido;
    }
}
