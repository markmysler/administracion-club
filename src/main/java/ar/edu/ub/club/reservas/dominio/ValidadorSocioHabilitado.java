package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Eslabón concreto de la Cadena de Responsabilidad (GoF Chain of Responsibility).
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorSocioHabilitado extends ValidadorReservaEncadenado {

    /** Atributo grupo. */
    private GrupoFamiliar grupo;

    /**
     * Crea una instancia de {@code ValidadorSocioHabilitado}.
     *
     * <p>Casos de uso: CU-27
     * @param grupo valor de {@code grupo}
     */
    public ValidadorSocioHabilitado(GrupoFamiliar grupo) {
        // PENDIENTE
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s valor de {@code s}
     */
    protected void verificar(SolicitudReserva s) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
