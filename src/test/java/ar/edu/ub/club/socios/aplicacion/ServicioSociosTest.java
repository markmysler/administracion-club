package ar.edu.ub.club.socios.aplicacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ar.edu.ub.club.accesos.aplicacion.GestorSesiones;
import ar.edu.ub.club.accesos.aplicacion.UsuarioActual;
import ar.edu.ub.club.accesos.dominio.CodificadorClave;
import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.accesos.dominio.PoliticaContrasenas;
import ar.edu.ub.club.accesos.dominio.Usuario;
import ar.edu.ub.club.auditoria.aplicacion.ServicioAuditoria;
import ar.edu.ub.club.compartido.dominio.ConsultaReservasFuturas;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.dominio.ResumenReserva;
import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.Consulta;
import ar.edu.ub.club.compartido.persistencia.FachadaPersistencia;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.grupos.dominio.GrupoFamiliar;
import ar.edu.ub.club.grupos.persistencia.GrupoVigenteDeSocio;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import ar.edu.ub.club.socios.dominio.SocioDesactivadoEvento;
import ar.edu.ub.club.socios.persistencia.SocioPorDni;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Pruebas de las reglas de {@code ServicioSocios} sin base de datos: la fachada de persistencia y los
 * colaboradores de otros módulos son dobles de Mockito.
 *
 * <p>Casos de uso: CU-01, CU-03, CU-06
 */
class ServicioSociosTest {

    /** Doble de la fachada de persistencia. */
    private FachadaPersistencia fachada;

    /** Doble del publicador de eventos. */
    private ApplicationEventPublisher publicador;

    /** Doble del gestor de sesiones. */
    private GestorSesiones gestorSesiones;

    /** Doble de la consulta invertida de reservas futuras. */
    private ConsultaReservasFuturas reservasFuturas;

    /** Doble de la cuenta de la sesión (administrador con todos los permisos). */
    private Usuario administrador;

    /** Fachada bajo prueba. */
    private ServicioSocios servicio;

    /** Categoría activa. */
    private final CategoriaSocio categoria = CategoriaSocio.reconstituir(1L, "Activo", Dinero.de("10000"), true);

    /**
     * Arma la fachada con dobles de sus colaboradores.
     *
     * <p>Casos de uso: CU-01, CU-03, CU-06
     */
    @BeforeEach
    void preparar() {
        fachada = mock(FachadaPersistencia.class);
        publicador = mock(ApplicationEventPublisher.class);
        gestorSesiones = mock(GestorSesiones.class);
        reservasFuturas = mock(ConsultaReservasFuturas.class);
        administrador = mock(Usuario.class);
        when(administrador.obtenerPermisos()).thenReturn(EnumSet.allOf(Permiso.class));
        UsuarioActual usuarioActual = mock(UsuarioActual.class);
        when(usuarioActual.obtener()).thenReturn(administrador);
        CodificadorClave codificador = mock(CodificadorClave.class);
        when(codificador.codificar(anyString())).thenReturn("hash");
        servicio = new ServicioSocios(fachada, mock(ServicioGruposFamiliares.class), reservasFuturas, publicador,
                codificador, mock(PoliticaContrasenas.class), new ControlAcceso(), usuarioActual,
                mock(ServicioAuditoria.class), gestorSesiones);
    }

    /**
     * Socio persistido de prueba.
     *
     * <p>Casos de uso: CU-03
     * @param id identificador
     * @return socio activo
     */
    private Socio socio(Long id) {
        Socio socio = Socio.reconstituir(id, "u" + id, "u" + id + "@club.test", "hash", EstadoCuenta.ACTIVA, 0, null,
                LocalDateTime.of(2026, 1, 1, 9, 0), "2000000" + id, "N" + id, "A" + id, LocalDate.of(1990, 1, 1),
                null, null, LocalDate.of(2026, 1, 1));
        socio.restaurarAsociaciones(categoria, null);
        return socio;
    }

    /**
     * Un DNI ya registrado impide el alta y marca el campo {@code dni} (CU-01 FA5).
     *
     * <p>Casos de uso: CU-01
     */
    @Test
    void registrarSocioRechazaDniRegistrado() {
        DatosSocio datos = new DatosSocio();
        datos.setDni("30111222");
        datos.setUsername("nuevo_socio");
        datos.setEmail("nuevo@club.test");
        datos.setClaveInicial("una clave larga y segura");
        when(fachada.contar(any(SocioPorDni.class))).thenReturn(1L);
        ErrorNegocio error = assertThrows(ErrorNegocio.class, () -> servicio.registrarSocio(datos));
        assertEquals("dni", error.getCampo());
        verify(fachada, never()).put(any());
    }

    /**
     * Sin el permiso GESTIONAR_SOCIOS la fachada rechaza la operación (RNF-04).
     *
     * <p>Casos de uso: CU-01
     */
    @Test
    void registrarSocioExigePermiso() {
        when(administrador.obtenerPermisos()).thenReturn(EnumSet.of(Permiso.CONSULTAR_PERFIL_PROPIO));
        assertThrows(AccesoDenegadoException.class, () -> servicio.registrarSocio(new DatosSocio()));
    }

    /**
     * Primer paso de la baja: devuelve las reservas afectadas sin cambios (sec_cu03_p3).
     *
     * <p>Casos de uso: CU-03
     */
    @Test
    @SuppressWarnings("unchecked")
    void desactivarSinConfirmarNoCambiaNada() {
        Socio socio = socio(10L);
        GrupoFamiliar grupo = new GrupoFamiliar(LocalDate.of(2026, 1, 1));
        grupo.agregarIntegrante(socio);
        grupo.cambiarTitular(socio);
        List<ResumenReserva> afectadas = List.of(new ResumenReserva(1L, "A10, N10", "Cancha 1",
                LocalDate.of(2026, 12, 1), LocalTime.of(10, 0), LocalTime.of(11, 0)));
        when(fachada.get(10L, Socio.class)).thenReturn(socio);
        when(fachada.buscarUno(any(GrupoVigenteDeSocio.class))).thenReturn((Optional) Optional.of(grupo));
        when(reservasFuturas.buscarFuturasDeSocio(10L)).thenReturn(afectadas);
        ResultadoBaja resultado = servicio.desactivarSocio(10L, null, false);
        assertFalse(resultado.realizada());
        assertEquals(afectadas, resultado.reservasAfectadas());
        assertTrue(socio.estaActiva());
        verify(publicador, never()).publishEvent(any());
    }

    /**
     * Titular con otros integrantes activos: sin nuevo titular no hay baja (RN-06, CU-03 FA5); con nuevo titular
     * cambia el titular, desactiva, publica el evento y revoca las sesiones.
     *
     * <p>Casos de uso: CU-03
     */
    @Test
    @SuppressWarnings("unchecked")
    void desactivarTitularExigeNuevoTitular() {
        Socio titular = socio(10L);
        Socio otro = socio(11L);
        GrupoFamiliar grupo = new GrupoFamiliar(LocalDate.of(2026, 1, 1));
        grupo.agregarIntegrante(titular);
        grupo.cambiarTitular(titular);
        grupo.agregarIntegrante(otro);
        when(fachada.get(10L, Socio.class)).thenReturn(titular);
        when(fachada.get(11L, Socio.class)).thenReturn(otro);
        when(fachada.buscarUno(any(Consulta.class))).thenReturn((Optional) Optional.of(grupo));
        when(reservasFuturas.buscarFuturasDeSocio(10L)).thenReturn(List.of());
        assertEquals("idNuevoTitular",
                assertThrows(ErrorNegocio.class, () -> servicio.desactivarSocio(10L, null, true)).getCampo());
        ResultadoBaja resultado = servicio.desactivarSocio(10L, 11L, true);
        assertTrue(resultado.realizada());
        assertFalse(titular.estaActiva());
        assertTrue(grupo.esTitular(otro));
        verify(publicador).publishEvent(any(SocioDesactivadoEvento.class));
        verify(gestorSesiones).revocarTokensDe(titular);
        verify(gestorSesiones).invalidarSesionesDe(titular);
        verify(fachada).put(eq(titular));
    }

    /**
     * Un socio ya inactivo no se puede dar de baja otra vez (CU-03 FA1).
     *
     * <p>Casos de uso: CU-03
     */
    @Test
    void desactivarRechazaSocioInactivo() {
        Socio socio = socio(10L);
        socio.desactivar();
        when(fachada.get(10L, Socio.class)).thenReturn(socio);
        assertThrows(ErrorNegocio.class, () -> servicio.desactivarSocio(10L, null, false));
    }
}
