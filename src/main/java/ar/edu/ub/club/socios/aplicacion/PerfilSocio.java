package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.socios.dominio.Socio;
import java.util.List;

/**
 * Objeto de datos de salida de {@code ServicioSocios.consultarPerfilPropio} (CU-05): el socio de la sesión, su
 * grupo vigente, la última cuota emitida del grupo (o {@code null}) y sus próximas reservas. Lo convierte
 * {@code ResumenPerfil.desde(perfil)}. Inmutable, sin comportamiento.
 *
 * <p>Objeto de comando o de datos de la capa de aplicación.
 *
 * <p>Casos de uso: CU-05
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
     * Crea el perfil con todos sus datos.
     *
     * <p>Agregado en la implementación: constructor (el modelo no lo declaraba; el diagrama
     * {@code sec_cu05_p1} lo dibuja como {@code PerfilSocio(socio, g, cuotaVigente, proximasReservas)}).
     *
     * <p>Casos de uso: CU-05
     * @param socio socio de la sesión
     * @param grupo grupo vigente
     * @param cuotaVigente última cuota emitida o {@code null} (CU-05 FA1)
     * @param proximasReservas reservas futuras confirmadas
     */
    public PerfilSocio(Socio socio, GrupoFamiliar grupo, Cuota cuotaVigente, List<ResumenReserva> proximasReservas) {
        this.socio = socio;
        this.grupo = grupo;
        this.cuotaVigente = cuotaVigente;
        this.proximasReservas = proximasReservas;
    }

    /**
     * Devuelve {@code socio}: socio.
     *
     * <p>Casos de uso: CU-05
     * @return el valor del atributo
     */
    public Socio getSocio() {
        return socio;
    }

    /**
     * Devuelve {@code grupo}: grupo.
     *
     * <p>Casos de uso: CU-05
     * @return el valor del atributo
     */
    public GrupoFamiliar getGrupo() {
        return grupo;
    }

    /**
     * Devuelve {@code cuotaVigente}: cuotaVigente.
     *
     * <p>Casos de uso: CU-05
     * @return el valor del atributo
     */
    public Cuota getCuotaVigente() {
        return cuotaVigente;
    }

    /**
     * Devuelve {@code proximasReservas}: proximasReservas.
     *
     * <p>Casos de uso: CU-05
     * @return el valor del atributo
     */
    public List<ResumenReserva> getProximasReservas() {
        return proximasReservas;
    }
}
