package ar.edu.ub.club.grupos.dominio;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import ar.edu.ub.club.accesos.dominio.EstadoCuenta;
import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.cuotas.dominio.TipoMovimientoSaldo;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Pruebas unitarias (sin base de datos) de {@code GrupoFamiliar}, Information Expert de la composición del grupo,
 * de la titularidad (RN-05, RN-06, RN-07), del importe de la cuota y del saldo a favor (RN-21).
 *
 * <p>Casos de uso: CU-01, CU-03, CU-07, CU-08, CU-09, CU-10; tareas programadas: emisión mensual de cuotas
 */
class GrupoFamiliarTest {

    /** Categoría de $ 10.000. */
    private final CategoriaSocio activo = CategoriaSocio.reconstituir(1L, "Activo", Dinero.de("10000.00"), true);

    /** Categoría de $ 4.000. */
    private final CategoriaSocio menor = CategoriaSocio.reconstituir(2L, "Menor", Dinero.de("4000.00"), true);

    /** Titular del grupo. */
    private Socio ana;

    /** Integrante activo. */
    private Socio beto;

    /** Integrante inactivo. */
    private Socio carla;

    /** Grupo de prueba con los tres socios y Ana como titular. */
    private GrupoFamiliar grupo;

    /**
     * Arma un grupo con un titular, un integrante activo y uno inactivo.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     */
    @BeforeEach
    void preparar() {
        ana = socio(10L, "Ana", EstadoCuenta.ACTIVA, activo);
        beto = socio(11L, "Beto", EstadoCuenta.ACTIVA, menor);
        carla = socio(12L, "Carla", EstadoCuenta.ACTIVA, activo);
        grupo = new GrupoFamiliar(LocalDate.of(2026, 1, 1));
        grupo.agregarIntegrante(ana);
        grupo.cambiarTitular(ana);
        grupo.agregarIntegrante(beto);
        grupo.agregarIntegrante(carla);
        carla.desactivar();
    }

    /**
     * Crea un socio persistido de prueba con {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param nombre nombre
     * @param estado estado de la cuenta
     * @param categoria categoría
     * @return socio
     */
    private static Socio socio(Long id, String nombre, EstadoCuenta estado, CategoriaSocio categoria) {
        Socio socio = Socio.reconstituir(id, nombre.toLowerCase() + "_usuario", nombre.toLowerCase() + "@club.test",
                "hash", estado, 0, null, LocalDateTime.of(2026, 1, 1, 10, 0), "3000000" + id, nombre, "Pérez",
                LocalDate.of(1990, 5, 1), null, null, LocalDate.of(2026, 1, 1));
        socio.restaurarAsociaciones(categoria, null);
        return socio;
    }

    /**
     * Agregar un integrante lo incorpora y le asigna el grupo (asociación en ambos sentidos).
     *
     * <p>Casos de uso: CU-07
     */
    @Test
    void agregarIntegranteAsignaElGrupoAlSocio() {
        Socio nuevo = socio(20L, "Dario", EstadoCuenta.ACTIVA, menor);
        grupo.agregarIntegrante(nuevo);
        assertTrue(grupo.getIntegrantes().contains(nuevo));
        assertSame(grupo, nuevo.getGrupoFamiliar());
    }

    /**
     * Un socio inactivo o que ya es integrante no se puede agregar (CU-07 FA2).
     *
     * <p>Casos de uso: CU-07
     */
    @Test
    void agregarIntegranteRechazaInactivoYRepetido() {
        Socio inactivo = socio(21L, "Eva", EstadoCuenta.INACTIVA, menor);
        assertThrows(ErrorNegocio.class, () -> grupo.agregarIntegrante(inactivo));
        assertThrows(ErrorNegocio.class, () -> grupo.agregarIntegrante(beto));
    }

    /**
     * Solo un integrante activo puede ser titular (RN-05, CU-08 FA2).
     *
     * <p>Casos de uso: CU-03, CU-08
     */
    @Test
    void cambiarTitularExigeIntegranteActivo() {
        grupo.cambiarTitular(beto);
        assertTrue(grupo.esTitular(beto));
        assertThrows(ErrorNegocio.class, () -> grupo.cambiarTitular(carla));
        assertThrows(ErrorNegocio.class, () -> grupo.cambiarTitular(socio(30L, "Fede", EstadoCuenta.ACTIVA, menor)));
        assertTrue(grupo.esTitular(beto));
    }

    /**
     * El titular no se puede quitar mientras haya otros integrantes activos (RN-06); un integrante no titular sí.
     *
     * <p>Casos de uso: CU-07, CU-09
     */
    @Test
    void quitarIntegranteRespetaAlTitular() {
        assertThrows(ErrorNegocio.class, () -> grupo.quitarIntegrante(ana));
        grupo.quitarIntegrante(beto);
        assertFalse(grupo.getIntegrantes().contains(beto));
        assertTrue(grupo.getVigente());
        assertThrows(ErrorNegocio.class, () -> grupo.quitarIntegrante(beto));
    }

    /**
     * Un grupo unipersonal cuyo único integrante se va queda no vigente y conserva su titular como historia (RN-07).
     *
     * <p>Casos de uso: CU-07
     */
    @Test
    void grupoSinIntegrantesDejaDeEstarVigente() {
        GrupoFamiliar unipersonal = new GrupoFamiliar(LocalDate.of(2026, 2, 1));
        Socio solo = socio(40L, "Gabi", EstadoCuenta.ACTIVA, activo);
        unipersonal.agregarIntegrante(solo);
        unipersonal.cambiarTitular(solo);
        unipersonal.quitarIntegrante(solo);
        assertFalse(unipersonal.getVigente());
        assertTrue(unipersonal.getIntegrantes().isEmpty());
        assertSame(solo, unipersonal.getTitular());
    }

    /**
     * El importe de la cuota suma solo las categorías de los integrantes activos.
     *
     * <p>Casos de uso: CU-10; tareas programadas: emisión mensual de cuotas
     */
    @Test
    void calcularImporteCuotaSumaSoloActivos() {
        assertEquals(List.of(ana, beto), grupo.integrantesActivos());
        assertEquals(Dinero.de("14000.00"), grupo.calcularImporteCuota());
    }

    /**
     * Titular con otros integrantes activos: condición de CU-03 paso 3 y CU-07 paso 7.
     *
     * <p>Casos de uso: CU-03, CU-07
     */
    @Test
    void detectaTitularConOtrosIntegrantesActivos() {
        assertTrue(grupo.tieneOtrosIntegrantesActivos(ana));
        grupo.quitarIntegrante(beto);
        assertFalse(grupo.tieneOtrosIntegrantesActivos(ana));
    }

    /**
     * El saldo a favor es créditos menos débitos (RN-21).
     *
     * <p>Casos de uso: CU-05, CU-37
     */
    @Test
    void saldoAFavorEsCreditosMenosDebitos() {
        List<MovimientoSaldo> movimientos = new ArrayList<>();
        movimientos.add(MovimientoSaldo.reconstituir(1L, 5L, LocalDateTime.now(), TipoMovimientoSaldo.CREDITO,
                Dinero.de("5000.00"), "Pago duplicado", 9L));
        movimientos.add(MovimientoSaldo.reconstituir(2L, 5L, LocalDateTime.now(), TipoMovimientoSaldo.DEBITO,
                Dinero.de("1500.00"), "Aplicado a cuota", null));
        GrupoFamiliar conSaldo = GrupoFamiliar.reconstituir(5L, LocalDate.of(2026, 1, 1), true, new ArrayList<>(),
                new ArrayList<>(), movimientos);
        assertEquals(Dinero.de("3500.00"), conSaldo.saldoAFavor());
    }

    /**
     * Emitir la cuota crea un ítem por integrante activo y aplica como crédito el mínimo entre saldo e importe.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     */
    @Test
    void emitirCuotaCreaUnItemPorIntegranteActivo() {
        grupo.asignarId(7L);
        Cuota cuota = grupo.emitirCuota("2026-11", FechasCuota.delPeriodo(YearMonth.of(2026, 11)), Dinero.de("20000.00"));
        assertEquals(2, cuota.getItems().size());
        assertEquals(Dinero.de("14000.00"), cuota.getImporte());
        assertEquals(Dinero.de("14000.00"), cuota.getCreditoAplicado());
        assertEquals(7L, cuota.getIdGrupo());
        assertTrue(grupo.getCuotas().contains(cuota));
    }
}
