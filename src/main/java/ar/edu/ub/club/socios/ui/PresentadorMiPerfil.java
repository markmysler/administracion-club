package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.cuotas.aplicacion.ServicioCuotas;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.aplicacion.SocioActual;

/**
 * Presentador de la pantalla P-24: Datos personales del socio, integrantes de su grupo, indicación de titular y
 * saldo a favor del grupo.
 *
 * <p>Casos de uso: CU-05
 */
public class PresentadorMiPerfil extends PresentadorBase<PantallaMiPerfil> {

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo servicioCuotas. */
    private ServicioCuotas servicioCuotas;

    /** Atributo socioActual. */
    private SocioActual socioActual;

    /**
     * {@code ServicioSocios.consultarPerfilPropio(socioActual.obtener())} → {@code ResumenPerfil.desde(perfil)};
     * {@code ServicioCuotas.consultarSaldo(perfil.grupo.id): SaldoGrupo} → {@code ResumenSaldo.desde(saldo)}
     *
     * <p>Casos de uso: CU-05
     */
    public void cargar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
