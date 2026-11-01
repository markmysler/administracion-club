package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * PENDIENTE: documentar responsabilidad.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-04
 */
public class FichaSocio {

    /** Atributo socio. */
    private Socio socio;

    /** Atributo grupo. */
    private GrupoFamiliar grupo;

    /** Atributo reservas. */
    private List<ResumenReserva> reservas;

    /** Atributo estadoCuenta. */
    private List<Cuota> estadoCuenta;

    /**
     * Crea una instancia de {@code FichaSocio}.
     *
     * <p>Casos de uso: CU-04
     * @param socio valor de {@code socio}
     * @param grupo valor de {@code grupo}
     * @param reservas valor de {@code reservas}
     * @param estadoCuenta valor de {@code estadoCuenta}
     */
    public FichaSocio(Socio socio, GrupoFamiliar grupo, List<ResumenReserva> reservas, List<Cuota> estadoCuenta) {
        // PENDIENTE
    }
}
