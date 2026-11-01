package ar.edu.ub.club.recursos.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.Deporte;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Fachada del módulo recursos (deportes y canchas). Las bajas obtienen las reservas futuras afectadas con la
 * consulta invertida {@code ConsultaReservasFuturas}, las informan antes de confirmar y, al confirmar, publican
 * un evento de dominio que hace cancelar esas reservas (RN-16). Así recursos no depende de reservas.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23, CU-26, CU-32, CU-38
 */
public class ServicioRecursos {

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta ({@code Deportes}, {@code DeportePorNombre}, {@code CanchasDeDeporte}, {@code CanchaPorNombre}). */
    private FachadaPersistencia fachadaPersistencia;

    /** Consulta invertida de reservas futuras por cancha o deporte. */
    private ConsultaReservasFuturas consultaReservasFuturas;

    /** Publica {@code CanchaDesactivadaEvento} y {@code DeporteDesactivadoEvento}. */
    private ApplicationEventPublisher publicadorEventos;

    /** Auditoría de altas, bajas y modificaciones de deportes y canchas. */
    private ServicioAuditoria servicioAuditoria;

    /** Verifica permisos. */
    private ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private UsuarioActual usuarioActual;

    /**
     * Devuelve los deportes.
     *
     * <p>Casos de uso: CU-18, CU-21, CU-26, CU-38
     * @return Lista de {@code Deporte}.
     */
    public List<Deporte> listarDeportes() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea un deporte activo con nombre único.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-18
     * @param datos Nombre y descripción.
     * @return {@code Deporte}.
     */
    public Deporte registrarDeporte(DatosDeporte datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Verifica {@code fachadaPersistencia.contar(DeportePorNombre(nombre, id)) &gt; 0}; {@code
     * deporte.modificar(nombre, descripcion)}; con {@code activo = true} invoca {@code activar()}. Audita.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-19
     * @param id Deporte.
     * @param datos Datos modificados.
     * @return {@code Deporte}.
     */
    public Deporte modificarDeporte(Long id, DatosDeporte datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Obtiene las reservas afectadas con {@code buscarFuturasDeDeporte(id)}; si hay y no se confirmó devuelve {@code
     * ResultadoBaja(false, reservasAfectadas)} sin cambios. Si se confirma (o no hay reservas afectadas), {@code
     * desactivar()} y publica {@code DeporteDesactivadoEvento}; {@code CanceladorReservasPorBaja} cancela las
     * reservas futuras de sus canchas en la misma transacción.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-20
     * @param id Deporte.
     * @param confirmar Confirmación tras la advertencia.
     * @return {@code ResultadoBaja}.
     */
    public ResultadoBaja desactivarDeporte(Long id, boolean confirmar) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve las canchas, opcionalmente de un deporte.
     *
     * <p>Casos de uso: CU-21, CU-26, CU-32
     * @param idDeporte Deporte (opcional).
     * @return Lista de {@code Cancha}.
     */
    public List<Cancha> listarCanchas(Long idDeporte) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea una cancha activa de un deporte activo, con nombre único.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-21
     * @param datos Nombre, deporte, ubicación y características.
     * @return {@code Cancha}.
     */
    public Cancha registrarCancha(DatosCancha datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Verifica {@code fachadaPersistencia.contar(CanchaPorNombre(nombre, id)) &gt; 0}; {@code
     * cancha.modificar(nombre, deporte, ubicacion, caracteristicas)}; con {@code activa = true} invoca {@code
     * activar()}, que exige el deporte activo. Si cambia el deporte y {@code buscarFuturasDeCancha(id)} devuelve
     * reservas, sin {@code datos.confirmar} lanza {@code ConfirmacionRequeridaException}; las reservas se conservan.
     * Audita.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-22
     * @param id Cancha.
     * @param datos Datos modificados.
     * @return {@code Cancha}.
     */
    public Cancha modificarCancha(Long id, DatosCancha datos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Como {@code desactivarDeporte}, con {@code buscarFuturasDeCancha(id)} y {@code CanchaDesactivadaEvento}.
     *
     * <p>Permiso: {@code GESTIONAR_RECURSOS}.
     *
     * <p>Casos de uso: CU-23
     * @param id Cancha.
     * @param confirmar Confirmación tras la advertencia.
     * @return {@code ResultadoBaja}.
     */
    public ResultadoBaja desactivarCancha(Long id, boolean confirmar) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
