package ar.edu.ub.club.agenda.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import java.time.LocalDate;

/**
 * Día no laborable del club: en esa fecha no se ofrecen turnos ni se aceptan reservas (RN-13). Forma parte de
 * {@code DisponibilidadClub.feriados} (se cargan siempre con la fila única).
 *
 * <p>Entidad del dominio; la persiste {@code BrokerFeriado} (tabla {@code feriado}).
 *
 * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
 */
public class Feriado extends EntidadPersistente {

    /** Longitud máxima de la descripción ({@code feriado.descripcion VARCHAR(100)}). */
    private static final int LARGO_DESCRIPCION = 100;

    /** Fecha del feriado (única). */
    private LocalDate fecha;

    /** Descripción (por ejemplo «Navidad»). */
    private String descripcion;

    /**
     * Crea un feriado nuevo (CU-24 FA1); valida que tenga fecha y una descripción de 1 a 100 caracteres. Que la
     * fecha sea futura y no repetida lo verifica {@code ServicioAgenda.registrarFeriado}.
     *
     * <p>Casos de uso: CU-24
     * @param fecha fecha del feriado
     * @param descripcion descripción
     * @throws ErrorNegocio con campo {@code fecha} o {@code descripcion} si un dato falta o es demasiado largo
     */
    public Feriado(LocalDate fecha, String descripcion) {
        if (fecha == null) {
            throw new ErrorNegocio("fecha", "Ingrese la fecha del feriado.");
        }
        String texto = descripcion == null ? "" : descripcion.trim();
        if (texto.isEmpty() || texto.length() > LARGO_DESCRIPCION) {
            throw new ErrorNegocio("descripcion", "Ingrese una descripción de hasta " + LARGO_DESCRIPCION + " caracteres.");
        }
        this.fecha = fecha;
        this.descripcion = texto;
    }

    /**
     * Constructor de materialización: no valida (los datos vienen de la base).
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     */
    private Feriado() {
    }

    /**
     * Desmaterialización (estereotipo «persistencia», solo para {@code BrokerFeriado}): reconstituye el feriado
     * con los valores de la fila.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @param id identificador
     * @param fecha fecha
     * @param descripcion descripción
     * @return el feriado reconstituido
     */
    public static Feriado reconstituir(Long id, LocalDate fecha, String descripcion) {
        Feriado feriado = new Feriado();
        feriado.asignarId(id);
        feriado.fecha = fecha;
        feriado.descripcion = descripcion;
        return feriado;
    }

    /**
     * Devuelve {@code fecha}: fecha del feriado.
     *
     * <p>Casos de uso: CU-24, CU-26, CU-27, CU-28, CU-32
     * @return el valor del atributo
     */
    public LocalDate getFecha() {
        return fecha;
    }

    /**
     * Devuelve {@code descripcion}: descripción del feriado.
     *
     * <p>Casos de uso: CU-24
     * @return el valor del atributo
     */
    public String getDescripcion() {
        return descripcion;
    }
}
