package ar.edu.ub.club.compartido.dominio;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Interfaz (Protected Variations) que da la fecha y la hora actuales en la zona del club; permite probar plazos
 * sin depender del reloj del sistema.
 *
 * <p>Casos de uso: todos
 */
public interface Reloj {

    /**
     * Fecha actual en la zona del club.
     *
     * <p>Casos de uso: CU-40; tareas programadas: emisión mensual de cuotas, vencimiento de cuotas
     * @return fecha de hoy
     */
    LocalDate hoy();

    /**
     * Momento actual en la zona del club.
     *
     * <p>Casos de uso: CU-11, CU-26, CU-27, CU-30, CU-32, CU-33; tareas programadas: finalización de reservas
     * cumplidas
     * @return fecha y hora
     */
    LocalDateTime ahora();
}
