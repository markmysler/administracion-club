package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import java.util.ArrayList;
import java.util.List;

/**
 * Objeto de comando del formulario de P-31: medios (con su habilitación; los de id nulo son nuevos), modo de la
 * pasarela y si se la habilita. Lo recibe {@code ServicioPagos.configurarMediosYPasarela}.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-34
 */
public class DatosConfiguracionPagos {

    /** Medios de pago. */
    private List<DatosMedioPago> medios;

    /** Modo de la pasarela. */
    private ModoPasarela modo;

    /** Si se habilita la pasarela. */
    private boolean habilitada;

    /**
     * Constructor vacío que requiere el {@code Binder} de Vaadin.
     *
     * <p>Casos de uso: CU-34
     */
    public DatosConfiguracionPagos() {
        this.medios = new ArrayList<>();
        this.modo = ModoPasarela.SIMULADA;
    }

    /**
     * Copia la configuración actual para editarla en P-31.
     *
     * <p>Casos de uso: CU-34
     * @param configuracion Configuración actual.
     * @return objeto de comando
     */
    public static DatosConfiguracionPagos desde(ConfiguracionPagos configuracion) {
        DatosConfiguracionPagos d = new DatosConfiguracionPagos();
        configuracion.getMedios().forEach(m -> d.medios.add(DatosMedioPago.desde(m)));
        if (configuracion.getPasarela() != null) {
            d.modo = configuracion.getPasarela().getModo();
            d.habilitada = Boolean.TRUE.equals(configuracion.getPasarela().getHabilitada());
        }
        return d;
    }

    /**
     * Devuelve {@code medios}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public List<DatosMedioPago> getMedios() {
        return medios;
    }

    /**
     * Asigna {@code medios}.
     *
     * <p>Casos de uso: CU-34
     * @param medios nuevo valor
     */
    public void setMedios(List<DatosMedioPago> medios) {
        this.medios = medios;
    }

    /**
     * Devuelve {@code modo}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public ModoPasarela getModo() {
        return modo;
    }

    /**
     * Asigna {@code modo}.
     *
     * <p>Casos de uso: CU-34
     * @param modo nuevo valor
     */
    public void setModo(ModoPasarela modo) {
        this.modo = modo;
    }

    /**
     * Devuelve {@code habilitada}.
     *
     * <p>Casos de uso: CU-34
     * @return el valor del atributo
     */
    public boolean getHabilitada() {
        return habilitada;
    }

    /**
     * Asigna {@code habilitada}.
     *
     * <p>Casos de uso: CU-34
     * @param habilitada nuevo valor
     */
    public void setHabilitada(boolean habilitada) {
        this.habilitada = habilitada;
    }
}
