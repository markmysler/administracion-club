package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * Medios de pago manuales habilitados, por nombre (los que admite un pago nuevo, CU-36). Consulta 54 de la
 * Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-36
 */
public class MediosPagoHabilitados extends ConsultaEntidades<MedioPago> {

    /**
     * Crea la consulta (sin parámetros).
     *
     * <p>Casos de uso: CU-36
     */
    public MediosPagoHabilitados() {
        // sin parámetros
    }

    /**
     * Clase del resultado: {@code MedioPago}.
     *
     * <p>Casos de uso: CU-36
     * @return MedioPago.class
     */
    @Override
    public Class<MedioPago> claseResultado() {
        return MedioPago.class;
    }

    /**
     * Condición y orden.
     *
     * <p>Casos de uso: CU-36
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE medio_pago.habilitado = true ORDER BY medio_pago.nombre";
    }

    /**
     * Sin parámetros.
     *
     * <p>Casos de uso: CU-36
     * @return lista vacía
     */
    @Override
    public List<Object> parametros() {
        return List.of();
    }
}
