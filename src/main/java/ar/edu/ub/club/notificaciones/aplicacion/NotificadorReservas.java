package ar.edu.ub.club.notificaciones.aplicacion;

import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.notificaciones.dominio.Notificacion;
import ar.edu.ub.club.notificaciones.dominio.TipoNotificacion;
import ar.edu.ub.club.notificaciones.integracion.MensajeCorreo;
import ar.edu.ub.club.notificaciones.integracion.ResultadoEnvio;
import ar.edu.ub.club.notificaciones.integracion.ServicioCorreo;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.reservas.dominio.EventoReserva;
import ar.edu.ub.club.reservas.dominio.ObservadorReserva;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.reservas.dominio.ReservaModificadaEvento;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Observador concreto del patrón Observer de los eventos de reserva (sujeto {@code PublicadorEventosReserva}).
 * {@code alOcurrir} se ejecuta dentro de la transacción de la operación, pero no hace nada en ella: registra una
 * sincronización que actúa después del commit (AFTER_COMMIT); si la transacción se revierte, el envío no ocurre.
 * Después del commit, en una transacción nueva ({@code PROPAGATION_REQUIRES_NEW}), crea la {@code Notificacion}
 * (Creator), la guarda, envía el correo por el puerto {@code ServicioCorreo} (Adapter) y registra el resultado
 * con {@code marcarEnviada} o {@code marcarFallida}. Una falla del correo nunca revierte la reserva (RN-19)
 * (sec_comun_notificar).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
@Component
public class NotificadorReservas implements ObservadorReserva {

    /** Bitácora técnica de los envíos que no pudieron registrarse. */
    private static final Logger BITACORA = LoggerFactory.getLogger(NotificadorReservas.class);

    /** Formato de fecha de los mensajes. */
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    /** Formato de hora de los mensajes. */
    private static final DateTimeFormatter HORA = DateTimeFormatter.ofPattern("HH:mm");

    /** Único punto de acceso a la persistencia («Singleton»): guarda la {@code Notificacion}. */
    private final FachadaPersistencia fachadaPersistencia;

    /** Puerto de envío de correo (adaptador activo según {@code club.correo.modo}). */
    private final ServicioCorreo servicioCorreo;

    /** Transacción nueva para el registro del envío, posterior al commit de la operación. */
    private final TransactionTemplate transaccionNueva;

    /**
     * Crea el observador (bean de Spring; queda suscripto al publicador por {@code ConfiguracionReservas}). La
     * plantilla de transacción recibida se copia con {@code PROPAGATION_REQUIRES_NEW}.
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param fachadaPersistencia fachada de persistencia
     * @param servicioCorreo puerto de correo
     * @param plantillaTransaccion plantilla de transacción de Spring
     */
    public NotificadorReservas(FachadaPersistencia fachadaPersistencia, ServicioCorreo servicioCorreo,
                               TransactionTemplate plantillaTransaccion) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.servicioCorreo = servicioCorreo;
        TransactionTemplate nueva = new TransactionTemplate(plantillaTransaccion.getTransactionManager());
        nueva.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);
        this.transaccionNueva = nueva;
    }

    /**
     * Difiere el envío hasta después del commit de la transacción en curso (si no hay transacción activa, lo
     * hace de inmediato en una transacción propia).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e evento publicado
     */
    @Override
    public void alOcurrir(EventoReserva e) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    notificarDespuesDelCommit(e);
                }
            });
        } else {
            notificarDespuesDelCommit(e);
        }
    }

    /**
     * En una transacción nueva crea la notificación sin enviar, la guarda, envía el correo y guarda el resultado.
     * Cualquier error se registra en la bitácora y no se propaga (la reserva ya está confirmada, RN-19).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e evento publicado
     */
    private void notificarDespuesDelCommit(EventoReserva e) {
        try {
            transaccionNueva.executeWithoutResult(estado -> enviar(e));
        } catch (RuntimeException ex) {
            BITACORA.error("No se pudo registrar la notificación de la reserva {}", e.getReserva().getId(), ex);
        }
    }

    /**
     * Pasos de sec_comun_notificar: «create» de la notificación, {@code put}, {@code enviar(MensajeCorreo)},
     * {@code marcarEnviada()} o {@code marcarFallida(detalle)} y {@code put}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e evento publicado
     */
    private void enviar(EventoReserva e) {
        Reserva r = e.getReserva();
        Socio socio = r.getSocio();
        TipoNotificacion tipo = TipoNotificacion.valueOf(e.accion());
        String cuerpo = cuerpo(e, tipo);
        Notificacion n = new Notificacion(socio, r, tipo, cuerpo);
        fachadaPersistencia.put(n);
        ResultadoEnvio resultado;
        try {
            resultado = servicioCorreo.enviar(new MensajeCorreo(socio.getEmail(), asunto(tipo), cuerpo));
        } catch (RuntimeException ex) {
            resultado = ResultadoEnvio.fallo(ex.getMessage());
        }
        if (resultado.isEnviado()) {
            n.marcarEnviada();
        } else {
            n.marcarFallida(resultado.getDetalle());
        }
        fachadaPersistencia.put(n);
    }

    /**
     * Asunto del correo según el tipo.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param tipo tipo de aviso
     * @return asunto
     */
    private static String asunto(TipoNotificacion tipo) {
        switch (tipo) {
            case CONFIRMACION:
                return "Reserva confirmada";
            case MODIFICACION:
                return "Reserva modificada";
            default:
                return "Reserva cancelada";
        }
    }

    /**
     * Cuerpo del correo: cancha, fecha y horario; en una modificación también los anteriores y en una
     * cancelación el motivo.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e evento
     * @param tipo tipo de aviso
     * @return texto del mensaje
     */
    private static String cuerpo(EventoReserva e, TipoNotificacion tipo) {
        Reserva r = e.getReserva();
        String actual = describir(r.getCancha(), r.getTurno());
        switch (tipo) {
            case CONFIRMACION:
                return "Su reserva de " + actual + " quedó confirmada.";
            case MODIFICACION:
                ReservaModificadaEvento m = (ReservaModificadaEvento) e;
                return "Su reserva fue modificada. Ahora es " + actual + " (antes: "
                        + describir(m.getCanchaAnterior(), m.getTurnoAnterior()) + ").";
            default:
                return "Su reserva de " + actual + " fue cancelada. Motivo: " + r.getMotivoCancelacion() + ".";
        }
    }

    /**
     * Describe una cancha y un turno: «la cancha X el dd/MM/aaaa de HH:mm a HH:mm».
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param cancha cancha
     * @param turno turno
     * @return descripción
     */
    private static String describir(Cancha cancha, Turno turno) {
        return "la cancha " + (cancha == null ? "" : cancha.getNombre()) + " el " + turno.getFecha().format(FECHA)
                + " de " + turno.getHoraInicio().format(HORA) + " a " + turno.getHoraFin().format(HORA);
    }
}
