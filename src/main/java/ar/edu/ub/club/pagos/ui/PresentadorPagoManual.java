package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;

/**
 * Presentador de la pantalla P-30: Registro de un pago manual: búsqueda del grupo, elección de la cuota impaga,
 * medio habilitado e importe (igual al importe a pagar de la cuota).
 *
 * <p>Casos de uso: CU-36
 */
public class PresentadorPagoManual extends PresentadorBase<PantallaPagoManual> {

    /** Atributo servicioPagos. */
    private ServicioPagos servicioPagos;

    /** Atributo servicioGruposFamiliares. */
    private ServicioGruposFamiliares servicioGruposFamiliares;

    /** Atributo servicioCuotas. */
    private ServicioCuotas servicioCuotas;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /**
     * {@code ServicioPagos.consultarMediosYPasarela(): ConfiguracionPagos} → medios habilitados como {@code
     * OpcionSeleccion}
     *
     * <p>Casos de uso: CU-36
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, true))} → {@code FilaGrupo}
     *
     * <p>Casos de uso: CU-36
     * @param texto valor de {@code texto}
     */
    public void alBuscarGrupo(String texto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioCuotas.consultarCuotasDeGrupo(idGrupo)} → {@code FilaCuota.desde(c, false)} (solo impagas)
     *
     * <p>Casos de uso: CU-36
     * @param idGrupo valor de {@code idGrupo}
     */
    public void alElegirGrupo(Long idGrupo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioPagos.registrarPagoManual(usuarioActual.obtenerAdministrador(), idCuota, idMedio, importe):
     * Pago} (importe = {@code FilaCuota.importeAPagar}, no editable)
     *
     * <p>Casos de uso: CU-36
     * @param idCuota valor de {@code idCuota}
     * @param idMedio valor de {@code idMedio}
     * @param importe valor de {@code importe}
     */
    public void alRegistrar(Long idCuota, Long idMedio, Dinero importe) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
