package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.recursos.dominio.CanchaDesactivadaEvento;
import ar.edu.ub.club.recursos.dominio.DeporteDesactivadoEvento;
import ar.edu.ub.club.reservas.dominio.PublicadorEventosReserva;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.reservas.dominio.ReservaCanceladaEvento;
import ar.edu.ub.club.reservas.persistencia.ReservasFuturasDeCancha;
import ar.edu.ub.club.reservas.persistencia.ReservasFuturasDeDeporte;
import ar.edu.ub.club.reservas.persistencia.ReservasFuturasDeSocio;
import ar.edu.ub.club.socios.dominio.SocioDesactivadoEvento;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Observador (patrón Observer) de los eventos de dominio de baja {@code SocioDesactivadoEvento}, {@code
 * CanchaDesactivadaEvento} y {@code DeporteDesactivadoEvento}, que publican {@code ServicioSocios} y {@code
 * ServicioRecursos} con los eventos de aplicación de Spring. Se ejecuta de forma sincrónica dentro de la
 * transacción de la baja ({@code @EventListener}; {@code @Transactional} con propagación REQUIRED se une a ella),
 * de modo que la baja y la cancelación de las reservas futuras son atómicas (RN-16). Gracias a él, los módulos
 * socios y recursos no dependen de reservas. No vuelve a verificar permisos: los verificó la fachada que publicó el
 * evento (sec_comun_cancelar_baja).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23
 */
@Component
@Transactional
public class CanceladorReservasPorBaja {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code ReservasFuturasDeSocio}, {@code ReservasFuturasDeCancha}, {@code ReservasFuturasDeDeporte}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Publica {@code ReservaCanceladaEvento} por cada cancelación (notificación y auditoría). */
    private final PublicadorEventosReserva publicador;

    /** Momento actual. */
    private final Reloj reloj;

    /**
     * Crea el observador (bean de Spring; dependencias por constructor).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param fachadaPersistencia fachada de persistencia
     * @param publicador sujeto del patrón Observer de reservas
     * @param reloj fecha y hora actual
     */
    public CanceladorReservasPorBaja(FachadaPersistencia fachadaPersistencia, PublicadorEventosReserva publicador, Reloj reloj) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.publicador = publicador;
        this.reloj = reloj;
    }

    /**
     * Cancela las reservas futuras confirmadas del socio con motivo «Baja de socio» y {@code canceladaPor =
     * evento.usuario}.
     *
     * <p>Casos de uso: CU-03
     * @param evento Socio dado de baja y cuenta que realizó la baja.
     */
    @EventListener
    public void alDesactivarSocio(SocioDesactivadoEvento evento) {
        List<Reserva> reservas = fachadaPersistencia.buscar(new ReservasFuturasDeSocio(evento.getIdSocio(), reloj.ahora()));
        cancelarTodas(reservas, "Baja de socio", evento.getUsuario());
    }

    /**
     * Cancela las reservas futuras de la cancha con motivo «Baja de cancha».
     *
     * <p>Casos de uso: CU-23
     * @param evento Cancha dada de baja y cuenta.
     */
    @EventListener
    public void alDesactivarCancha(CanchaDesactivadaEvento evento) {
        List<Reserva> reservas = fachadaPersistencia.buscar(new ReservasFuturasDeCancha(evento.getIdCancha(), reloj.ahora()));
        cancelarTodas(reservas, "Baja de cancha", evento.getUsuario());
    }

    /**
     * Cancela las reservas futuras de las canchas del deporte con motivo «Baja de deporte».
     *
     * <p>Casos de uso: CU-20
     * @param evento Deporte dado de baja y cuenta.
     */
    @EventListener
    public void alDesactivarDeporte(DeporteDesactivadoEvento evento) {
        List<Reserva> reservas = fachadaPersistencia.buscar(new ReservasFuturasDeDeporte(evento.getIdDeporte(), reloj.ahora()));
        cancelarTodas(reservas, "Baja de deporte", evento.getUsuario());
    }

    /**
     * Para cada reserva invoca {@code cancelar(motivo, por, ahora)} (el estado registra la cancelación), la guarda
     * con {@code fachadaPersistencia.put(r)} y publica {@code ReservaCanceladaEvento(r, por, ahora)}. Si una falla,
     * la excepción revierte la baja completa (RN-16).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param reservas reservas futuras confirmadas afectadas
     * @param motivo «Baja de socio», «Baja de cancha» o «Baja de deporte»
     * @param por administrador que confirmó la baja
     * @return cantidad cancelada
     */
    private Integer cancelarTodas(List<Reserva> reservas, String motivo, Usuario por) {
        LocalDateTime ahora = reloj.ahora();
        int canceladas = 0;
        for (Reserva r : reservas) {
            r.cancelar(motivo, por, ahora);
            fachadaPersistencia.put(r);
            publicador.publicar(new ReservaCanceladaEvento(r, por, ahora));
            canceladas++;
        }
        return canceladas;
    }
}
