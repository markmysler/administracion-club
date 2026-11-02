package ar.edu.ub.club.socios.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Socio con ese DNI (a lo sumo uno: {@code uk_socio_dni}). La usa {@code ServicioSocios} con {@code contar} para
 * verificar que el DNI no esté registrado (RN-01) en el alta y en la modificación.
 *
 * <p>Objeto consulta (Query Object, Fowler) de entidades: su {@code sql()} se agrega a {@code consultaBase()} del
 * broker de {@code Socio} y el resultado pasa por el mapa de identidad. No es un bean: se crea con {@code new}.
 *
 * <p>Casos de uso: CU-01, CU-02
 */
public class SocioPorDni extends ConsultaEntidades<Socio> {

    /** DNI buscado. */
    private final String dni;

    /**
     * Crea la consulta con sus parámetros.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param dni DNI buscado
     */
    public SocioPorDni(String dni) {
        this.dni = dni;
    }

    /**
     * Clase de resultado: la resuelve el broker de {@code Socio}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return {@code Socio.class}
     */
    @Override
    public Class<Socio> claseResultado() {
        return Socio.class;
    }

    /**
     * Condición por DNI sobre {@code usuario JOIN socio}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return condición y orden con parámetros {@code ?}
     */
    @Override
    public String sql() {
        return "WHERE socio.dni = ?";
    }

    /**
     * Valores de los parámetros en el orden de {@code sql()}.
     *
     * <p>Casos de uso: CU-01, CU-02
     * @return parámetros
     */
    @Override
    public List<Object> parametros() {
        return List.of(dni);
    }
}
