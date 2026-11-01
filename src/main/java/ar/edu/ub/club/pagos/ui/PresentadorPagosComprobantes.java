package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.pagos.aplicacion.FiltroPagos;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-28: Listado de pagos con estado (incluidos los ACREDITADOS) y acceso al
 * comprobante, y saldo a favor del grupo.
 *
 * <p>Casos de uso: CU-37
 */
public class PresentadorPagosComprobantes extends PresentadorBase<PantallaPagosComprobantes> {

    /** Atributo servicioPagos. */
    private ServicioPagos servicioPagos;

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo servicioCuotas. */
    private ServicioCuotas servicioCuotas;

    /** Atributo servicioGruposFamiliares. */
    private ServicioGruposFamiliares servicioGruposFamiliares;

    /** Atributo usuarioActual. */
    private UsuarioActual usuarioActual;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /** Atributo controlAcceso. */
    private ControlAcceso controlAcceso;

    /**
     * {@code ServicioPagos.consultarPagos(usuarioActual.obtener(), filtro): List&lt;Pago&gt;} → {@code
     * FilaPago.desde(p)}; si la cuenta tiene CONSULTAR_PAGOS_PROPIOS, {@code
     * ServicioSocios.consultarPerfilPropio(socio)} para el grupo y {@code cargarSaldo(idGrupo)}
     *
     * <p>Casos de uso: CU-37
     * @param filtro valor de {@code filtro}
     */
    public void cargar(FiltroPagos filtro) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioCuotas.consultarSaldo(idGrupo)} → {@code ResumenSaldo}
     *
     * <p>Casos de uso: CU-37
     * @param idGrupo valor de {@code idGrupo}
     */
    public void cargarSaldo(Long idGrupo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, null))} → {@code FilaGrupo}
     * (filtro del administrador)
     *
     * <p>Casos de uso: CU-37
     * @param texto valor de {@code texto}
     */
    public void alBuscarGrupo(String texto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("pagos/comprobante/" + idComprobante)}
     *
     * <p>Casos de uso: CU-37
     * @param idComprobante valor de {@code idComprobante}
     */
    public void alVerComprobante(Long idComprobante) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
