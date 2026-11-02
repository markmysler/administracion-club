package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import java.util.List;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 6) de RN-13: límite de reservas vigentes por socio.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorLimiteReservas extends ValidadorReservaEncadenado {

    /** Límite configurado. */
    private final ReglaReserva regla;

    /** Reservas vigentes del socio. */
    private final List<Reserva> vigentes;

    /**
     * Crea el validador con los datos que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param regla reglas de reserva (límite)
     * @param vigentes reservas vigentes del socio
     */
    public ValidadorLimiteReservas(ReglaReserva regla, List<Reserva> vigentes) {
        this.regla = regla;
        this.vigentes = vigentes == null ? List.of() : List.copyOf(vigentes);
    }

    /**
     * Cuenta las reservas de {@code vigentes} distintas de {@code s.reservaOriginal}; si la cantidad es mayor o
     * igual a {@code regla.limiteReservasVigentesPorSocio} lanza {@code ErrorNegocio} con el límite y la cantidad
     * actual.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si se alcanzó el límite
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        Reserva original = s.getReservaOriginal();
        long cantidad = vigentes.stream().filter(r -> original == null || !mismaReserva(r, original)).count();
        int limite = regla.getLimiteReservasVigentesPorSocio();
        if (cantidad >= limite) {
            throw new ErrorNegocio("Alcanzó el límite de " + limite + " reservas vigentes (tiene " + cantidad + ").");
        }
    }

    /**
     * Compara dos reservas por identidad o por identificador (la original puede venir de otra lectura).
     *
     * <p>Casos de uso: CU-32
     * @param a una reserva
     * @param b otra reserva
     * @return true si son la misma reserva
     */
    private static boolean mismaReserva(Reserva a, Reserva b) {
        return a == b || (a.getId() != null && a.getId().equals(b.getId()));
    }
}
