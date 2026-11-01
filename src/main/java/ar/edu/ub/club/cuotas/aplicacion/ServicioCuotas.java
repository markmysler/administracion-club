package ar.edu.ub.club.cuotas.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.CuotaCubiertaConSaldoEvento;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.cuotas.dominio.TipoMovimientoSaldo;
import ar.edu.ub.club.cuotas.persistencia.CuotasDeGrupo;
import ar.edu.ub.club.cuotas.persistencia.CuotasImpagasDeGrupo;
import ar.edu.ub.club.cuotas.persistencia.CuotasPendientesVencidas;
import ar.edu.ub.club.cuotas.persistencia.MovimientosDeGrupo;
import ar.edu.ub.club.cuotas.persistencia.SaldoDeGrupo;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.grupos.persistencia.GruposVigentesSinCuota;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Fachada del módulo cuotas. La emisión mensual delega en {@code GrupoFamiliar.emitirCuota} (Creator e
 * Information Expert) y aplica el saldo a favor del grupo (RN-21); el vencimiento delega en el estado de cada
 * {@code Cuota} (State). Cuando un saldo a favor cubre una cuota publica {@code CuotaCubiertaConSaldoEvento}
 * (Observer): el módulo pagos crea el {@code PagoConSaldo}, así cuotas no depende de pagos.
 *
 * <p>Casos de uso: CU-05, CU-35, CU-36, CU-37; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
 */
@Service
@Transactional
public class ServicioCuotas {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(ServicioCuotas.class);

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code CuotasDeGrupo}, {@code GruposVigentesSinCuota}, {@code CuotasPendientesVencidas}, {@code CuotasImpagasDeGrupo}, {@code SaldoDeGrupo}, {@code MovimientosDeGrupo}; configuración de la pasarela por {@code get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela)}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Publica {@code CuotaCubiertaConSaldoEvento}. */
    private final ApplicationEventPublisher publicadorEventos;

    /** Verifica permisos y pertenencia. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /** Agregado en la implementación: abre una transacción nueva por grupo en la emisión mensual. */
    private final TransactionTemplate transaccionPorGrupo;

    /**
     * Crea la fachada (inyección por constructor).
     *
     * <p>Casos de uso: todos
     * @param fachadaPersistencia fachada de persistencia
     * @param publicadorEventos publicador de eventos de Spring
     * @param controlAcceso control de acceso
     * @param usuarioActual cuenta de la sesión
     * @param gestorTransacciones gestor de transacciones (para la transacción por grupo)
     */
    public ServicioCuotas(FachadaPersistencia fachadaPersistencia, ApplicationEventPublisher publicadorEventos,
            ControlAcceso controlAcceso, UsuarioActual usuarioActual, PlatformTransactionManager gestorTransacciones) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.publicadorEventos = publicadorEventos;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
        this.transaccionPorGrupo = new TransactionTemplate(gestorTransacciones);
        this.transaccionPorGrupo.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
    }

    /**
     * Para cada grupo vigente sin cuota del período obtiene {@code saldo =
     * fachadaPersistencia.buscarUno(SaldoDeGrupo(idGrupo))} y llama a {@code emitirCuota(periodo, fechas, saldo)},
     * que crea la cuota con sus ítems y {@code creditoAplicado = min(saldo, importe)}. Si hay crédito aplicado
     * registra un {@code MovimientoSaldo} DEBITO; si el crédito cubre todo el importe publica {@code
     * CuotaCubiertaConSaldoEvento}, que el módulo pagos atiende ({@code RegistradorPagoConSaldo}) creando el {@code
     * PagoConSaldo} aprobado y su comprobante en la misma transacción, con lo que la cuota queda PAGADA. Así cuotas
     * no depende de pagos. Cada grupo se procesa en su transacción ({@code REQUIRES_NEW}): si uno falla se registra
     * y se sigue con el resto; es idempotente por grupo y período (UNIQUE en la base y la consulta de grupos sin
     * cuota) (sec_tmp_emision_p1).
     *
     * <p>Permiso: — (tarea programada).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @param periodo Año y mes.
     * @return Cantidad de cuotas emitidas.
     */
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public int emitirCuotasDelMes(YearMonth periodo) {
        String textoPeriodo = periodo.toString();
        List<Long> idsGrupos = transaccionPorGrupo.execute(estado ->
                fachadaPersistencia.buscar(new GruposVigentesSinCuota(textoPeriodo)).stream().map(GrupoFamiliar::getId).toList());
        int emitidas = 0;
        for (Long idGrupo : idsGrupos == null ? List.<Long>of() : idsGrupos) {
            try {
                transaccionPorGrupo.executeWithoutResult(estado -> emitirCuotaDeGrupo(idGrupo, periodo));
                emitidas++;
            } catch (RuntimeException ex) {
                BITACORA.error("No se pudo emitir la cuota {} del grupo {}; se reintentará en la próxima ejecución",
                        textoPeriodo, idGrupo, ex);
            }
        }
        return emitidas;
    }

    /**
     * Invoca {@code vencer(hoy)} sobre las cuotas pendientes con vencimiento anterior a hoy (State: Pendiente →
     * Vencida) y las guarda (sec_tmp_vencer_cuotas).
     *
     * <p>Permiso: — (tarea programada).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de cuotas
     * @param hoy Fecha de proceso.
     * @return Cantidad de cuotas vencidas.
     */
    public int marcarCuotasVencidas(LocalDate hoy) {
        int vencidas = 0;
        for (Cuota c : fachadaPersistencia.buscar(new CuotasPendientesVencidas(hoy))) {
            c.vencer(hoy);
            fachadaPersistencia.put(c);
            if (c.estaVencida()) {
                vencidas++;
            }
        }
        return vencidas;
    }

    /**
     * Devuelve las cuotas del grupo ({@code fachadaPersistencia.buscar(CuotasDeGrupo(idGrupo))}) con {@code
     * creditoAplicado} e {@code importeAPagar}; si la cuenta no es de administración verifica que pertenezca al
     * grupo (RN-20). Si la pasarela admite el pago online lo informa {@code pagoOnlineHabilitado()}; el presentador
     * combina ese dato con {@code cuota.admitePago()} y la titularidad en {@code FilaCuota.admitePagoOnline}.
     *
     * <p>Permiso: {@code CONSULTAR_PERFIL_PROPIO} o {@code REGISTRAR_PAGO_MANUAL}.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-35, CU-36
     * @param idGrupo Grupo.
     * @return Lista de {@code Cuota}.
     * @throws AccesoDenegadoException si un socio consulta otro grupo
     */
    @Transactional(readOnly = true)
    public List<Cuota> consultarCuotasDeGrupo(Long idGrupo) {
        verificarAlcance(idGrupo, Permiso.REGISTRAR_PAGO_MANUAL);
        List<Cuota> cuotas = fachadaPersistencia.buscar(new CuotasDeGrupo(idGrupo));
        cuotas.forEach(c -> c.getItems().size()); // carga el Virtual Proxy de los ítems dentro de la transacción
        return cuotas;
    }

    /**
     * Devuelve el saldo a favor del grupo ({@code GrupoFamiliar.saldoAFavor()}) y sus movimientos de crédito y
     * débito; un socio solo consulta el de su grupo (RN-20) (sec_cu37_p2).
     *
     * <p>Permiso: {@code CONSULTAR_PERFIL_PROPIO} o {@code CONSULTAR_PAGOS_CLUB}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @param idGrupo Grupo.
     * @return {@code SaldoGrupo} (saldo y movimientos).
     * @throws AccesoDenegadoException si un socio consulta otro grupo
     */
    @Transactional(readOnly = true)
    public SaldoGrupo consultarSaldo(Long idGrupo) {
        verificarAlcance(idGrupo, Permiso.CONSULTAR_PAGOS_CLUB);
        GrupoFamiliar grupo = fachadaPersistencia.get(idGrupo, GrupoFamiliar.class);
        Dinero saldo = grupo.saldoAFavor();
        List<MovimientoSaldo> movimientos = fachadaPersistencia.buscar(new MovimientosDeGrupo(idGrupo));
        return new SaldoGrupo(saldo, movimientos);
    }

    /**
     * Informa si la pasarela está habilitada para el pago online: {@code
     * fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela).habilitada}. Lo usa {@code
     * PresentadorEstadoCuenta} para armar {@code FilaCuota.admitePagoOnline} (cuota que admite pago, cuenta titular
     * y pasarela habilitada).
     *
     * <p>Permiso: — (solo lectura, sin datos personales).
     *
     * <p>Casos de uso: CU-35
     * @return true si la pasarela está habilitada.
     */
    @Transactional(readOnly = true)
    public boolean pagoOnlineHabilitado() {
        ConfiguracionPasarela c = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        return Boolean.TRUE.equals(c.getHabilitada());
    }

    /**
     * Registra un {@code MovimientoSaldo} CREDITO a favor del grupo, que guarda el identificador del pago acreditado
     * (RN-21), y llama a {@code aplicarSaldoAPendientes(grupo)}. Se ejecuta dentro de la transacción del llamador
     * (sec_tmp_aplicar_saldo).
     *
     * <p>Permiso: — (llamada interna de pagos).
     *
     * <p>Casos de uso: CU-35
     * @param idGrupo Grupo beneficiario.
     * @param importe Importe del pago acreditado.
     * @param idPago Pago que origina el crédito.
     */
    public void registrarCredito(Long idGrupo, Dinero importe, Long idPago) {
        MovimientoSaldo credito = new MovimientoSaldo(TipoMovimientoSaldo.CREDITO, importe, idPago);
        credito.asignarGrupo(idGrupo);
        fachadaPersistencia.put(credito);
        aplicarSaldoAPendientes(fachadaPersistencia.get(idGrupo, GrupoFamiliar.class));
    }

    /**
     * Mientras el saldo a favor cubra por completo la cuota impaga más antigua del grupo (Pendiente o Vencida),
     * registra un {@code MovimientoSaldo} DEBITO por su importe a pagar y publica {@code
     * CuotaCubiertaConSaldoEvento}, que {@code RegistradorPagoConSaldo} atiende creando el {@code PagoConSaldo} y su
     * comprobante. El saldo restante se aplica en la emisión siguiente como crédito parcial (RN-21).
     *
     * <p>Permiso: — (llamada interna).
     *
     * <p>Casos de uso: CU-35
     * @param grupo Grupo que recibió el crédito.
     */
    public void aplicarSaldoAPendientes(GrupoFamiliar grupo) {
        Dinero saldo = fachadaPersistencia.buscarUno(new SaldoDeGrupo(grupo.getId())).orElse(Dinero.cero());
        for (Cuota c : fachadaPersistencia.buscar(new CuotasImpagasDeGrupo(grupo.getId()))) {
            Dinero importe = c.importeAPagar();
            if (!importe.esPositivo() || importe.esMayorQue(saldo)) {
                break;
            }
            fachadaPersistencia.put(new MovimientoSaldo(TipoMovimientoSaldo.DEBITO, importe, c));
            saldo = saldo.restar(importe);
            publicadorEventos.publishEvent(new CuotaCubiertaConSaldoEvento(c.getId(), grupo.getId(), importe, ahora()));
        }
    }

    /**
     * Emite la cuota de un grupo dentro de su transacción: saldo, {@code GrupoFamiliar.emitirCuota} (Creator),
     * {@code put(c)}, DEBITO del crédito aplicado y, si queda en 0, el evento de cuota cubierta.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas, emisión mensual de cuotas (aplicación del saldo a favor)
     * @param idGrupo Grupo.
     * @param periodo Período.
     */
    private void emitirCuotaDeGrupo(Long idGrupo, YearMonth periodo) {
        GrupoFamiliar g = fachadaPersistencia.get(idGrupo, GrupoFamiliar.class);
        Dinero saldo = fachadaPersistencia.buscarUno(new SaldoDeGrupo(idGrupo)).orElse(Dinero.cero());
        Cuota c = g.emitirCuota(periodo.toString(), FechasCuota.delPeriodo(periodo), saldo);
        if (c.getIdGrupo() == null) {
            c.asignarGrupo(idGrupo);
        }
        fachadaPersistencia.put(c);
        if (c.getCreditoAplicado().esPositivo()) {
            fachadaPersistencia.put(new MovimientoSaldo(TipoMovimientoSaldo.DEBITO, c.getCreditoAplicado(), c));
            if (c.importeAPagar().esCero()) {
                publicadorEventos.publishEvent(new CuotaCubiertaConSaldoEvento(c.getId(), idGrupo, c.getCreditoAplicado(), ahora()));
            }
        }
    }

    /**
     * Verifica el alcance de una consulta sobre un grupo: con el permiso de administración consulta cualquiera; si
     * no, exige CONSULTAR_PERFIL_PROPIO y que el grupo sea el del socio de la sesión (RN-20). No compara tipos de
     * cuenta.
     *
     * <p>Casos de uso: CU-05, CU-35, CU-36, CU-37
     * @param idGrupo Grupo consultado.
     * @param permisoAdministracion Permiso que habilita cualquier grupo.
     * @throws AccesoDenegadoException si no corresponde
     */
    private void verificarAlcance(Long idGrupo, Permiso permisoAdministracion) {
        Usuario usuario = usuarioActual.obtener();
        if (controlAcceso.tienePermiso(usuario, permisoAdministracion)) {
            return;
        }
        controlAcceso.verificar(usuario, Permiso.CONSULTAR_PERFIL_PROPIO);
        GrupoFamiliar propio = fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(usuario.getId())).orElse(null);
        if (propio == null || !propio.getId().equals(idGrupo)) {
            throw new AccesoDenegadoException("El grupo no es el del socio de la sesión");
        }
    }

    /**
     * Momento actual en la zona del club (la fachada no tiene {@code Reloj} en el diseño).
     *
     * <p>Casos de uso: CU-35; tareas programadas: emisión mensual de cuotas (aplicación del saldo a favor)
     * @return ahora
     */
    private static LocalDateTime ahora() {
        return LocalDateTime.now(RelojSistema.ZONA_CLUB);
    }
}
