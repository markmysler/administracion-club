package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Socio que reserva, leído con bloqueo pesimista ({@code FOR UPDATE}, antes {@code bloquearParaReserva}): la usa el
 * módulo reservas para serializar las reservas concurrentes del mismo socio y respetar el límite de reservas
 * vigentes (RN-12). Como {@code buscar} siempre ejecuta la consulta, el bloqueo se toma aunque el socio ya esté
 * en el mapa de identidad.
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code Socio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-27, CU-28
 */
public class SocioParaReserva extends ConsultaEntidades<Socio> {

    /** Socio que reserva. */
    private final Long idSocio;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @param idSocio socio que reserva
     */
    public SocioParaReserva(Long idSocio) {
        this.idSocio = idSocio;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code Socio}.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseResultado() {
        return Socio.class;
    }

    /**
     * Condición por id con {@code FOR UPDATE} (bloquea las filas de usuario y socio hasta el fin de la transacción).
     *
     * <p>Casos de uso: CU-27, CU-28
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE socio.id = ? FOR UPDATE";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-27, CU-28
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(idSocio);
    }
}
