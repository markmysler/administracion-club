package ar.edu.ub.club.recursos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: todos
 */
public class DeportePorNombre extends ConsultaEntidades<Deporte> {

    /** Atributo nombre. */
    private String nombre;

    /** Atributo idExcluido. */
    private Long idExcluido;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<Deporte> claseResultado() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public String sql() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    @Override
    public List<Object> parametros() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
