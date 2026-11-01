package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;

/**
 * Entidad contexto del patrón State: delega {@code cancelar}, {@code modificar} y {@code finalizar} en su {@code
 * EstadoReserva}; una transición inválida lanza {@code TransicionInvalidaException}. Extiende {@code
 * EntidadPersistente}, de la que hereda {@code id} y {@code version} (control optimista). El constructor deja la
 * reserva en el pseudoestado inicial; después de validar la solicitud, la fachada llama a {@code confirmar()},
 * que realiza la transición inicial a Confirmada, y publica {@code ReservaConfirmadaEvento}.
 *
 * <p>Casos de uso: todos
 */
public class Reserva extends EntidadPersistente {

    /** Titular de la reserva. */
    private Socio socio;

    /** Cancha. */
    private Cancha cancha;

    /** Fecha, hora de inicio y hora de fin. */
    private Turno turno;

    /** Estado actual (objeto estado). */
    private EstadoReserva estado;

    /** Momento del registro. */
    private LocalDateTime fechaCreacion;

    /** Cuenta que registró la reserva. */
    private Usuario registradaPor;

    /** Cuenta que canceló. */
    private Usuario canceladaPor;

    /** Motivo de la cancelación. */
    private String motivoCancelacion;

    /**
     * Constructor que usa {@code ServicioReservas} (Creator): asigna los datos y {@code fechaCreacion = ahora}; la
     * reserva queda en el pseudoestado inicial del diagrama de estados. {@code id} y {@code version} son heredados
     * de {@code EntidadPersistente}.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param socio Socio.
     * @param cancha Cancha.
     * @param turno Turno.
     * @param registradaPor Cuenta que registra.
     * @param ahora Momento del alta.
     */
    public Reserva(Socio socio, Cancha cancha, Turno turno, Usuario registradaPor, LocalDateTime ahora) {
        // PENDIENTE
    }

    /**
     * Transición inicial (pseudoestado inicial → Confirmada): asigna {@code
     * EstadoReserva.desdeCodigo("CONFIRMADA")}. Es la única transición que resuelve la entidad, porque el
     * pseudoestado inicial no es un objeto estado; si la reserva ya tenía estado lanza {@code
     * TransicionInvalidaException}.
     *
     * <p>Casos de uso: CU-27, CU-28
     */
    public void confirmar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en {@code estado.cancelar(this, motivo, por, ahora)}.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param motivo Motivo.
     * @param por Cuenta que cancela.
     * @param ahora Momento actual.
     */
    public void cancelar(String motivo, Usuario por, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado; solo Confirmada lo admite y permanece Confirmada.
     *
     * <p>Casos de uso: CU-32
     * @param nuevoTurno Turno nuevo.
     * @param nuevaCancha Cancha nueva.
     * @param ahora Momento actual.
     */
    public void modificar(Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Delega en el estado: Confirmada → Finalizada.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     */
    public void finalizar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Indica si la reserva es del socio (RN-20).
     *
     * <p>Casos de uso: CU-30, CU-31, CU-32
     * @param socio Socio.
     * @return true si le pertenece.
     */
    public boolean perteneceA(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Confirmada y con turno futuro.
     *
     * <p>Casos de uso: CU-30, CU-33
     * @param ahora Momento actual.
     * @return true si está vigente.
     */
    public boolean esVigente(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param ahora valor de {@code ahora}
     * @return el resultado
     */
    public boolean haComenzado(LocalDateTime ahora) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param version valor de {@code version}
     * @param turno valor de {@code turno}
     * @param estado valor de {@code estado}
     * @param fechaCreacion valor de {@code fechaCreacion}
     * @param motivoCancelacion valor de {@code motivoCancelacion}
     * @return el resultado
     */
    public static Reserva reconstituir(Long id, Integer version, Turno turno, EstadoReserva estado, LocalDateTime fechaCreacion, String motivoCancelacion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param socio valor de {@code socio}
     * @param cancha valor de {@code cancha}
     * @param registradaPor valor de {@code registradaPor}
     * @param canceladaPor valor de {@code canceladaPor}
     */
    public void restaurarAsociaciones(Socio socio, Cancha cancha, Usuario registradaPor, Usuario canceladaPor) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33; tareas programadas: finalización de reservas cumplidas
     * @param nuevo valor de {@code nuevo}
     */
     void cambiarEstado(EstadoReserva nuevo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-30, CU-33
     * @param motivo valor de {@code motivo}
     * @param por valor de {@code por}
     */
     void registrarCancelacion(String motivo, Usuario por) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param nuevoTurno valor de {@code nuevoTurno}
     * @param nuevaCancha valor de {@code nuevaCancha}
     */
     void cambiarTurno(Turno nuevoTurno, Cancha nuevaCancha) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code socio}: Titular de la reserva.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code cancha}: Cancha.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Cancha getCancha() {
        return cancha;
    }

    /**
     * Devuelve {@code turno}: Fecha, hora de inicio y hora de fin.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Turno getTurno() {
        return turno;
    }

    /**
     * Devuelve {@code estado}: Estado actual (objeto estado).
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public EstadoReserva getEstado() {
        return estado;
    }

    /**
     * Devuelve {@code fechaCreacion}: Momento del registro.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Devuelve {@code registradaPor}: Cuenta que registró la reserva.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Usuario getRegistradaPor() {
        return registradaPor;
    }

    /**
     * Devuelve {@code canceladaPor}: Cuenta que canceló.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Usuario getCanceladaPor() {
        return canceladaPor;
    }

    /**
     * Devuelve {@code motivoCancelacion}: Motivo de la cancelación.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public String getMotivoCancelacion() {
        return motivoCancelacion;
    }
}
