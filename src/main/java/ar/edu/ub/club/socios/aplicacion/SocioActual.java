package ar.edu.ub.club.socios.aplicacion;

import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Interfaz (puerto) que resuelve el socio autenticado. Su implementación {@code SocioActualSesion} (socios.aplicacion)
 * obtiene el socio con {@code fachadaPersistencia.get(id, Socio)}; si la cuenta no es de un socio lanza {@code
 * AccesoDenegadoException}. Evita que la vista envíe el identificador del socio (RN-20).
 *
 * <p>Casos de uso: CU-05, CU-27, CU-29, CU-30, CU-31, CU-32, CU-35
 */
public interface SocioActual {

    /**
     * Devuelve el socio de la sesión.
     *
     * <p>Casos de uso: CU-05, CU-27, CU-29, CU-30, CU-31, CU-32, CU-35
     * @return {@code Socio}.
     */
    Socio obtener();
}
