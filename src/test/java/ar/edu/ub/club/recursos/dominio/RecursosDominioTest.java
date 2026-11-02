package ar.edu.ub.club.recursos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias de las reglas de {@code Deporte} y {@code Cancha} (CU-18 a CU-23), sin base de datos.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20, CU-21, CU-22, CU-23
 */
class RecursosDominioTest {

    /**
     * Un deporte nuevo queda activo; nombre obligatorio de hasta 40 caracteres; descripción vacía → null.
     *
     * <p>Casos de uso: CU-18
     */
    @Test
    void deporteNuevo() {
        Deporte tenis = new Deporte("  Tenis ", "  ");
        assertTrue(tenis.estaActivo());
        assertEquals("Tenis", tenis.getNombre());
        assertNull(tenis.getDescripcion());
        assertEquals("nombre", assertThrows(ErrorNegocio.class, () -> new Deporte("", null)).getCampo());
        assertThrows(ErrorNegocio.class, () -> new Deporte("x".repeat(41), null));
    }

    /**
     * Desactivar dos veces falla (CU-20 FA1); activar lo reactiva (CU-19 FA1).
     *
     * <p>Casos de uso: CU-19, CU-20
     */
    @Test
    void deporteDesactivarYActivar() {
        Deporte padel = new Deporte("Pádel", null);
        padel.desactivar();
        assertFalse(padel.estaActivo());
        assertThrows(ErrorNegocio.class, padel::desactivar);
        padel.activar();
        assertTrue(padel.estaActivo());
    }

    /**
     * Una cancha nueva queda activa y disponible si su deporte está activo; deja de estarlo si el deporte se
     * desactiva (RN-11).
     *
     * <p>Casos de uso: CU-20, CU-21
     */
    @Test
    void canchaDisponibleSegunSuDeporte() {
        Deporte futbol = new Deporte("Fútbol 5", null);
        Cancha cancha = new Cancha("Cancha 4", futbol, "Fondo", "Sintético");
        assertTrue(cancha.estaActivo());
        assertTrue(cancha.estaDisponibleParaReservar());
        futbol.desactivar();
        assertTrue(cancha.estaActivo());
        assertFalse(cancha.estaDisponibleParaReservar());
    }

    /**
     * La cancha exige deporte; la reactivación exige el deporte activo (CU-22 FA1); desactivar dos veces falla
     * (CU-23 FA1).
     *
     * <p>Casos de uso: CU-21, CU-22, CU-23
     */
    @Test
    void canchaActivarYDesactivar() {
        assertEquals("idDeporte", assertThrows(ErrorNegocio.class, () -> new Cancha("C", null, null, null)).getCampo());
        Deporte tenis = new Deporte("Tenis", null);
        Cancha cancha = new Cancha("Cancha 1", tenis, null, null);
        cancha.desactivar();
        assertThrows(ErrorNegocio.class, cancha::desactivar);
        tenis.desactivar();
        assertThrows(ErrorNegocio.class, cancha::activar);
        tenis.activar();
        cancha.activar();
        assertTrue(cancha.estaDisponibleParaReservar());
    }

    /**
     * Modificar cambia los datos y el deporte y valida longitudes (CU-22).
     *
     * <p>Casos de uso: CU-22
     */
    @Test
    void canchaModificar() {
        Cancha cancha = new Cancha("Cancha 1", new Deporte("Tenis", null), null, null);
        Deporte padel = new Deporte("Pádel", null);
        cancha.modificar("Cancha 1 bis", padel, "Norte", null);
        assertEquals("Cancha 1 bis", cancha.getNombre());
        assertEquals(padel, cancha.getDeporte());
        assertEquals("ubicacion", assertThrows(ErrorNegocio.class,
                () -> cancha.modificar("C", padel, "x".repeat(101), null)).getCampo());
    }
}
