package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.compartido.excepciones.RecursoNoEncontradoException;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import java.util.List;
import java.util.Set;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * {@code UserDetailsService} mínimo que necesita {@code PersistentTokenBasedRememberMeServices} para reconstruir la
 * cuenta de un token persistente (CU-11 FA1, {@code sec_cu11_p4.puml}). Adapter entre Spring Security y el dominio:
 * el «username» que recibe es el id de la cuenta en texto (el nombre del principal, ver {@code GestorSesiones}).
 * Solo acepta cuentas activas que admiten sesión persistente (socios); con cualquier otra Spring Security borra la
 * cookie y la petición sigue sin sesión.
 *
 * <p>También fabrica el principal de la sesión ({@code principalDe}) para que el ingreso por formulario
 * ({@code RegistroSesionVaadin}) y el ingreso por remember-me usen el mismo principal.
 *
 * <p>Agregado en la implementación: el diseño nombra el mecanismo remember-me pero no esta clase.
 *
 * <p>Casos de uso: CU-11, CU-12
 */
@Component
public class CuentasParaRecordarme implements UserDetailsService {

    /** Acceso a la cuenta. */
    private final FachadaPersistencia fachadaPersistencia;

    /**
     * Crea el servicio.
     *
     * <p>Casos de uso: CU-11
     * @param fachadaPersistencia fachada de persistencia
     */
    public CuentasParaRecordarme(FachadaPersistencia fachadaPersistencia) {
        this.fachadaPersistencia = fachadaPersistencia;
    }

    /**
     * Obtiene la cuenta por id ({@code fachadaPersistencia.get(id, Usuario)}) y arma su principal si está activa y
     * admite sesión persistente (polimorfismo: solo {@code Socio} devuelve verdadero).
     *
     * <p>Casos de uso: CU-11
     * @param idTexto id de la cuenta en texto
     * @return principal de la sesión
     * @throws UsernameNotFoundException si la cuenta no existe, está inactiva o no admite sesión persistente
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String idTexto) {
        Usuario usuario;
        try {
            usuario = fachadaPersistencia.get(Long.valueOf(idTexto), Usuario.class);
        } catch (NumberFormatException | RecursoNoEncontradoException e) {
            throw new UsernameNotFoundException("Cuenta inexistente");
        }
        if (!usuario.estaActiva() || !usuario.admiteSesionPersistente()) {
            throw new UsernameNotFoundException("La cuenta no admite sesión persistente");
        }
        return principalDe(usuario.getId(), usuario.obtenerPermisos());
    }

    /**
     * Principal de la sesión: {@code User} de Spring Security cuyo nombre es el id de la cuenta y cuyas
     * autoridades son los nombres de los permisos (informativas: la autorización la hace {@code ControlAcceso}).
     *
     * <p>Casos de uso: CU-11
     * @param idUsuario id de la cuenta
     * @param permisos permisos de la cuenta
     * @return principal
     */
    public static UserDetails principalDe(Long idUsuario, Set<Permiso> permisos) {
        List<SimpleGrantedAuthority> autoridades = permisos.stream()
                .map(p -> new SimpleGrantedAuthority(p.name()))
                .toList();
        return new User(String.valueOf(idUsuario), "", autoridades);
    }
}
