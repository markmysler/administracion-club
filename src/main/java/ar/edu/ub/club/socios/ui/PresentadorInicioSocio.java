package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de P-05 (inicio del socio): resumen de la cuota vigente, próximas reservas y accesos a reservar y
 * pagar. Obtiene la cuenta con {@code socioActual.obtener()} (RN-20: la vista nunca envía la identidad).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin, llama a la fachada dentro de
 * {@code consultar(...)} y convierte el resultado con {@code ResumenPerfil.desde(...)}.
 *
 * <p>Casos de uso: CU-05, CU-29
 */
@SpringComponent
@UIScope
public class PresentadorInicioSocio extends PresentadorBase<PantallaInicioSocio> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /** Socio de la sesión. */
    private final SocioActual socioActual;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     * @param socioActual socio de la sesión
     */
    public PresentadorInicioSocio(ServicioSocios servicioSocios, SocioActual socioActual) {
        this.servicioSocios = servicioSocios;
        this.socioActual = socioActual;
    }

    /**
     * Carga el resumen: {@code ServicioSocios.consultarPerfilPropio(socioActual.obtener())} →
     * {@code ResumenPerfil.desde(perfil)} → {@code mostrarResumen}.
     *
     * <p>Casos de uso: CU-05
     */
    public void cargar() {
        consultar(() -> ResumenPerfil.desde(servicioSocios.consultarPerfilPropio(socioActual.obtener())))
                .ifPresent(pantalla::mostrarResumen);
    }

    /**
     * Va a la grilla de turnos disponibles (CU-26/CU-27).
     *
     * <p>Casos de uso: CU-05, CU-27
     */
    public void alReservar() {
        pantalla.navegarA("turnos");
    }

    /**
     * Va al estado de cuenta del grupo (P-25, CU-35).
     *
     * <p>Casos de uso: CU-05, CU-35
     */
    public void alPagar() {
        pantalla.navegarA("estado-cuenta");
    }
}
