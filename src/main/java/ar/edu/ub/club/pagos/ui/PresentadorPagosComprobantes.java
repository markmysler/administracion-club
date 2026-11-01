package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.grupos.aplicacion.CriterioBusquedaGrupo;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.grupos.ui.FilaGrupo;
import ar.edu.ub.club.pagos.aplicacion.FiltroPagos;
import ar.edu.ub.club.pagos.aplicacion.ServicioPagos;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

/**
 * Presentador de la pantalla P-28 Pagos y comprobantes (MVP Vista Pasiva; no usa tipos de Vaadin). Lista los pagos
 * con {@code ServicioPagos.consultarPagos} pasando la cuenta de la sesión (el alcance lo decide la fachada, RN-20),
 * muestra el saldo a favor del grupo con {@code ServicioCuotas.consultarSaldo} y, para el administrador, sugiere
 * grupos para el filtro. Diagramas sec_cu37_p1, sec_cu37_p2 y sec_cu37_p3.
 *
 * <p>Casos de uso: CU-05, CU-37
 */
@SpringComponent
@UIScope
public class PresentadorPagosComprobantes extends PresentadorBase<PantallaPagosComprobantes> {

    /** Fachada de pagos. */
    private final ServicioPagos servicioPagos;

    /** Fachada de socios (grupo del socio). */
    private final ServicioSocios servicioSocios;

    /** Fachada de cuotas (saldo a favor). */
    private final ServicioCuotas servicioCuotas;

    /** Fachada de grupos (filtro del administrador). */
    private final ServicioGruposFamiliares servicioGruposFamiliares;

    /** Cuenta de la sesión. */
    private final UsuarioActual usuarioActual;

    /** Socio de la sesión. */
    private final SocioActual socioActual;

    /** Verifica qué permiso de consulta tiene la cuenta. */
    private final ControlAcceso controlAcceso;

    /**
     * Crea el presentador con sus dependencias.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param servicioPagos fachada de pagos
     * @param servicioSocios fachada de socios
     * @param servicioCuotas fachada de cuotas
     * @param servicioGruposFamiliares fachada de grupos
     * @param usuarioActual cuenta de la sesión
     * @param socioActual socio de la sesión
     * @param controlAcceso control de acceso
     */
    public PresentadorPagosComprobantes(ServicioPagos servicioPagos, ServicioSocios servicioSocios,
                                        ServicioCuotas servicioCuotas,
                                        ServicioGruposFamiliares servicioGruposFamiliares,
                                        UsuarioActual usuarioActual, SocioActual socioActual,
                                        ControlAcceso controlAcceso) {
        this.servicioPagos = servicioPagos;
        this.servicioSocios = servicioSocios;
        this.servicioCuotas = servicioCuotas;
        this.servicioGruposFamiliares = servicioGruposFamiliares;
        this.usuarioActual = usuarioActual;
        this.socioActual = socioActual;
        this.controlAcceso = controlAcceso;
    }

    /**
     * Carga el listado (pasos 1 a 4, 6 y 7): {@code ServicioPagos.consultarPagos(usuarioActual.obtener(), filtro)} →
     * {@code FilaPago.desde(p)}. Si la cuenta tiene CONSULTAR_PAGOS_PROPIOS (socio), oculta el filtro por grupo y
     * carga el saldo de su grupo con {@code ServicioSocios.consultarPerfilPropio(socio)} y {@code cargarSaldo};
     * si es administrador y filtró un grupo, carga el saldo de ese grupo (paso 5).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param filtro período, estado y grupo (este último solo del administrador)
     */
    public void cargar(FiltroPagos filtro) {
        Usuario usuario = usuarioActual.obtener();
        boolean esSocio = controlAcceso.tienePermiso(usuario, Permiso.CONSULTAR_PAGOS_PROPIOS);
        pantalla.mostrarFiltroGrupo(!esSocio);
        consultar(() -> servicioPagos.consultarPagos(usuario, filtro)).ifPresent(pagos -> {
            List<FilaPago> filas = pagos.stream().map(FilaPago::desde).toList();
            pantalla.mostrarPagos(filas);
            if (filas.isEmpty()) {
                pantalla.mostrarMensaje(sinFiltro(filtro) ? "No hay pagos registrados"
                        : "Ningún pago cumple el filtro");
            }
        });
        if (esSocio) {
            consultar(() -> servicioSocios.consultarPerfilPropio(socioActual.obtener()))
                    .map(perfil -> perfil.getGrupo() == null ? null : perfil.getGrupo().getId())
                    .ifPresent(this::cargarSaldo);
        } else if (filtro != null && filtro.getIdGrupo() != null) {
            cargarSaldo(filtro.getIdGrupo());
        } else {
            pantalla.mostrarSaldo(null);
        }
    }

    /**
     * Carga el saldo a favor del grupo (paso 5, sec_cu37_p2): {@code ServicioCuotas.consultarSaldo(idGrupo)} →
     * {@code ResumenSaldo.desde} → {@code pantalla.mostrarSaldo}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param idGrupo grupo familiar
     */
    public void cargarSaldo(Long idGrupo) {
        consultar(() -> servicioCuotas.consultarSaldo(idGrupo))
                .map(ResumenSaldo::desde)
                .ifPresent(pantalla::mostrarSaldo);
    }

    /**
     * Sugiere grupos para el filtro del administrador: {@code ServicioGruposFamiliares.buscarGrupos(new
     * CriterioBusquedaGrupo(texto, null))} → {@code FilaGrupo.desde}.
     *
     * <p>Casos de uso: CU-37
     * @param texto DNI o nombre del titular o de un integrante
     */
    public void alBuscarGrupo(String texto) {
        consultar(() -> servicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, null)))
                .map(resumenes -> resumenes.stream().map(FilaGrupo::desde).toList())
                .ifPresent(filas -> {
                    pantalla.mostrarGrupos(filas);
                    if (filas.isEmpty()) {
                        pantalla.mostrarMensaje("No hay coincidencias");
                    }
                });
    }

    /**
     * Abre el comprobante o la constancia (paso 8): {@code navegarA("pagos/comprobante/" + idComprobante)}. Un pago
     * sin comprobante (pendiente, rechazado o vencido) no lo tiene (FA4, RN-17).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param idComprobante id del comprobante
     */
    public void alVerComprobante(Long idComprobante) {
        if (idComprobante == null) {
            pantalla.mostrarMensaje("Sin comprobante: solo los pagos aprobados o acreditados lo tienen");
            return;
        }
        pantalla.navegarA("pagos/comprobante/" + idComprobante);
    }

    /**
     * Indica si el filtro no restringe nada (para elegir el mensaje de FA1 o FA2).
     *
     * <p>Casos de uso: CU-37
     * @param filtro filtro de pagos
     * @return {@code true} si no tiene ningún criterio
     */
    private boolean sinFiltro(FiltroPagos filtro) {
        return filtro == null || (filtro.getDesde() == null && filtro.getHasta() == null
                && filtro.getPeriodo() == null && filtro.getEstado() == null && filtro.getIdGrupo() == null);
    }
}
