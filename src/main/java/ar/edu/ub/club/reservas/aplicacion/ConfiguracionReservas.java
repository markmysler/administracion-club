package ar.edu.ub.club.reservas.aplicacion;

import ar.edu.ub.club.reservas.dominio.ObservadorReserva;
import ar.edu.ub.club.reservas.dominio.PublicadorEventosReserva;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Agregado en la implementación: configuración del patrón Observer de reservas. {@code PublicadorEventosReserva}
 * está en {@code reservas.dominio} y no depende de Spring, por eso se declara aquí como bean; al terminar de crear
 * todos los beans se suscriben a él, una sola vez y en el orden de Spring, todos los beans que implementan {@code
 * ObservadorReserva} ({@code AuditorReservas}, {@code NotificadorReservas}). Hacerlo después de crear los beans
 * evita el ciclo publicador ↔ observadores.
 *
 * <p>Casos de uso: ninguno (configuración)
 */
@Configuration
public class ConfiguracionReservas {

    /**
     * Sujeto del patrón Observer (instancia única de la aplicación).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @return publicador de eventos de reserva
     */
    @Bean
    public PublicadorEventosReserva publicadorEventosReserva() {
        return new PublicadorEventosReserva();
    }

    /**
     * Suscribe los observadores al publicador cuando todos los beans únicos ya existen.
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param publicador sujeto del patrón Observer
     * @param observadores beans que implementan {@code ObservadorReserva}
     * @return tarea de inicio que realiza la suscripción
     */
    @Bean
    public SmartInitializingSingleton suscribirObservadoresReserva(PublicadorEventosReserva publicador,
                                                                   ObjectProvider<ObservadorReserva> observadores) {
        return () -> observadores.orderedStream().forEach(publicador::suscribir);
    }
}
