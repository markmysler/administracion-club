package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: todos
 */
public class SocioParaReserva extends ConsultaEntidades<Socio> {

    /** Atributo idSocio. */
    private Long idSocio;

    /**
     * TEMPORAL (rama reservas): constructor que implementa el agente del módulo; se descarta al unir.
     *
     * <p>Casos de uso: CU-27
     */
    public SocioParaReserva(Long idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @return el resultado
     */
    public Class<Socio> claseResultado() {
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
