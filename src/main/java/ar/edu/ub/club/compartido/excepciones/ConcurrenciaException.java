package ar.edu.ub.club.compartido.excepciones;

/**
 * Excepción que lanza {@code BrokerBaseDatos.actualizar} cuando el control optimista por versión no actualizó
 * ninguna fila (la reserva cambió mientras se editaba, CU-30, CU-32, CU-33). El presentador la atiende en {@code
 * alConflicto}: muestra «la reserva cambió» y vuelve a cargar.
 *
 * <p>Casos de uso: CU-30, CU-32, CU-33
 */
public class ConcurrenciaException extends RuntimeException {

    /** Clase del objeto que no se pudo actualizar. */
    private final String claseObjeto;

    /** Identificador del objeto. */
    private final Long id;

    /**
     * Crea la excepción con el mensaje «La versión cambió: el broker no actualizó ninguna fila».
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @param claseObjeto clase del objeto que no se pudo actualizar
     * @param id identificador del objeto
     */
    public ConcurrenciaException(String claseObjeto, Long id) {
        super("La versión cambió: el broker no actualizó ninguna fila (" + claseObjeto + " " + id + ")");
        this.claseObjeto = claseObjeto;
        this.id = id;
    }

    /**
     * Devuelve la clase del objeto en conflicto.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @return nombre simple de la clase
     */
    public String getClaseObjeto() {
        return claseObjeto;
    }

    /**
     * Devuelve el identificador del objeto en conflicto.
     *
     * <p>Casos de uso: CU-30, CU-32, CU-33
     * @return identificador
     */
    public Long getId() {
        return id;
    }
}
