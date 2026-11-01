package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDate;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Entidad del dominio; la persiste su broker.
 *
 * <p>Casos de uso: todos
 */
public class Feriado extends EntidadPersistente {

    /** Atributo fecha. */
    private LocalDate fecha;

    /** Atributo descripcion. */
    private String descripcion;

    /**
     * Crea una instancia de {@code Feriado}.
     *
     * <p>Casos de uso: CU-24
     * @param fecha valor de {@code fecha}
     * @param descripcion valor de {@code descripcion}
     */
    public Feriado(LocalDate fecha, String descripcion) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param fecha valor de {@code fecha}
     * @param descripcion valor de {@code descripcion}
     * @return el resultado
     */
    public static Feriado reconstituir(Long id, LocalDate fecha, String descripcion) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
