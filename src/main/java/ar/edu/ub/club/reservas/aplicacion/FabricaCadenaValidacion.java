package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.agenda.dominio.ConsultaTurnosOcupados;
import ar.edu.ub.club.agenda.dominio.DisponibilidadClub;
import ar.edu.ub.club.agenda.dominio.Feriado;
import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.agenda.persistencia.FeriadosEntre;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.reservas.dominio.Reserva;
import ar.edu.ub.club.reservas.dominio.SolicitudReserva;
import ar.edu.ub.club.reservas.dominio.ValidadorAnticipacion;
import ar.edu.ub.club.reservas.dominio.ValidadorDuracionTurno;
import ar.edu.ub.club.reservas.dominio.ValidadorHorarioClub;
import ar.edu.ub.club.reservas.dominio.ValidadorLimiteReservas;
import ar.edu.ub.club.reservas.dominio.ValidadorRecursoActivo;
import ar.edu.ub.club.reservas.dominio.ValidadorReserva;
import ar.edu.ub.club.reservas.dominio.ValidadorSocioHabilitado;
import ar.edu.ub.club.reservas.dominio.ValidadorTurnoLibre;
import ar.edu.ub.club.reservas.persistencia.ReservasFuturasDeSocio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.stereotype.Component;

/**
 * Clase de {@code reservas.aplicacion} que obtiene con {@code FachadaPersistencia} y {@code
 * ConsultaTurnosOcupados} los datos que necesita cada validador y arma la cadena (Chain of Responsibility) en el
 * orden fijo de RN-11 a RN-15. Los validadores son clases de dominio puras que reciben sus datos por constructor
 * (Low Coupling, D-10): {@code ValidadorSocioHabilitado(grupo)}, {@code ValidadorRecursoActivo}, {@code
 * ValidadorHorarioClub(disponibilidad, feriados)}, {@code ValidadorDuracionTurno(regla, disponibilidad)}, {@code
 * ValidadorAnticipacion(regla)}, {@code ValidadorLimiteReservas(regla, vigentes)} y {@code
 * ValidadorTurnoLibre(ocupados)}. Las lecturas se hacen dentro de la transacción de la fachada que la invoca.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
@Component
public class FabricaCadenaValidacion {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code GrupoVigenteDeSocio}, {@code FeriadosEntre}, {@code ReservasFuturasDeSocio}; disponibilidad y reglas por {@code get(ID_UNICO, …)}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Turnos ocupados de la cancha en la fecha (consulta invertida de agenda que implementa reservas). */
    private final ConsultaTurnosOcupados consultaTurnos;

    /**
     * Crea la fábrica (bean de Spring; dependencias por constructor).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param fachadaPersistencia fachada de persistencia
     * @param consultaTurnos consulta de turnos ocupados
     */
    public FabricaCadenaValidacion(FachadaPersistencia fachadaPersistencia, ConsultaTurnosOcupados consultaTurnos) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.consultaTurnos = consultaTurnos;
    }

    /**
     * Obtiene el grupo vigente del socio, la disponibilidad, los feriados del día, la regla, las reservas vigentes
     * del socio y los turnos ocupados de la cancha; crea los siete validadores y los enlaza con {@code enlazar}
     * (sec_cu27_p3). En MODIFICACION excluye la reserva original de las vigentes y su turno de los ocupados.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param solicitud Solicitud a validar.
     * @return Primer validador de la cadena.
     */
    public ValidadorReserva crearCadena(SolicitudReserva solicitud) {
        Long idSocio = solicitud.getSocio().getId();
        LocalDate fecha = solicitud.getTurno().getFecha();
        GrupoFamiliar grupo = fachadaPersistencia.buscarUno(new GrupoVigenteDeSocio(idSocio)).orElse(null);
        DisponibilidadClub disponibilidad = fachadaPersistencia.get(DisponibilidadClub.ID_UNICO, DisponibilidadClub.class);
        List<Feriado> feriados = fachadaPersistencia.buscar(new FeriadosEntre(fecha, fecha));
        ReglaReserva regla = fachadaPersistencia.get(ReglaReserva.ID_UNICO, ReglaReserva.class);
        List<Reserva> vigentes = new ArrayList<>(fachadaPersistencia.buscar(
                new ReservasFuturasDeSocio(idSocio, solicitud.getAhora())));
        List<Turno> ocupados = consultaTurnos.turnosOcupados(solicitud.getCancha().getId(), fecha);
        if (solicitud.esModificacion()) {
            Long idOriginal = solicitud.getReservaOriginal().getId();
            vigentes.removeIf(r -> Objects.equals(r.getId(), idOriginal));
        }
        ocupados = ocupadosSinOriginal(solicitud, ocupados);

        ValidadorSocioHabilitado v1 = new ValidadorSocioHabilitado(grupo);
        ValidadorRecursoActivo v2 = new ValidadorRecursoActivo();
        ValidadorHorarioClub v3 = new ValidadorHorarioClub(disponibilidad, feriados);
        ValidadorDuracionTurno v4 = new ValidadorDuracionTurno(regla, disponibilidad);
        ValidadorAnticipacion v5 = new ValidadorAnticipacion(regla);
        ValidadorLimiteReservas v6 = new ValidadorLimiteReservas(regla, vigentes);
        ValidadorTurnoLibre v7 = new ValidadorTurnoLibre(ocupados);
        v1.enlazar(v2);
        v2.enlazar(v3);
        v3.enlazar(v4);
        v4.enlazar(v5);
        v5.enlazar(v6);
        v6.enlazar(v7);
        return v1;
    }

    /**
     * En una MODIFICACION en la que la cancha no cambia, quita de la lista el turno de la reserva original, para
     * que la reserva no se superponga consigo misma (CU-32 paso 13). En un ALTA devuelve la lista sin cambios.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @param ocupados turnos confirmados de la cancha en la fecha
     * @return turnos ocupados por otras reservas
     */
    private List<Turno> ocupadosSinOriginal(SolicitudReserva s, List<Turno> ocupados) {
        if (!s.esModificacion()) {
            return ocupados;
        }
        Reserva original = s.getReservaOriginal();
        if (original.getCancha() == null || !Objects.equals(original.getCancha().getId(), s.getCancha().getId())) {
            return ocupados;
        }
        Turno t = original.getTurno();
        List<Turno> resultado = new ArrayList<>(ocupados);
        for (int i = 0; i < resultado.size(); i++) {
            Turno o = resultado.get(i);
            if (Objects.equals(o.getFecha(), t.getFecha()) && Objects.equals(o.getHoraInicio(), t.getHoraInicio())
                    && Objects.equals(o.getHoraFin(), t.getHoraFin())) {
                resultado.remove(i);
                break;
            }
        }
        return resultado;
    }
}
