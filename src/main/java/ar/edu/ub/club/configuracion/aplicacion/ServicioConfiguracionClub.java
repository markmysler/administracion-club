package ar.edu.ub.club.configuracion.aplicacion;

import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.configuracion.dominio.ConfiguracionClub;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Fachada (GoF Facade) de los datos institucionales del club (fila única). Controlador del caso de uso CU-41.
 *
 * <p>Clase anotada con {@code @Service} y {@code @Transactional}: cada método público es una transacción (los de
 * consulta con {@code readOnly = true}). La primera instrucción de cada método con permiso es
 * {@code controlAcceso.verificar(usuario, permiso)}.
 *
 * <p>Casos de uso: CU-41
 */
@Service
@Transactional
public class ServicioConfiguracionClub {

    /** Serializa el detalle de auditoría. */
    private static final ObjectMapper JSON = new ObjectMapper();

    /** Único punto de acceso a la persistencia («Singleton»): {@code get}, {@code put} y los objetos consulta (fila única por {@code get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)}). */
    private final FachadaPersistencia fachadaPersistencia;

    /** Auditoría de la modificación (CU-41). */
    private final ServicioAuditoria servicioAuditoria;

    /** Verifica permisos. */
    private final ControlAcceso controlAcceso;

    /** Cuenta de la sesión vigente. */
    private final UsuarioActual usuarioActual;

    /**
     * Crea la fachada.
     *
     * <p>Casos de uso: CU-41
     * @param fachadaPersistencia fachada de persistencia
     * @param servicioAuditoria auditoría
     * @param controlAcceso verificación de permisos
     * @param usuarioActual cuenta de la sesión
     */
    public ServicioConfiguracionClub(FachadaPersistencia fachadaPersistencia, ServicioAuditoria servicioAuditoria,
                                     ControlAcceso controlAcceso, UsuarioActual usuarioActual) {
        this.fachadaPersistencia = fachadaPersistencia;
        this.servicioAuditoria = servicioAuditoria;
        this.controlAcceso = controlAcceso;
        this.usuarioActual = usuarioActual;
    }

    /**
     * Devuelve los datos del club ({@code get(ConfiguracionClub.ID_UNICO, ConfiguracionClub)}). Sin permiso: lo
     * usa también el encabezado de todas las pantallas.
     *
     * <p>Casos de uso: CU-41
     * @return {@code ConfiguracionClub}.
     */
    @Transactional(readOnly = true)
    public ConfiguracionClub consultar() {
        return fachadaPersistencia.get(ConfiguracionClub.ID_UNICO, ConfiguracionClub.class);
    }

    /**
     * Valida y actualiza los datos; audita ({@code sec_cu41_p3.puml}). Reglas: nombre obligatorio (hasta 100
     * caracteres), dirección hasta 150, teléfono con dígitos, espacios, +, guion o paréntesis (hasta 30) y correo con
     * formato válido (hasta 120).
     *
     * <p>Permiso: {@code CONFIGURAR_CLUB}.
     *
     * <p>Casos de uso: CU-41
     * @param datos Nombre, dirección, teléfono y correo.
     * @return {@code ConfiguracionClub}.
     * @throws ErrorNegocio con el campo y el motivo si un dato no es válido (FA2)
     */
    public ConfiguracionClub modificar(DatosConfiguracionClub datos) {
        Usuario actor = usuarioActual.obtener();
        controlAcceso.verificar(actor, Permiso.CONFIGURAR_CLUB);
        String nombre = recortar(datos.getNombre());
        String direccion = recortar(datos.getDireccion());
        String telefono = recortar(datos.getTelefono());
        String email = recortar(datos.getEmail());
        if (nombre == null) {
            throw new ErrorNegocio("nombre", "Ingrese el nombre del club.");
        }
        if (nombre.length() > 100) {
            throw new ErrorNegocio("nombre", "El nombre no puede superar los 100 caracteres.");
        }
        if (direccion != null && direccion.length() > 150) {
            throw new ErrorNegocio("direccion", "La dirección no puede superar los 150 caracteres.");
        }
        if (telefono != null && (telefono.length() > 30 || !telefono.matches("^[0-9 +()\\-]+$"))) {
            throw new ErrorNegocio("telefono", "El teléfono solo admite dígitos, espacios, +, guiones y paréntesis.");
        }
        if (email != null && (email.length() > 120 || !email.matches("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$"))) {
            throw new ErrorNegocio("email", "El correo electrónico no tiene un formato válido.");
        }
        ConfiguracionClub configuracion = fachadaPersistencia.get(ConfiguracionClub.ID_UNICO, ConfiguracionClub.class);
        configuracion.actualizar(nombre, direccion, telefono, email);
        fachadaPersistencia.put(configuracion);
        Map<String, Object> detalle = new LinkedHashMap<>();
        detalle.put("nombre", nombre);
        detalle.put("direccion", direccion);
        detalle.put("telefono", telefono);
        detalle.put("email", email);
        servicioAuditoria.registrar(actor, "MODIFICACION", "ConfiguracionClub", ConfiguracionClub.ID_UNICO,
                json(detalle));
        return configuracion;
    }

    /**
     * Quita los espacios de los extremos y convierte el texto vacío en {@code null}.
     *
     * <p>Casos de uso: CU-41
     * @param valor texto ingresado
     * @return texto normalizado o {@code null}
     */
    private static String recortar(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        return valor.strip();
    }

    /**
     * Serializa el detalle de auditoría como JSON.
     *
     * <p>Casos de uso: CU-41
     * @param detalle pares nombre → valor
     * @return JSON
     */
    private static String json(Map<String, Object> detalle) {
        try {
            return JSON.writeValueAsString(detalle);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("No se pudo serializar el detalle de auditoría", e);
        }
    }
}
