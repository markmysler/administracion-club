package ar.edu.ub.club.socios.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de {@code Socio} (polimorfismo de la cuenta y validación de datos personales) y de
 * {@code CategoriaSocio}.
 *
 * <p>Casos de uso: CU-01, CU-02, CU-06, CU-16
 */
class SocioTest {

    /** Categoría de prueba. */
    private final CategoriaSocio categoria = CategoriaSocio.reconstituir(1L, "Activo", Dinero.de("10000"), true);

    /**
     * Datos válidos de un alta.
     *
     * <p>Casos de uso: CU-01
     * @return datos
     */
    private static DatosSocio datos() {
        DatosSocio datos = new DatosSocio();
        datos.setDni("30111222");
        datos.setNombre(" Lucía ");
        datos.setApellido("Gómez");
        datos.setFechaNacimiento(LocalDate.of(1995, 3, 4));
        datos.setTelefono("  ");
        datos.setEmail("lucia@club.test");
        datos.setUsername("lucia");
        return datos;
    }

    /**
     * El alta crea una cuenta activa de tipo Socio con los permisos del socio (CU-16, RNF-04).
     *
     * <p>Casos de uso: CU-01, CU-16
     */
    @Test
    void altaCreaCuentaActivaDeTipoSocio() {
        Socio socio = new Socio(datos(), categoria, "hash");
        assertTrue(socio.estaActiva());
        assertEquals("Socio", socio.tipoCuenta());
        assertTrue(socio.admiteSesionPersistente());
        assertEquals("Gómez, Lucía", socio.nombreCompleto());
        assertTrue(socio.obtenerPermisos().contains(Permiso.CONSULTAR_PERFIL_PROPIO));
        assertFalse(socio.obtenerPermisos().contains(Permiso.GESTIONAR_SOCIOS));
        assertEquals(null, socio.getTelefono());
        assertEquals(categoria, socio.getCategoria());
    }

    /**
     * El DNI debe tener 7 u 8 dígitos y la fecha de nacimiento debe ser anterior al alta (CU-01 FA3).
     *
     * <p>Casos de uso: CU-01, CU-02
     */
    @Test
    void rechazaDatosPersonalesInvalidos() {
        DatosSocio dniMalo = datos();
        dniMalo.setDni("12AB");
        ErrorNegocio error = assertThrows(ErrorNegocio.class, () -> new Socio(dniMalo, categoria, "hash"));
        assertEquals("dni", error.getCampo());
        DatosSocio futuro = datos();
        futuro.setFechaNacimiento(LocalDate.now().plusDays(3));
        assertEquals("fechaNacimiento",
                assertThrows(ErrorNegocio.class, () -> new Socio(futuro, categoria, "hash")).getCampo());
    }

    /**
     * La modificación aplica los datos y la nueva categoría (CU-02).
     *
     * <p>Casos de uso: CU-02
     */
    @Test
    void actualizarDatosCambiaCategoria() {
        Socio socio = new Socio(datos(), categoria, "hash");
        CategoriaSocio otra = CategoriaSocio.reconstituir(2L, "Menor", Dinero.de("4000"), true);
        DatosSocio cambios = datos();
        cambios.setApellido("Gómez Paz");
        socio.actualizarDatos(cambios, otra);
        assertEquals("Gómez Paz", socio.getApellido());
        assertEquals(otra, socio.getCategoria());
    }

    /**
     * Una categoría exige nombre y un importe mayor o igual a cero (CU-06 FA2).
     *
     * <p>Casos de uso: CU-06
     */
    @Test
    void categoriaValidaNombreEImporte() {
        assertEquals("importe",
                assertThrows(ErrorNegocio.class, () -> new CategoriaSocio("X", Dinero.de("-1"), true)).getCampo());
        assertEquals("nombre",
                assertThrows(ErrorNegocio.class, () -> new CategoriaSocio(" ", Dinero.de("1"), true)).getCampo());
        CategoriaSocio nueva = new CategoriaSocio("Vitalicio", Dinero.cero(), null);
        assertTrue(nueva.estaActiva());
        nueva.modificar("Vitalicio", Dinero.de("100"), false);
        assertFalse(nueva.estaActiva());
    }
}
