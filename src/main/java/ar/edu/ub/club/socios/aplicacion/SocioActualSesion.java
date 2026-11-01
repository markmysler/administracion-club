package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Implementación (socios.aplicacion) de {@code SocioActual}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class SocioActualSesion implements SocioActual {

    /** Obtiene el socio de la sesión. */
    private FachadaPersistencia fachadaPersistencia;

    /**
     * Toma el identificador del principal de la sesión y devuelve {@code fachadaPersistencia.get(id, Socio)}; si la
     * cuenta no es de un socio lanza {@code AccesoDenegadoException}.
     *
     * <p>Casos de uso: PENDIENTE
     * @return el resultado
     */
    public Socio obtener() {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
