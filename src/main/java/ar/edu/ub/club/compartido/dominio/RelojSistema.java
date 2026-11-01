package ar.edu.ub.club.compartido.dominio;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Implementación de {@code Reloj} sobre el reloj del sistema en la zona del club (America/Argentina/Buenos_Aires).
 * El diseño declara solo la interfaz; esta clase es la implementación mínima que registra
 * {@code ConfiguracionAplicacion} como bean. En las pruebas se reemplaza por un reloj fijo.
 *
 * <p>Casos de uso: todos
 */
public class RelojSistema implements Reloj {

    /** Zona horaria del club. */
    public static final ZoneId ZONA_CLUB = ZoneId.of("America/Argentina/Buenos_Aires");

    /** Reloj de Java en la zona del club. */
    private final Clock reloj;

    /**
     * Crea el reloj en la zona del club.
     *
     * <p>Casos de uso: todos
     */
    public RelojSistema() {
        this(Clock.system(ZONA_CLUB));
    }

    /**
     * Crea el reloj sobre un {@code Clock} dado (pruebas).
     *
     * <p>Casos de uso: todos
     * @param reloj reloj de Java
     */
    public RelojSistema(Clock reloj) {
        this.reloj = reloj;
    }

    /**
     * Fecha actual en la zona del club.
     *
     * <p>Casos de uso: todos
     * @return fecha de hoy
     */
    @Override
    public LocalDate hoy() {
        return LocalDate.now(reloj);
    }

    /**
     * Momento actual en la zona del club, sin nanosegundos (precisión de la base).
     *
     * <p>Casos de uso: todos
     * @return fecha y hora
     */
    @Override
    public LocalDateTime ahora() {
        return LocalDateTime.now(reloj).withNano(0);
    }
}
