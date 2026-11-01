package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class ConfiguracionPagos {

    /** Atributo medios. */
    private List<MedioPago> medios;

    /** Atributo pasarela. */
    private ConfiguracionPasarela pasarela;

    /**
     * Crea una instancia de {@code ConfiguracionPagos}.
     *
     * <p>Casos de uso: CU-34
     * @param medios valor de {@code medios}
     * @param pasarela valor de {@code pasarela}
     */
    public ConfiguracionPagos(List<MedioPago> medios, ConfiguracionPasarela pasarela) {
        // PENDIENTE
    }

    /**
     * Devuelve {@code medios}: medios.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public List<MedioPago> getMedios() {
        return medios;
    }

    /**
     * Asigna {@code medios} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-34
     * @param medios nuevo valor
     */
    public void setMedios(List<MedioPago> medios) {
        this.medios = medios;
    }

    /**
     * Devuelve {@code pasarela}: pasarela.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public ConfiguracionPasarela getPasarela() {
        return pasarela;
    }

    /**
     * Asigna {@code pasarela} (lo usa el {@code Binder} del formulario).
     *
     * <p>Casos de uso: CU-34
     * @param pasarela nuevo valor
     */
    public void setPasarela(ConfiguracionPasarela pasarela) {
        this.pasarela = pasarela;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-34
     */
    public ConfiguracionPagos() {
    }
}
