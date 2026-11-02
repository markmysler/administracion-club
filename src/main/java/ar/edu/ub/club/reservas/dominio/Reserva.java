package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.agenda.dominio.Turno;
import ar.edu.ub.club.compartido.excepciones.TransicionInvalidaException;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * Entidad contexto del patrón State: delega {@code cancelar}, {@code modificar} y {@code finalizar} en su {@code
 * EstadoReserva}; una transición inválida lanza {@code TransicionInvalidaException}. Extiende {@code
 * EntidadPersistente}, de la que hereda {@code id} y {@code version} (control optimista). El constructor deja la
 * reserva en el pseudoestado inicial; después de validar la solicitud, la fachada llama a {@code confirmar()},
 * que realiza la transición inicial a Confirmada, y publica {@code ReservaConfirmadaEvento}. Fuera del paquete
 * {@code reservas.dominio} la reserva solo cambia mediante sus operaciones públicas, que delegan en el estado.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-29, CU-30, CU-31, CU-32, CU-33; tareas programadas:
 * finalización de reservas cumplidas
 */
public class Reserva extends EntidadPersistente {

    /** Titular de la reserva. */
    private Socio socio;

    /** Cancha. */
    private Cancha cancha;

    /** Fecha, hora de inicio y hora de fin. */
    private Turno turno;

    /** Estado actual (objeto estado); {@code null} en el pseudoestado inicial, hasta {@code confirmar()}. */
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
     * de {@code EntidadPersistente}: la versión se inicia en 0 para que {@code BrokerReserva} aplique el control
     * optimista en cada actualización.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param socio Socio.
     * @param cancha Cancha.
     * @param turno Turno.
     * @param registradaPor Cuenta que registra.
     * @param ahora Momento del alta.
     */
    public Reserva(Socio socio, Cancha cancha, Turno turno, Usuario registradaPor, LocalDateTime ahora) {
        this.socio = socio;
        this.cancha = cancha;
        this.turno = turno;
        this.registradaPor = registradaPor;
        this.fechaCreacion = ahora;
        asignarVersion(0);
    }

    /**
     * Constructor vacío que usa {@code reconstituir} (persistencia): no pasa por el pseudoestado inicial.
     *
     * <p>Casos de uso: todos
     */
    private Reserva() {
        // los valores los asigna reconstituir
    }

    /**
     * Transición inicial (pseudoestado inicial → Confirmada): asigna {@code
     * EstadoReserva.desdeCodigo("CONFIRMADA")}. Es la única transición que resuelve la entidad, porque el
     * pseudoestado inicial no es un objeto estado; si la reserva ya tenía estado lanza {@code
     * TransicionInvalidaException}.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @throws TransicionInvalidaException si la reserva ya tenía estado
     */
    public void confirmar() {
        if (estado != null) {
            throw new TransicionInvalidaException("La reserva ya fue confirmada.");
        }
        estado = EstadoReserva.desdeCodigo("CONFIRMADA");
    }

    /**
     * Delega en {@code estado.cancelar(this, motivo, por, ahora)} (patrón State).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param motivo Motivo.
     * @param por Cuenta que cancela.
     * @param ahora Momento actual.
     * @throws TransicionInvalidaException si la reserva no está confirmada
     */
    public void cancelar(String motivo, Usuario por, LocalDateTime ahora) {
        estadoActual().cancelar(this, motivo, por, ahora);
    }

    /**
     * Delega en el estado; solo Confirmada lo admite y permanece Confirmada (RN-15).
     *
     * <p>Casos de uso: CU-32
     * @param nuevoTurno Turno nuevo.
     * @param nuevaCancha Cancha nueva.
     * @param ahora Momento actual.
     * @throws TransicionInvalidaException si la reserva no está confirmada
     */
    public void modificar(Turno nuevoTurno, Cancha nuevaCancha, LocalDateTime ahora) {
        estadoActual().modificar(this, nuevoTurno, nuevaCancha, ahora);
    }

    /**
     * Delega en el estado: Confirmada → Finalizada.
     *
     * <p>Casos de uso: tareas programadas: finalización de reservas cumplidas
     * @throws TransicionInvalidaException si la reserva no está confirmada
     */
    public void finalizar() {
        estadoActual().finalizar(this);
    }

    /**
     * Indica si la reserva es del socio: compara el identificador del titular (RN-20).
     *
     * <p>Casos de uso: CU-29, CU-30, CU-31, CU-32
     * @param socio Socio.
     * @return true si le pertenece.
     */
    public boolean perteneceA(Socio socio) {
        return socio != null && this.socio != null && this.socio.getId() != null
                && Objects.equals(this.socio.getId(), socio.getId());
    }

    /**
     * Confirmada y con turno futuro (todavía no comenzó).
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @param ahora Momento actual.
     * @return true si está vigente.
     */
    public boolean esVigente(LocalDateTime ahora) {
        return estado instanceof EstadoReservaConfirmada && !haComenzado(ahora);
    }

    /**
     * Indica si el inicio del turno es anterior o igual a {@code ahora}.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @param ahora momento actual
     * @return true si el turno ya comenzó
     */
    public boolean haComenzado(LocalDateTime ahora) {
        return !turno.inicio().isAfter(ahora);
    }

    /**
     * Crea la reserva con los valores de una fila ya existente, sin pasar por el pseudoestado inicial
     * («persistencia»). Solo la usa {@code BrokerReserva.crearObjeto}; las asociaciones llegan después con
     * {@code restaurarAsociaciones}.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param version versión del control optimista
     * @param turno fecha, hora de inicio y hora de fin
     * @param estado objeto estado ({@code EstadoReserva.desdeCodigo})
     * @param fechaCreacion momento del registro
     * @param motivoCancelacion motivo de la cancelación o {@code null}
     * @return la reserva reconstituida
     */
    public static Reserva reconstituir(Long id, Integer version, Turno turno, EstadoReserva estado, LocalDateTime fechaCreacion, String motivoCancelacion) {
        Reserva r = new Reserva();
        r.asignarId(id);
        r.asignarVersion(version);
        r.turno = turno;
        r.estado = estado;
        r.fechaCreacion = fechaCreacion;
        r.motivoCancelacion = motivoCancelacion;
        return r;
    }

    /**
     * Asigna las asociaciones a uno que resolvió el broker («persistencia»): socio y cancha por JOIN, las cuentas
     * con {@code get}.
     *
     * <p>Casos de uso: todos
     * @param socio titular
     * @param cancha cancha
     * @param registradaPor cuenta que registró
     * @param canceladaPor cuenta que canceló o {@code null}
     */
    public void restaurarAsociaciones(Socio socio, Cancha cancha, Usuario registradaPor, Usuario canceladaPor) {
        this.socio = socio;
        this.cancha = cancha;
        this.registradaPor = registradaPor;
        this.canceladaPor = canceladaPor;
    }

    /**
     * Reemplaza el objeto estado (visibilidad de paquete: solo lo invocan los estados concretos).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33; tareas programadas: finalización de reservas cumplidas
     * @param nuevo estado destino
     */
    void cambiarEstado(EstadoReserva nuevo) {
        this.estado = nuevo;
    }

    /**
     * Asigna el motivo y la cuenta que cancela (visibilidad de paquete; la usa {@code EstadoReservaConfirmada}).
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-33
     * @param motivo motivo de la cancelación
     * @param por cuenta que cancela
     */
    void registrarCancelacion(String motivo, Usuario por) {
        this.motivoCancelacion = motivo;
        this.canceladaPor = por;
    }

    /**
     * Reemplaza el turno y la cancha de una reserva confirmada (visibilidad de paquete; la usa
     * {@code EstadoReservaConfirmada.modificar}).
     *
     * <p>Casos de uso: CU-32
     * @param nuevoTurno turno nuevo
     * @param nuevaCancha cancha nueva
     */
    void cambiarTurno(Turno nuevoTurno, Cancha nuevaCancha) {
        this.turno = nuevoTurno;
        this.cancha = nuevaCancha;
    }

    /**
     * Estado actual; en el pseudoestado inicial (sin confirmar) ninguna transición es válida.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas
     * cumplidas
     * @return el objeto estado
     * @throws TransicionInvalidaException si la reserva todavía no fue confirmada
     */
    private EstadoReserva estadoActual() {
        if (estado == null) {
            throw new TransicionInvalidaException("La reserva todavía no fue confirmada.");
        }
        return estado;
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
