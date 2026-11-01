package ar.edu.ub.club.configuracion.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;

/**
 * Fachada de los datos institucionales del club (fila única).
 *
 * <p>Casos de uso: CU-05, CU-37, CU-41
 */
public class ServicioConfiguracionClub {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta (fila única por {@code get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Auditoría de la modificación (CU-41). */
    private ServicioAuditoria servicioAuditoria;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private UsuarioActual usuarioActual;

    /**
     * Devuelve los datos del club.
     *
     * <p>Casos de uso: CU-05, CU-37, CU-41
     * @return {@code ConfiguracionClub}.
     */
    public ConfiguracionClub consultar() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Valida y actualiza los datos; audita.
     *
     * <p>Permiso: {@code CONFIGURAR_CLUB}.
     *
     * <p>Casos de uso: CU-41
     * @param datos Nombre, dirección, teléfono y correo.
     * @return {@code ConfiguracionClub}.
     */
    public ConfiguracionClub modificar(DatosConfiguracionClub datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
