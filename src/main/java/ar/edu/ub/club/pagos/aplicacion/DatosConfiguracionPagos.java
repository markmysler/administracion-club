package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class DatosConfiguracionPagos {

    /** Atributo medios. */
    private List<DatosMedioPago> medios;

    /** Atributo modo. */
    private ModoPasarela modo;

    /** Atributo habilitada. */
    private boolean habilitada;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-34
     * @param configuracion valor de {@code configuracion}
     * @return el resultado
     */
    public static DatosConfiguracionPagos desde(ConfiguracionPagos configuracion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code medios}: medios.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public List<DatosMedioPago> getMedios() {
        return medios;
    }

    /**
     * Asigna {@code medios} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-34
     * @param medios nuevo valor
     */
    public void setMedios(List<DatosMedioPago> medios) {
        this.medios = medios;
    }

    /**
     * Devuelve {@code modo}: modo.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public ModoPasarela getModo() {
        return modo;
    }

    /**
     * Asigna {@code modo} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-34
     * @param modo nuevo valor
     */
    public void setModo(ModoPasarela modo) {
        this.modo = modo;
    }

    /**
     * Devuelve {@code habilitada}: habilitada.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public boolean getHabilitada() {
        return habilitada;
    }

    /**
     * Asigna {@code habilitada} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-34
     * @param habilitada nuevo valor
     */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}
