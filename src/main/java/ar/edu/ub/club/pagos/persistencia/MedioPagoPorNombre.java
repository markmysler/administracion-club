package ar.edu.ub.club.pagos.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.pagos.dominio.MedioPago;
import java.util.List;

/**
 * Medio de pago por nombre, sin distinguir mayúsculas (CU-34 FA1: el nombre es único). Consulta 55 de la
 * Entrega 7C.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve el broker de su clase de resultado.
 *
 * <p>Casos de uso: CU-34
 */
public class MedioPagoPorNombre extends ConsultaEntidades<MedioPago> {

    /** Nombre buscado. */
    private final String nombre;

    /**
     * Crea la consulta.
     *
     * <p>Casos de uso: CU-34
     * @param nombre Nombre del medio.
     */
    public MedioPagoPorNombre(String nombre) {
        this.nombre = nombre == null ? "" : nombre.trim();
    }

    /**
     * Clase del resultado: {@code MedioPago}.
     *
     * <p>Casos de uso: CU-34
     * @return MedioPago.class
     */
    @Override
    public Class<MedioPago> claseResultado() {
        return MedioPago.class;
    }

    /**
     * Condición.
     *
     * <p>Casos de uso: CU-34
     * @return fragmento SQL
     */
    @Override
    public String sql() {
        return "WHERE lower(medio_pago.nombre) = lower(?)";
    }

    /**
     * Parámetros en orden.
     *
     * <p>Casos de uso: CU-34
     * @return [nombre]
     */
    @Override
    public List<Object> parametros() {
        return List.of(nombre);
    }
}
