package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.cuotas.ui.ResumenSaldo;
import ar.edu.ub.club.socios.aplicacion.PerfilSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.Optional;

/**
 * Presentador de P-24 (mi perfil): datos personales, integrantes del grupo, indicación de titular, cuota vigente y
 * saldo a favor del grupo con sus movimientos (CU-05, {@code sec_cu05_p1} y {@code sec_cu05_p2}).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a las fachadas de socios y cuotas
 * dentro de {@code consultar(...)} y convierte con {@code ResumenPerfil.desde} y {@code ResumenSaldo.desde}.
 *
 * <p>Casos de uso: CU-05
 */
@SpringComponent
@UIScope
public class PresentadorMiPerfil extends PresentadorBase<PantallaMiPerfil> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /** Fachada de cuotas (saldo a favor y movimientos). */
    private final ServicioCuotas servicioCuotas;

    /** Socio de la sesión. */
    private final SocioActual socioActual;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     * @param servicioCuotas fachada de cuotas
     * @param socioActual socio de la sesión
     */
    public PresentadorMiPerfil(ServicioSocios servicioSocios, ServicioCuotas servicioCuotas, SocioActual socioActual) {
        this.servicioSocios = servicioSocios;
        this.servicioCuotas = servicioCuotas;
        this.socioActual = socioActual;
    }

    /**
     * {@code ServicioSocios.consultarPerfilPropio(socioActual.obtener())} → {@code ResumenPerfil.desde(perfil)};
     * {@code ServicioCuotas.consultarSaldo(perfil.grupo.id)} → {@code ResumenSaldo.desde(saldo)}; luego muestra el
     * perfil y el saldo.
     *
     * <p>Casos de uso: CU-05
     */
    public void cargar() {
        Optional<PerfilSocio> perfil = consultar(() -> servicioSocios.consultarPerfilPropio(socioActual.obtener()));
        if (perfil.isEmpty()) {
            return;
        }
        ResumenPerfil resumen = ResumenPerfil.desde(perfil.get());
        Optional<ResumenSaldo> saldo = consultar(() -> ResumenSaldo.desde(
                servicioCuotas.consultarSaldo(perfil.get().getGrupo().getId())));
        pantalla.mostrarPerfil(resumen);
        saldo.ifPresent(pantalla::mostrarSaldo);
    }
}
