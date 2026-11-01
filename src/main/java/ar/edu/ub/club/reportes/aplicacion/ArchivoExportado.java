package ar.edu.ub.club.reportes.aplicacion;

/**
 * Archivo generado por una estrategia {@code ExportadorReporte}: nombre sugerido, tipo MIME y bytes. Lo devuelve
 * {@code ServicioReportes.exportarReporte} y el presentador lo entrega a la vista con {@code pantalla.descargar(...)},
 * que arma el {@code StreamResource} de descarga (MODELO 11.5.4).
 *
 * <p>Objeto de datos de salida de la capa de aplicación (inmutable).
 *
 * <p>Casos de uso: CU-38, CU-39
 */
public class ArchivoExportado {

    /** Nombre sugerido del archivo, con su extensión (.xlsx o .pdf). */
    private String nombre;

    /** Tipo de contenido (MIME) del archivo. */
    private String tipoContenido;

    /** Bytes del archivo generado. */
    private byte[] contenido;

    /**
     * Crea el archivo exportado con todos sus datos.
     *
     * <p>Agregado en la implementación: constructor con los atributos del diseño (el esqueleto solo traía getters).
     *
     * <p>Casos de uso: CU-38, CU-39
     * @param nombre nombre sugerido del archivo, con extensión
     * @param tipoContenido tipo MIME del archivo
     * @param contenido bytes del archivo
     */
    public ArchivoExportado(String nombre, String tipoContenido, byte[] contenido) {
        this.nombre = nombre;
        this.tipoContenido = tipoContenido;
        this.contenido = contenido;
    }

    /**
     * Devuelve el nombre sugerido del archivo.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return nombre con extensión
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Devuelve el tipo de contenido (MIME) del archivo.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return tipo MIME
     */
    public String getTipoContenido() {
        return tipoContenido;
    }

    /**
     * Devuelve los bytes del archivo.
     *
     * <p>Casos de uso: CU-38, CU-39
     * @return contenido del archivo
     */
    public byte[] getContenido() {
        return contenido;
    }
}
