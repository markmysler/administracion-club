package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-05: Inicio del socio: resumen de la cuota vigente, próximas reservas y accesos a
 * reservar y pagar.
 *
 * <p>Casos de uso: CU-05, CU-29
 */
public class PresentadorInicioSocio extends PresentadorBase<PantallaInicioSocio> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /**
     * {@code ServicioSocios.consultarPerfilPropio(socioActual.obtener()): PerfilSocio} → {@code
     * ResumenPerfil.desde(perfil)}
     *
     * <p>Casos de uso: CU-05, CU-29
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("turnos")}
     *
     * <p>Casos de uso: CU-05, CU-29
     */
    public void alReservar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code navegarA("estado-cuenta")}
     *
     * <p>Casos de uso: CU-05, CU-29
     */
    public void alPagar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
