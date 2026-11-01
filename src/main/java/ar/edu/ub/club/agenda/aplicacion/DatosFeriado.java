package ar.edu.ub.club.agenda.aplicacion;

import java.time.LocalDate;

/**
 * Objeto de comando del alta de feriado en P-17 (CU-24 FA1): fecha, descripción y la marca {@code confirmar}
 * (true después de que el administrador acepta las reservas confirmadas de esa fecha, que se conservan). Lo
 * vincula el {@code Binder} y lo recibe {@code ServicioAgenda.registrarFeriado}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-24
 */
public class DatosFeriado {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo descripcion. */
    private String descripcion;

    /** Atributo confirmar. */
    private boolean confirmar;

    /**
     * Devuelve {@code fecha}: fecha.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Asigna {@code fecha} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param fecha nuevo valor
     */
    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    /**
     * Devuelve {@code descripcion}: descripcion.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Asigna {@code descripcion} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param descripcion nuevo valor
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Devuelve {@code confirmar}: confirmar.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public boolean getConfirmar() {
        return confirmar;
    }

    /**
     * Asigna {@code confirmar} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-24
     * @param confirmar nuevo valor
     */
    public void setConfirmar(boolean confirmar) {
        this.confirmar = confirmar;
    }
}
