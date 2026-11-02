package ar.edu.ub.club.auditoria.aplicacion;

import ar.edu.ub.club.reservas.dominio.EventoReserva;
import ar.edu.ub.club.reservas.dominio.ObservadorReserva;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Observador concreto del patrón Observer: recibe los eventos de reserva (confirmada, modificada, cancelada) que
 * publica {@code PublicadorEventosReserva} y los registra en la auditoría con {@code ServicioAuditoria} (RNF-12).
 * Se ejecuta de forma sincrónica dentro de la transacción de la fachada de reservas. El publicador lo suscribe
 * solo (todo bean {@code ObservadorReserva}); este observador no conoce al publicador.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
 */
@Component
public class AuditorReservas implements ObservadorReserva {

    /** Servicio que escribe el registro de auditoría. */
    private final ServicioAuditoria servicioAuditoria;

    /**
     * Crea el observador.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
     * @param servicioAuditoria servicio de auditoría
     */
    public AuditorReservas(ServicioAuditoria servicioAuditoria) {
        this.servicioAuditoria = servicioAuditoria;
    }

    /**
     * Registra el evento: {@code registrar(e.usuario, e.accion(), "Reserva", e.reserva.id, detalle)}; la acción la
     * define cada evento concreto (polimorfismo de {@code EventoReserva.accion()}).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
     * @param e evento de reserva
     */
    @Override
    public void alOcurrir(EventoReserva e) {
        Map<String, Object> detalle = new LinkedHashMap<>();
        detalle.put("fechaHora", String.valueOf(e.getFechaHora()));
        servicioAuditoria.registrar(e.getUsuario(), e.accion(), "Reserva",
                e.getReserva() == null ? null : e.getReserva().getId(), aJson(detalle));
    }

    /**
     * Serializa el detalle como JSON simple (valores de texto escapados).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33
     * @param detalle pares nombre → valor
     * @return JSON
     */
    private static String aJson(Map<String, Object> detalle) {
        StringBuilder sb = new StringBuilder("{");
        for (Map.Entry<String, Object> par : detalle.entrySet()) {
            if (sb.length() > 1) {
                sb.append(',');
            }
            sb.append('"').append(par.getKey()).append("\":\"")
                    .append(String.valueOf(par.getValue()).replace("\\", "\\\\").replace("\"", "\\\"")).append('"');
        }
        return sb.append('}').toString();
    }
}
