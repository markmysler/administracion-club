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
 * <p>Casos de uso: PENDIENTE
 */
public class PerfilSocio {

    /** Atributo socio. */
    private Socio socio;

    /** Atributo grupo. */
    private GrupoFamiliar grupo;

    /** Atributo cuotaVigente. */
    private Cuota cuotaVigente;

    /** Atributo proximasReservas. */
    private List<ResumenReserva> proximasReservas;

    /**
     * Devuelve {@code socio}: socio.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code grupo}: grupo.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public GrupoFamiliar getGrupo() {
        return grupo;
    }

    /**
     * Devuelve {@code cuotaVigente}: cuotaVigente.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public Cuota getCuotaVigente() {
        return cuotaVigente;
    }

    /**
     * Devuelve {@code proximasReservas}: proximasReservas.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el valor del atributo
     */
    public List<ResumenReserva> getProximasReservas() {
        return proximasReservas;
    }
}
