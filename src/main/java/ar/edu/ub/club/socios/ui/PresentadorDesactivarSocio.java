package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import java.util.List;

/**
 * Presentador de la pantalla P-09: Diálogo de baja de socio en dos pasos: muestra las reservas futuras que se
 * cancelarán y, si el socio es titular de un grupo con otros integrantes activos, pide elegir el nuevo titular.
 *
 * <p>Casos de uso: CU-03
 */
public class PresentadorDesactivarSocio extends PresentadorBase<PantallaDesactivarSocio> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo idSocio. */
    private Long idSocio;

    /**
     * {@code pantalla.mostrarSocio(nombre, dni)}; {@code ServicioSocios.desactivarSocio(idSocio, null, false):
     * ResultadoBaja}; si {@code realizada} cierra; si no muestra {@code FilaResumenReserva.desde(...)} y, si hay
     * candidatos, pide el nuevo titular
     *
     * <p>Casos de uso: CU-03
     * @param idSocio valor de {@code idSocio}
     * @param nombre valor de {@code nombre}
     * @param dni valor de {@code dni}
     * @param candidatosTitular valor de {@code candidatosTitular}
     */
    public void alAbrir(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioSocios.desactivarSocio(idSocio, idNuevoTitular, true): ResultadoBaja}; dispara {@code
     * DialogoResueltoEvento}
     *
     * <p>Casos de uso: CU-03
     * @param idNuevoTitular valor de {@code idNuevoTitular}
     */
    public void alConfirmar(Long idNuevoTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
