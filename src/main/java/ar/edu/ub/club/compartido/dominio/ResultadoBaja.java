package ar.edu.ub.club.compartido.dominio;

import java.util.List;

/**
 * Objeto de datos ({@code compartido.dominio}) que devuelven las bajas en dos pasos (CU-03, CU-20, CU-23): con
 * {@code confirmar = false} informa las reservas futuras que se cancelarían sin hacer la baja
 * ({@code realizada = false}); con {@code confirmar = true} hace la baja y devuelve las canceladas.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23
 *
 * @param realizada indica si la baja se efectuó
 * @param reservasAfectadas reservas futuras que se cancelan (o se cancelarían) por la baja
 */
public record ResultadoBaja(boolean realizada, List<ResumenReserva> reservasAfectadas) {

    /**
     * Resultado del primer paso: la baja no se hizo y se informan las reservas afectadas.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param afectadas reservas futuras afectadas
     * @return resultado no realizado
     */
    public static ResultadoBaja pendiente(List<ResumenReserva> afectadas) {
        return new ResultadoBaja(false, List.copyOf(afectadas));
    }

    /**
     * Resultado del segundo paso: la baja se hizo.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23
     * @param afectadas reservas canceladas por la baja
     * @return resultado realizado
     */
    public static ResultadoBaja realizada(List<ResumenReserva> afectadas) {
        return new ResultadoBaja(true, List.copyOf(afectadas));
    }
}
