package ar.edu.ub.club.pagos.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Administrador;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.pagos.dominio.Comprobante;
import ar.edu.ub.club.pagos.dominio.ConfiguracionPasarela;
import ar.edu.ub.club.pagos.dominio.DatosPago;
import ar.edu.ub.club.pagos.dominio.EstadoPagoPendiente;
import ar.edu.ub.club.pagos.dominio.GeneradorComprobante;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import ar.edu.ub.club.pagos.dominio.ModoPasarela;
import ar.edu.ub.club.pagos.dominio.Pago;
import ar.edu.ub.club.pagos.dominio.PagoOnline;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoManual;
import ar.edu.ub.club.pagos.dominio.ProcesadorPagoOnline;
import ar.edu.ub.club.pagos.integracion.IntencionPago;
import ar.edu.ub.club.pagos.integracion.MercadoPagoAdapter;
import ar.edu.ub.club.pagos.integracion.PasarelaPago;
import ar.edu.ub.club.pagos.integracion.ResultadoPasarela;
import ar.edu.ub.club.pagos.integracion.SolicitudPagoOnline;
import ar.edu.ub.club.pagos.persistencia.MedioPagoPorNombre;
import ar.edu.ub.club.pagos.persistencia.MediosPago;
import ar.edu.ub.club.pagos.persistencia.MediosPagoHabilitados;
import ar.edu.ub.club.pagos.persistencia.PagoPorReferenciaExterna;
import ar.edu.ub.club.pagos.persistencia.PagosDeGrupo;
import ar.edu.ub.club.pagos.persistencia.PagosOnlinePendientesAnterioresA;
import ar.edu.ub.club.pagos.persistencia.PagosPorFiltro;
import ar.edu.ub.club.socios.dominio.Socio;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada del módulo pagos. Usa Factory Method ({@code ProcesadorPagoOnline}, {@code ProcesadorPagoManual};
 * {@code ProcesadorPagoConSaldo} lo usa el observador {@code RegistradorPagoConSaldo}) para crear los pagos,
 * {@code GeneradorComprobante} para la numeración correlativa y el puerto {@code PasarelaPago} (Adapter) para la
 * pasarela. Garantiza la idempotencia de la confirmación por webhook.
 *
 * <p>Adaptador activo: según {@code ConfiguracionPasarela.modo} (leído en cada operación), {@code pasarela}
 * (el adaptador simulado, bean {@code @Primary}) en modo SIMULADA o {@code pasarelaMercadoPago} en modo
 * MERCADO_PAGO ({@code pasarelaActiva}).
 *
 * <p>Casos de uso: CU-34, CU-35, CU-36, CU-37; tareas programadas: vencimiento de pagos online pendientes
 */
@Service
@Transactional
public class ServicioPagos {

    /** Bitácora técnica. */
    private static final Logger BITACORA = LoggerFactory.getLogger(ServicioPagos.class);

    /** SQLState de PostgreSQL para la violación de una restricción única. */
    private static final String VIOLACION_UNICA = "23505";

    /** Validez de la intención de pago (RN-22). */
    private static final long HORAS_VIGENCIA_INTENCION = 24;

    /** Motivo del vencimiento de un pago online pendiente (RN-22). */
    static final String MOTIVO_ABANDONADO = "Abandonado";

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code PagoPorReferenciaExterna} (FOR UPDATE), {@code PagosPorFiltro}, {@code PagosDeGrupo}, {@code PagosOnlinePendientesAnterioresA}, {@code MediosPago}, {@code MediosPagoHabilitados}, {@code MedioPagoPorNombre}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Adaptador activo en modo SIMULADA (bean {@code @Primary} {@code PasarelaSimuladaAdapter}). */
    private final PasarelaPago pasarela;

    /** Agregado en la implementación: adaptador de Mercado Pago, activo en modo MERCADO_PAGO. */
    private final MercadoPagoAdapter pasarelaMercadoPago;

    /** Creador de pagos online. */
    private final ProcesadorPagoOnline procesadorOnline;

    /** Creador de pagos manuales. */
    private final ProcesadorPagoManual procesadorManual;

    /** Crea comprobantes. */
    private final GeneradorComprobante generadorComprobante;

    /** Fachada de cuotas: registra el crédito de un pago acreditado. */
    private final ServicioCuotas servicioCuotas;

    /** Verifica permisos y pertenencia. */
    private final ControlAcceso controlAcceso;

    /** Auditoría de pagos. */
    private final ServicioAuditoria servicioAuditoria;

    /** Fecha y hora. */
    private final Reloj reloj;

    /** Agregado en la implementación: cuenta de la sesión para CU-34 (las firmas no reciben la cuenta). */
    private final UsuarioActual usuarioActual;

    /** Agregado en la implementación: URL pública del sistema ({@code club.url-base}) para las URL de retorno y notificación. */
    private final String urlBase;

    /**
     * Crea la fachada con sus colaboradores (inyección por constructor).
     *
     * <p>Casos de uso: CU-34, CU-35, CU-36, CU-37
     * @param fachadaPersistencia fachada de persistencia
     * @param pasarela adaptador simulado (primario)
     * @param pasarelaMercadoPago adaptador de Mercado Pago
     * @param procesadorOnline creador de pagos online
     * @param procesadorManual creador de pagos manuales
     * @param generadorComprobante generador de comprobantes
     * @param servicioCuotas fachada de cuotas
     * @param controlAcceso control de acceso
     * @param servicioAuditoria auditoría
     * @param reloj reloj del club
     * @param usuarioActual cuenta de la sesión
     * @param urlBase URL pública del sistema
     */
    public ServicioPagos(FachadaPersistencia fachadaPersistencia, PasarelaPago pasarela, MercadoPagoAdapter pasarelaMercadoPago,
            ProcesadorPagoOnline procesadorOnline, ProcesadorPagoManual procesadorManual,
            GeneradorComprobante generadorComprobante, ServicioCuotas servicioCuotas, ControlAcceso controlAcceso,
            ServicioAuditoria servicioAuditoria, Reloj reloj, UsuarioActual usuarioActual,
            @Value("${club.url-base:http://localhost:8080}") String urlBase) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.pasarela = pasarela;
        this.pasarelaMercadoPago = pasarelaMercadoPago;
        this.procesadorOnline = procesadorOnline;
        this.procesadorManual = procesadorManual;
        this.generadorComprobante = generadorComprobante;
        this.servicioCuotas = servicioCuotas;
        this.controlAcceso = controlAcceso;
        this.servicioAuditoria = servicioAuditoria;
        this.reloj = reloj;
        this.usuarioActual = usuarioActual;
        this.urlBase = urlBase.endsWith("/") ? urlBase.substring(0, urlBase.length() - 1) : urlBase;
    }

    /**
     * Devuelve medios y configuración de la pasarela ({@code buscar(MediosPago())} y {@code
     * get(ConfiguracionPasarela.ID_UNICO, ...)}, sec_cu34_p1).
     *
     * <p>Permiso: {@code CONFIGURAR_PAGOS} o {@code REGISTRAR_PAGO_MANUAL}.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return {@code ConfiguracionPagos}.
     */
    @Transactional(readOnly = true)
    public ConfiguracionPagos consultarMediosYPasarela() {
        Usuario usuario = usuarioActual.obtener();
        if (!controlAcceso.tienePermiso(usuario, Permiso.REGISTRAR_PAGO_MANUAL)) {
            controlAcceso.verificar(usuario, Permiso.CONFIGURAR_PAGOS);
        }
        List<MedioPago> medios = fachadaPersistencia.buscar(new MediosPago());
        ConfiguracionPasarela c = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        return new ConfiguracionPagos(medios, c);
    }

    /**
     * Devuelve los medios de pago manuales habilitados ({@code fachadaPersistencia.buscar(MediosPagoHabilitados())}),
     * los únicos que pueden usarse al registrar un pago manual (CU-36, paso 4).
     *
     * <p>Permiso: {@code REGISTRAR_PAGO_MANUAL}.
     *
     * <p>Casos de uso: CU-36
     * @return lista de {@code MedioPago} habilitados, ordenados por nombre
     */
    @Transactional(readOnly = true)
    public List<MedioPago> listarMediosHabilitados() {
        controlAcceso.verificar(usuarioActual.obtener(), Permiso.REGISTRAR_PAGO_MANUAL);
        return fachadaPersistencia.buscar(new MediosPagoHabilitados());
    }

    /**
     * Actualiza medios habilitados (y da de alta los nuevos, FA1); si se habilita la pasarela exige {@code
     * pasarela.validarConfiguracion(config) = true}; audita (sec_cu34_p3).
     *
     * <p>Permiso: {@code CONFIGURAR_PAGOS}.
     *
     * <p>Casos de uso: CU-34
     * @param datos Medios y configuración de la pasarela.
     * @throws ErrorNegocio si un medio nuevo repite un nombre (FA1) o no tiene nombre (FA2)
     */
    public void configurarMediosYPasarela(DatosConfiguracionPagos datos) {
        Usuario admin = usuarioActual.obtener();
        controlAcceso.verificar(admin, Permiso.CONFIGURAR_PAGOS);
        for (DatosMedioPago d : datos.getMedios()) {
            if (d.getId() == null) {
                String nombre = d.getNombre() == null ? "" : d.getNombre().trim();
                if (nombre.isEmpty()) {
                    throw new ErrorNegocio("nombre", "Ingrese el nombre del medio de pago.");
                }
                if (fachadaPersistencia.contar(new MedioPagoPorNombre(nombre)) > 0) {
                    throw new ErrorNegocio("nombre", "Ya existe un medio de pago con el nombre " + nombre + ".");
                }
                MedioPago nuevo = new MedioPago(nombre);
                nuevo.cambiarHabilitado(d.getHabilitado());
                fachadaPersistencia.put(nuevo);
            } else {
                MedioPago medio = fachadaPersistencia.get(d.getId(), MedioPago.class);
                medio.cambiarHabilitado(d.getHabilitado());
                fachadaPersistencia.put(medio);
            }
        }
        ConfiguracionPasarela c = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        ModoPasarela modo = datos.getModo() == null ? ModoPasarela.SIMULADA : datos.getModo();
        boolean credenciales = modo == ModoPasarela.SIMULADA || pasarelaMercadoPago.tieneCredenciales();
        boolean habilitada = false;
        if (datos.getHabilitada() && credenciales) {
            c.configurar(modo, true, credenciales);
            habilitada = adaptadorDe(modo).validarConfiguracion(c);
        }
        c.configurar(modo, habilitada, credenciales);
        fachadaPersistencia.put(c);
        servicioAuditoria.registrar(admin, "MODIFICACION", "ConfiguracionPasarela", c.getId(),
                "{\"modo\": \"" + modo + "\", \"habilitada\": " + habilitada + ", \"medios\": " + datos.getMedios().size() + "}");
        if (datos.getHabilitada() && !habilitada) {
            BITACORA.info("La pasarela {} no se habilitó: credenciales ausentes o configuración rechazada", modo);
        }
    }

    /**
     * Verifica pasarela habilitada, que el socio sea titular del grupo de la cuota y {@code cuota.admitePago()};
     * {@code procesadorOnline.registrar(cuota, datos)} crea el {@code PagoOnline} Pendiente por {@code
     * importeAPagar} con referencia externa única; pide {@code pasarela.crearIntencionPago} con vencimiento de 24
     * horas (RN-22). Si la pasarela falla al crear la intención, {@code pago.rechazar(motivo)} y lanza {@code
     * ErrorNegocio} sin revertir el rechazo ({@code &#64;Transactional(noRollbackFor = ErrorNegocio.class)}).
     *
     * <p>Permiso: {@code PAGAR_CUOTA_ONLINE}.
     *
     * <p>Casos de uso: CU-35
     * @param titular Socio de la sesión.
     * @param idCuota Cuota a pagar.
     * @return {@code IntencionPago} (idPreferencia, urlCheckout).
     * @throws AccesoDenegadoException si el socio no es el titular del grupo de la cuota (FA1, FA4; RN-09, RN-20)
     * @throws ErrorNegocio si la pasarela está deshabilitada (FA2), la cuota ya fue pagada (FA5) o la pasarela falla (FA6)
     */
    @Transactional(noRollbackFor = ErrorNegocio.class)
    public IntencionPago iniciarPagoOnline(Socio titular, Long idCuota) {
        controlAcceso.verificar(titular, Permiso.PAGAR_CUOTA_ONLINE);
        ConfiguracionPasarela config = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        if (!Boolean.TRUE.equals(config.getHabilitada())) {
            throw new ErrorNegocio("El pago online no está disponible; puede pagar la cuota en la administración del club.");
        }
        Cuota cuota = fachadaPersistencia.get(idCuota, Cuota.class);
        GrupoFamiliar grupo = fachadaPersistencia.get(cuota.getIdGrupo(), GrupoFamiliar.class);
        if (grupo.getTitular() == null || !grupo.getTitular().getId().equals(titular.getId())) {
            throw new AccesoDenegadoException("Solo el titular del grupo puede pagar sus cuotas online");
        }
        if (!cuota.admitePago()) {
            throw new ErrorNegocio("La cuota ya fue pagada.");
        }
        LocalDateTime ahora = reloj.ahora();
        PagoOnline pago = (PagoOnline) procesadorOnline.registrar(cuota,
                new DatosPago(cuota.importeAPagar(), ahora, null, null, titular));
        SolicitudPagoOnline solicitud = new SolicitudPagoOnline(pago.getReferenciaExterna(),
                "Cuota societaria " + cuota.getPeriodo(), pago.getImporte(),
                urlBase + "/pagos/retorno?referencia=" + pago.getReferenciaExterna(),
                urlBase + "/api/pasarela/notificaciones", ahora.plusHours(HORAS_VIGENCIA_INTENCION));
        try {
            return pasarelaActiva(config).crearIntencionPago(solicitud);
        } catch (RuntimeException ex) {
            BITACORA.warn("La pasarela no creó la intención del pago {}", pago.getId(), ex);
            pago.rechazar("Falla al crear la intención de pago");
            fachadaPersistencia.put(pago);
            servicioAuditoria.registrar(titular, "PAGO_RECHAZADO", "Pago", pago.getId(),
                    "{\"motivo\": \"Falla al crear la intención de pago\"}");
            throw new ErrorNegocio("La pasarela de pago no está disponible en este momento; intente más tarde.");
        }
    }

    /**
     * Consulta {@code pasarela.obtenerResultado(idPagoPasarela)} y busca el pago por referencia externa con bloqueo
     * de fila; {@code pago.registrarRespuestaPasarela(resultado)}. Si el pago ya está APROBADO, RECHAZADO o
     * ACREDITADO termina sin cambios (idempotencia). Si está Pendiente o VENCIDO y la pasarela informa aprobado: con
     * {@code cuota.admitePago()}, {@code aprobar(ahora)}, {@code cuota.registrarPagoAprobado(pago)} y comprobante;
     * con la cuota ya pagada, {@code acreditar(ahora)}, {@code servicioCuotas.registrarCredito(idGrupo, importe,
     * idPago)} y constancia de acreditación (RN-21). Si informa rechazado: {@code rechazar(motivo)} (solo desde
     * Pendiente). Ningún pago cobrado queda sin registrar. Un pago desconocido o con importe distinto se audita
     * como ANOMALIA_PAGO (FA11). Lo invocan {@code WebhookPasarelaController} y, en modo SIMULADA, {@code
     * PasarelaSimuladaAdapter.resolverPago}.
     *
     * <p>Permiso: — (sistema externo).
     *
     * <p>Casos de uso: CU-35
     * @param idPagoPasarela Identificador del pago en la pasarela.
     */
    public void confirmarResultadoPasarela(String idPagoPasarela) {
        ConfiguracionPasarela config = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        ResultadoPasarela resultado = pasarelaActiva(config).obtenerResultado(idPagoPasarela);
        if (resultado == null) {
            servicioAuditoria.registrar(null, "ANOMALIA_PAGO", "Pago", null,
                    "{\"idPagoPasarela\": \"" + idPagoPasarela + "\", \"detalle\": \"pago desconocido en la pasarela\"}");
            return;
        }
        if (!resultado.esAprobado() && !resultado.esRechazado()) {
            return; // FA9: sigue pendiente en la pasarela; llegará otra notificación
        }
        Optional<PagoOnline> encontrado = resultado.getReferenciaExterna() == null ? Optional.empty()
                : fachadaPersistencia.buscarUno(new PagoPorReferenciaExterna(resultado.getReferenciaExterna(), true));
        if (encontrado.isEmpty() || (resultado.getImporte() != null && !resultado.getImporte().equals(encontrado.get().getImporte()))) {
            servicioAuditoria.registrar(null, "ANOMALIA_PAGO", "Pago", encontrado.map(Pago::getId).orElse(null),
                    "{\"idPagoPasarela\": \"" + idPagoPasarela + "\", \"detalle\": \"pago inexistente o importe distinto\"}");
            return;
        }
        PagoOnline pago = encontrado.get();
        if (pago.getEstado().esFinal()) {
            return; // FA10: notificación repetida (idempotencia, RN-18)
        }
        aplicarResultado(pago, resultado, reloj.ahora());
    }

    /**
     * Devuelve el pago si pertenece al grupo del socio (RN-20). Lo consulta P-26 cada 3 segundos.
     *
     * <p>Permiso: {@code PAGAR_CUOTA_ONLINE}.
     *
     * <p>Casos de uso: CU-35
     * @param usuario Cuenta de la sesión.
     * @param referenciaExterna Referencia del pago.
     * @return {@code PagoOnline}.
     * @throws RecursoNoEncontradoException si no hay pago con esa referencia
     * @throws AccesoDenegadoException si el pago es de otro grupo
     */
    @Transactional(readOnly = true)
    public PagoOnline consultarPagoOnline(Usuario usuario, UUID referenciaExterna) {
        controlAcceso.verificar(usuario, Permiso.PAGAR_CUOTA_ONLINE);
        PagoOnline pago = fachadaPersistencia.buscarUno(new PagoPorReferenciaExterna(referenciaExterna, false))
                .orElseThrow(() -> new RecursoNoEncontradoException(PagoOnline.class, referenciaExterna));
        GrupoFamiliar grupo = grupoDe(usuario);
        if (grupo == null || !grupo.getId().equals(pago.getCuota().getIdGrupo())) {
            throw new AccesoDenegadoException("El pago no pertenece al grupo del socio");
        }
        return pago;
    }

    /**
     * Si otro pago aprobado de la misma cuota se guardó entretanto, el índice único parcial de la base lo rechaza y
     * se traduce a {@code ErrorNegocio("La cuota ya fue pagada")} (CU-36 FA8). Verifica medio habilitado, {@code
     * admitePago()} e importe igual a {@code importeAPagar} (sin pagos parciales); {@code
     * procesadorManual.registrar} crea el {@code PagoManual} aprobado con la fecha actual; registra el pago en la
     * cuota, genera el comprobante y audita (sec_cu36_p3).
     *
     * <p>Permiso: {@code REGISTRAR_PAGO_MANUAL}.
     *
     * <p>Casos de uso: CU-36
     * @param admin Administrador que registra.
     * @param idCuota Cuota.
     * @param idMedio Medio de pago.
     * @param importe Importe recibido.
     * @return {@code Pago} aprobado con comprobante.
     * @throws ErrorNegocio si el medio no está habilitado (FA5), el importe difiere (FA6, RN-08) o la cuota ya fue pagada (FA7, FA8)
     */
    public Pago registrarPagoManual(Administrador admin, Long idCuota, Long idMedio, Dinero importe) {
        controlAcceso.verificar(admin, Permiso.REGISTRAR_PAGO_MANUAL);
        MedioPago medio = fachadaPersistencia.get(idMedio, MedioPago.class);
        if (!Boolean.TRUE.equals(medio.getHabilitado())) {
            throw new ErrorNegocio("medio", "El medio de pago " + medio.getNombre() + " no está habilitado.");
        }
        Cuota cuota = fachadaPersistencia.get(idCuota, Cuota.class);
        if (importe == null || !importe.equals(cuota.importeAPagar())) {
            throw new ErrorNegocio("importe", "El importe debe ser igual al importe a pagar de la cuota ("
                    + cuota.importeAPagar().getMonto().toPlainString() + "); no se admiten pagos parciales.");
        }
        if (!cuota.admitePago()) {
            throw new ErrorNegocio("La cuota ya fue pagada.");
        }
        Pago pago;
        try {
            pago = procesadorManual.registrar(cuota, new DatosPago(importe, reloj.ahora(), medio, admin, null));
        } catch (IllegalStateException ex) {
            if (esViolacionUnica(ex)) {
                throw new ErrorNegocio("La cuota ya fue pagada.");
            }
            throw ex;
        }
        cuota.registrarPagoAprobado(pago);
        fachadaPersistencia.put(cuota);
        generadorComprobante.generar(pago);
        servicioAuditoria.registrar(admin, "PAGO", "Pago", pago.getId(),
                "{\"medio\": \"" + medio.getNombre().replace("\"", "'") + "\", \"cuota\": " + cuota.getId() + "}");
        return pago;
    }

    /**
     * El alcance se decide con {@code usuario.obtenerPermisos()}: con CONSULTAR_PAGOS_CLUB, {@code
     * fachadaPersistencia.buscar(PagosPorFiltro(filtro))}; con solo CONSULTAR_PAGOS_PROPIOS, {@code
     * fachadaPersistencia.buscar(PagosDeGrupo(idGrupo, filtro))} del grupo del socio (RN-20). No compara tipos de
     * cuenta.
     *
     * <p>Permiso: {@code CONSULTAR_PAGOS_CLUB} o {@code CONSULTAR_PAGOS_PROPIOS}.
     *
     * <p>Casos de uso: CU-37
     * @param usuario Cuenta de la sesión.
     * @param filtro Rango, estado y grupo.
     * @return Lista de {@code Pago}.
     * @throws ErrorNegocio si «desde» es posterior a «hasta» (FA2)
     */
    @Transactional(readOnly = true)
    public List<Pago> consultarPagos(Usuario usuario, FiltroPagos filtro) {
        FiltroPagos f = filtro == null ? new FiltroPagos() : filtro;
        if (f.getDesde() != null && f.getHasta() != null && f.getDesde().isAfter(f.getHasta())) {
            throw new ErrorNegocio("desde", "La fecha desde no puede ser posterior a la fecha hasta.");
        }
        if (controlAcceso.tienePermiso(usuario, Permiso.CONSULTAR_PAGOS_CLUB)) {
            return fachadaPersistencia.buscar(new PagosPorFiltro(f));
        }
        controlAcceso.verificar(usuario, Permiso.CONSULTAR_PAGOS_PROPIOS);
        GrupoFamiliar grupo = grupoDe(usuario);
        if (grupo == null) {
            return List.of();
        }
        return fachadaPersistencia.buscar(new PagosDeGrupo(grupo.getId(), f));
    }

    /**
     * Devuelve el comprobante si la cuenta tiene acceso: el administrador cualquiera; el socio solo los de su grupo
     * (RN-20, FA3).
     *
     * <p>Permiso: {@code CONSULTAR_PAGOS_CLUB} o {@code CONSULTAR_PAGOS_PROPIOS}.
     *
     * <p>Casos de uso: CU-37
     * @param usuario Cuenta de la sesión.
     * @param idComprobante Comprobante.
     * @return {@code Comprobante}.
     * @throws AccesoDenegadoException si el comprobante es de otro grupo
     */
    @Transactional(readOnly = true)
    public Comprobante obtenerComprobante(Usuario usuario, Long idComprobante) {
        Comprobante c = fachadaPersistencia.get(idComprobante, Comprobante.class);
        if (controlAcceso.tienePermiso(usuario, Permiso.CONSULTAR_PAGOS_CLUB)) {
            return c;
        }
        controlAcceso.verificar(usuario, Permiso.CONSULTAR_PAGOS_PROPIOS);
        GrupoFamiliar grupo = grupoDe(usuario);
        if (grupo == null || !grupo.getId().equals(c.getPago().getCuota().getIdGrupo())) {
            throw new AccesoDenegadoException("El comprobante no pertenece al grupo del socio");
        }
        return c;
    }

    /**
     * Para cada {@code PagoOnline} Pendiente creado hace más de 24 horas ({@code
     * fachadaPersistencia.buscar(PagosOnlinePendientesAnterioresA(ahora − 24 h))}) consulta primero {@code
     * pasarela.obtenerResultado}: si ya fue aprobado lo procesa como {@code confirmarResultadoPasarela}; si fue
     * rechazado lo rechaza; si no, {@code pago.vencer("Abandonado")} lo pasa a VENCIDO (RN-22). VENCIDO no es
     * final: una aprobación posterior se procesa igual. La cuota admite un nuevo pago. Audita con usuario nulo
     * (sec_tmp_vencer_pagos).
     *
     * <p>Permiso: — (tarea programada).
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param ahora Momento de proceso.
     * @return Cantidad de pagos vencidos.
     */
    public int vencerPagosPendientes(LocalDateTime ahora) {
        ConfiguracionPasarela config = fachadaPersistencia.get(ConfiguracionPasarela.ID_UNICO, ConfiguracionPasarela.class);
        PasarelaPago activa = pasarelaActiva(config);
        int vencidos = 0;
        for (PagoOnline p : fachadaPersistencia.buscar(new PagosOnlinePendientesAnterioresA(ahora.minusHours(HORAS_VIGENCIA_INTENCION)))) {
            ResultadoPasarela resultado = consultarSinFallar(activa, p.getIdPagoPasarela());
            if (resultado != null && (resultado.esAprobado() || resultado.esRechazado())) {
                aplicarResultado(p, resultado, ahora);
            } else {
                p.vencer(MOTIVO_ABANDONADO);
                fachadaPersistencia.put(p);
                servicioAuditoria.registrar(null, "VENCIMIENTO", "Pago", p.getId(), "{\"motivo\": \"" + MOTIVO_ABANDONADO + "\"}");
                vencidos++;
            }
        }
        return vencidos;
    }

    /**
     * Aplica el resultado APROBADO o RECHAZADO a un pago Pendiente o VENCIDO (sec_cu35_p8): aprobado con la cuota
     * impaga → aprobar, cuota PAGADA y comprobante; aprobado con la cuota ya pagada → acreditar, constancia y
     * crédito a favor del grupo (RN-21); rechazado → rechazar solo desde Pendiente. Audita sin usuario.
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param pago Pago bloqueado.
     * @param resultado Resultado consultado.
     * @param ahora Momento de proceso.
     */
    private void aplicarResultado(PagoOnline pago, ResultadoPasarela resultado, LocalDateTime ahora) {
        pago.registrarRespuestaPasarela(resultado);
        Cuota cuota = pago.getCuota();
        if (resultado.esAprobado()) {
            if (cuota.admitePago()) {
                pago.aprobar(ahora);
                fachadaPersistencia.put(pago);
                cuota.registrarPagoAprobado(pago);
                fachadaPersistencia.put(cuota);
                generadorComprobante.generar(pago);
                servicioAuditoria.registrar(null, "PAGO_APROBADO", "Pago", pago.getId(),
                        "{\"idPagoPasarela\": \"" + pago.getIdPagoPasarela() + "\"}");
            } else {
                pago.acreditar(ahora);
                fachadaPersistencia.put(pago);
                generadorComprobante.generar(pago);
                servicioCuotas.registrarCredito(cuota.getIdGrupo(), pago.getImporte(), pago.getId());
                servicioAuditoria.registrar(null, "PAGO_ACREDITADO", "Pago", pago.getId(),
                        "{\"idPagoPasarela\": \"" + pago.getIdPagoPasarela() + "\", \"motivo\": \"cuota ya pagada\"}");
            }
        } else if (pago.getEstado() instanceof EstadoPagoPendiente) {
            String motivo = resultado.getMotivo() == null ? "Rechazado por la pasarela" : resultado.getMotivo();
            pago.rechazar(motivo);
            fachadaPersistencia.put(pago);
            servicioAuditoria.registrar(null, "PAGO_RECHAZADO", "Pago", pago.getId(),
                    "{\"motivo\": \"" + motivo.replace("\"", "'") + "\"}");
        } else {
            fachadaPersistencia.put(pago); // VENCIDO y rechazado: solo se guarda la respuesta
        }
    }

    /**
     * Adaptador activo según el modo de la configuración (Adapter + Protected Variations).
     *
     * <p>Casos de uso: CU-35; tareas programadas: vencimiento de pagos online pendientes
     * @param config Configuración de la pasarela.
     * @return adaptador
     */
    private PasarelaPago pasarelaActiva(ConfiguracionPasarela config) {
        return adaptadorDe(config.getModo());
    }

    /**
     * Adaptador de un modo: MERCADO_PAGO → {@code MercadoPagoAdapter}; SIMULADA (o nulo) → el simulado.
     *
     * <p>Casos de uso: CU-34, CU-35
     * @param modo Modo.
     * @return adaptador
     */
    private PasarelaPago adaptadorDe(ModoPasarela modo) {
        return modo == ModoPasarela.MERCADO_PAGO ? pasarelaMercadoPago : pasarela;
    }

    /**
     * Consulta el resultado sin propagar fallas de la pasarela (para la tarea programada): sin identificador o ante
     * un error devuelve null, y el pago se trata como abandonado.
     *
     * <p>Casos de uso: tareas programadas: vencimiento de pagos online pendientes
     * @param activa Adaptador.
     * @param idPagoPasarela Identificador o null.
     * @return resultado o null
     */
    private ResultadoPasarela consultarSinFallar(PasarelaPago activa, String idPagoPasarela) {
        if (idPagoPasarela == null) {
            return null;
        }
        try {
            return activa.obtenerResultado(idPagoPasarela);
        } catch (RuntimeException ex) {
            BITACORA.warn("No se pudo consultar el pago {} en la pasarela", idPagoPasarela, ex);
            return null;
        }
    }

    /**
     * Grupo vigente del socio de la sesión ({@code buscarUno(GrupoVigenteDeSocio(idSocio))}, sec_cu37_p1).
     *
     * <p>Casos de uso: CU-35, CU-37
     * @param usuario Cuenta.
     * @return grupo o null si no tiene
     */
    private GrupoFamiliar grupoDe(Usuario usuario) {
        return fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(usuario.getId())).orElse(null);
    }

    /**
     * Indica si la excepción técnica de {@code BrokerBaseDatos} proviene de una violación de restricción única
     * (índice {@code ux_pago_aprobado_por_cuota}, RN-18).
     *
     * <p>Casos de uso: CU-36
     * @param ex excepción
     * @return true si la causa es SQLState 23505
     */
    private static boolean esViolacionUnica(Throwable ex) {
        for (Throwable t = ex; t != null; t = t.getCause()) {
            if (t instanceof SQLException sql && VIOLACION_UNICA.equals(sql.getSQLState())) {
                return true;
            }
        }
        return false;
    }
}
