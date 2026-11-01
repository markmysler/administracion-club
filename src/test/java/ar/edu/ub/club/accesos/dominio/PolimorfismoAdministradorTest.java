package ar.edu.ub.club.accesos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.excepciones.AccesoDenegadoException;
import ar.edu.ub.club.compartido.seguridad.Autorizable;
import ar.edu.ub.club.compartido.seguridad.ControlAcceso;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import org.junit.jupiter.api.Test;

/**
 * Pruebas del polimorfismo de {@code Administrador} ({@code pat_polimorfismo.puml}): permisos de administración,
 * sin sesión persistente y tipo de cuenta, consultados por {@code ControlAcceso} sin preguntar por la clase.
 *
 * <p>Casos de uso: CU-11, CU-16
 */
class PolimorfismoAdministradorTest {

    /** Cuenta como la ve {@code ControlAcceso}: solo {@code Autorizable}. */
    private final Usuario cuenta = new Administrador("Laura", "Gómez", "laura", "laura@club.local", "hash");

    /** Verificador de permisos. */
    private final ControlAcceso controlAcceso = new ControlAcceso();

    /**
     * Tiene los permisos de administración y ninguno de los de socio.
     *
     * <p>Casos de uso: CU-11, CU-16
     */
    @Test
    void permisosDeAdministracion() {
        Autorizable autorizable = cuenta;
        assertTrue(controlAcceso.tienePermiso(autorizable, Permiso.GESTIONAR_CUENTAS));
        assertTrue(controlAcceso.tienePermiso(autorizable, Permiso.CONSULTAR_DASHBOARD));
        assertTrue(controlAcceso.tienePermiso(autorizable, Permiso.CONFIGURAR_CLUB));
        assertTrue(controlAcceso.tienePermiso(autorizable, Permiso.CONSULTAR_DISPONIBILIDAD));
        assertFalse(controlAcceso.tienePermiso(autorizable, Permiso.RESERVAR_ONLINE));
        assertFalse(controlAcceso.tienePermiso(autorizable, Permiso.PAGAR_CUOTA_ONLINE));
        assertThrows(AccesoDenegadoException.class,
                () -> controlAcceso.verificar(autorizable, Permiso.CONSULTAR_PERFIL_PROPIO));
        assertEquals(14, cuenta.obtenerPermisos().size());
    }

    /**
     * No admite sesión persistente, su tipo es "Administrador" y su nombre "Apellido, Nombre".
     *
     * <p>Casos de uso: CU-11, CU-16
     */
    @Test
    void sesionPersistenteTipoYNombre() {
        assertFalse(cuenta.admiteSesionPersistente());
        assertEquals("Administrador", cuenta.tipoCuenta());
        assertEquals("Gómez, Laura", cuenta.nombreCompleto());
    }

    /**
     * Los permisos no se pueden modificar desde afuera.
     *
     * <p>Casos de uso: CU-16
     */
    @Test
    void permisosInmutables() {
        assertThrows(UnsupportedOperationException.class, () -> cuenta.obtenerPermisos().add(Permiso.RESERVAR_ONLINE));
    }
}
