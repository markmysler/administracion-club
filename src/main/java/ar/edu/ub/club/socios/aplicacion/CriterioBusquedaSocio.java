package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-04
 */
public class CriterioBusquedaSocio {

    /** Atributo texto. */
    private String texto;

    /** Atributo estado. */
    private EstadoCuenta estado;

    /**
     * Devuelve {@code texto}: texto.
     *
     * <p>Casos de uso: CU-04
     * @return el valor del atributo
     */
    public String getTexto() {
        return texto;
    }

    /**
     * Asigna {@code texto} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-04
     * @param texto nuevo valor
     */
    public void setTexto(String texto) {
        this.texto = texto;
    }

    /**
     * Devuelve {@code estado}: estado.
     *
     * <p>Casos de uso: CU-04
     * @return el valor del atributo
     */
    public EstadoCuenta getEstado() {
        return estado;
    }

    /**
     * Asigna {@code estado} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-04
     * @param estado nuevo valor
     */
    public void setEstado(EstadoCuenta estado) {
        this.estado = estado;
    }
}
