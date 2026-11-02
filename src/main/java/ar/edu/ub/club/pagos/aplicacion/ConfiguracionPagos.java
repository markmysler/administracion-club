package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * Resultado de {@code ServicioPagos.consultarMediosYPasarela}: los medios de pago manuales y la configuración de
 * la pasarela (P-30 y P-31).
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34, CU-36
 */
public class ConfiguracionPagos {

    /** Medios de pago manuales. */
    private List<MedioPago> medios;

    /** Configuración de la pasarela. */
    private ConfiguracionPasarela pasarela;

    /**
     * Crea el resultado.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @param medios Medios de pago.
     * @param pasarela Configuración de la pasarela.
     */
    public ConfiguracionPagos(List<MedioPago> medios, ConfiguracionPasarela pasarela) {
        this.medios = medios;
        this.pasarela = pasarela;
    }

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-34
     */
    public ConfiguracionPagos() {
        this.medios = List.of();
    }

    /**
     * Devuelve {@code medios}.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return el valor del atributo
     */
    public List<MedioPago> getMedios() {
        return medios;
    }

    /**
     * Asigna {@code medios}.
     *
     * <p>Casos de uso: CU-34
     * @param medios nuevo valor
     */
    public void setMedios(List<MedioPago> medios) {
        this.medios = medios;
    }

    /**
     * Devuelve {@code pasarela}.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return el valor del atributo
     */
    public ConfiguracionPasarela getPasarela() {
        return pasarela;
    }

    /**
     * Asigna {@code pasarela}.
     *
     * <p>Casos de uso: CU-34
     * @param pasarela nuevo valor
     */
    public void setPasarela(ConfiguracionPasarela pasarela) {
        this.pasarela = pasarela;
    }
}
