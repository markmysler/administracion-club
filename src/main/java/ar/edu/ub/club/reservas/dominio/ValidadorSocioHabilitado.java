package ar.edu.ub.club.reservas.dominio;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;

/**
 * Eslabón concreto de la Cadena de Responsabilidad (posición 1) de RN-11 y RN-08: el socio debe estar activo y,
 * salvo en una modificación del administrador, su grupo familiar no debe tener una cuota vencida.
 *
 * <p>Casos de uso: CU-27, CU-28, CU-32
 */
public class ValidadorSocioHabilitado extends ValidadorReservaEncadenado {

    /** Grupo vigente del socio ({@code null} si no tiene grupo vigente: no hay cuota que controlar). */
    private final GrupoFamiliar grupo;

    /**
     * Crea el validador con el grupo que obtuvo la fábrica.
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param grupo grupo vigente del socio
     */
    public ValidadorSocioHabilitado(GrupoFamiliar grupo) {
        this.grupo = grupo;
    }

    /**
     * Si {@code s.socio.estaActiva()} es false lanza {@code ErrorNegocio} («La cuenta del socio está inactiva»).
     * Si {@code s.controlaDeuda()} y {@code grupo.tieneCuotaVencida()} lanza {@code ErrorNegocio} con el mensaje
     * según el origen (socio: puede pagarla online; administrador: registre antes su pago).
     *
     * <p>Casos de uso: CU-27, CU-28, CU-32
     * @param s solicitud
     * @throws ErrorNegocio si el socio no está habilitado
     */
    @Override
    protected void verificar(SolicitudReserva s) {
        if (!s.getSocio().estaActiva()) {
            throw new ErrorNegocio("La cuenta del socio está inactiva.");
        }
        if (s.controlaDeuda() && grupo != null && grupo.tieneCuotaVencida()) {
            if (s.getOrigen() == OrigenSolicitud.SOCIO) {
                throw new ErrorNegocio("El grupo familiar tiene una cuota vencida; el titular puede pagarla online "
                        + "o en la administración.");
            }
            throw new ErrorNegocio("El grupo familiar del socio tiene una cuota vencida; registre antes su pago.");
        }
    }
}
