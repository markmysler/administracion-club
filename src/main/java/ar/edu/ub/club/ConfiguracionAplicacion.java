package ar.edu.ub.club;

import ar.edu.ub.club.compartido.dominio.Reloj;
import ar.edu.ub.club.compartido.dominio.RelojSistema;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Beans de infraestructura que el diseño declara como interfaces sin implementación nombrada: el {@code Reloj} del
 * club. El {@code TransactionTemplate}, el {@code DataSourceTransactionManager} y el pool HikariCP los configura
 * Spring Boot.
 *
 * <p>Casos de uso: ninguno (configuración)
 */
@Configuration
public class ConfiguracionAplicacion {

    /**
     * Reloj del club (zona America/Argentina/Buenos_Aires).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @return reloj del sistema
     */
    @Bean
    public Reloj reloj() {
        return new RelojSistema();
    }
}
