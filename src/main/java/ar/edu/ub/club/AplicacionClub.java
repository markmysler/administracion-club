package ar.edu.ub.club;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Punto de entrada de la aplicación Spring Boot (Vaadin Flow + Spring Security + JDBC con Database Broker). Habilita
 * las tareas programadas de {@code PlanificadorCuotas}, {@code PlanificadorReservas} y {@code PlanificadorPagos}.
 *
 * <p>Casos de uso: ninguno (configuración)
 */
@SpringBootApplication
@EnableScheduling
public class AplicacionClub {

    /**
     * Inicia la aplicación.
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(AplicacionClub.class, args);
    }
}
