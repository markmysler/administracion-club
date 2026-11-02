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
     * Inicia la aplicación. Fija antes la zona horaria del club en la JVM (Windows informa el alias obsoleto
     * «America/Buenos_Aires», que PostgreSQL 16 rechaza al abrir la conexión).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("America/Argentina/Buenos_Aires"));
        SpringApplication.run(AplicacionClub.class, args);
    }
}
