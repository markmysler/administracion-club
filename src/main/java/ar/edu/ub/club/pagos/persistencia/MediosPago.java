package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * Todos los medios de pago manuales, por nombre (P-31). Consulta 61 de MODELO §11.8.4.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-34, CU-36
 */
public class MediosPago extends ConsultaEntidades<MedioPago> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-34, CU-36
     */
    public MediosPago() {
        // sin parámetros
    }

    /**
     * Clase del resultado: {@code MedioPago}.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return MedioPago.class
     */
    @Override
    public Class<MedioPago> claseResultado() {
        return MedioPago.class;
    }

    /**
     * Orden por nombre.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "ORDER BY medio_pago.nombre";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-34, CU-36
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}
