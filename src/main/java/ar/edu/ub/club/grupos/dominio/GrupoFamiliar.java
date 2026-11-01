package ar.edu.ub.club.grupos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.util.List;

/**
 * Entidad de {@code grupos.dominio}. Information Expert de su composición, de su cuota, de su deuda y de su
 * saldo a favor, y Creator de {@code Cuota}. Navega sus cuotas por la asociación {@code cuotas}, lo que le
 * permite responder {@code tieneCuotaVencida()}. Las colecciones {@code integrantes}, {@code cuotas} y {@code
 * movimientos} se cargan en el primer acceso mediante un {@code ProxyListaVirtual} (Virtual Proxy) que ejecuta
 * la consulta correspondiente ({@code SociosDeGrupo}, {@code CuotasDeGrupo}, {@code MovimientosDeGrupo}) con
 * {@code FachadaPersistencia}; el dominio solo las ve como {@code List}.
 *
 * <p>Casos de uso: todos
 */
public class GrupoFamiliar extends EntidadPersistente {

    /** Creación del grupo. */
    private LocalDate fechaCreacion;

    /** Indica si tiene integrantes y recibe cuotas. */
    private Boolean vigente;

    /** Integrante titular. */
    private Socio titular;

    /** Socios del grupo. */
    private List<Socio> integrantes;

    /** Cuotas emitidas al grupo. */
    private List<Cuota> cuotas;

    /** Créditos y débitos del saldo a favor. */
    private List<MovimientoSaldo> movimientos;

    /**
     * Crea una instancia de {@code GrupoFamiliar}.
     *
     * <p>Casos de uso: CU-09
     * @param fechaCreacion valor de {@code fechaCreacion}
     */
    public GrupoFamiliar(LocalDate fechaCreacion) {
        // PENDIENTE
    }

    /**
     * Incorpora un socio activo.
     *
     * <p>Casos de uso: CU-07, CU-09
     * @param socio Socio.
     */
    public void agregarIntegrante(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Quita un integrante; rechaza al titular si quedan otros integrantes activos.
     *
     * <p>Casos de uso: CU-07, CU-09
     * @param socio Socio.
     */
    public void quitarIntegrante(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Designa titular a un integrante activo.
     *
     * <p>Casos de uso: CU-03, CU-08, CU-09
     * @param socio Nuevo titular.
     */
    public void cambiarTitular(Socio socio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Integrantes con cuenta activa.
     *
     * <p>Casos de uso: todos
     * @return Lista de {@code Socio}.
     */
    public List<Socio> integrantesActivos() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Suma de los importes de categoría de los integrantes activos.
     *
     * <p>Casos de uso: CU-10; tareas programadas: emisión mensual de cuotas
     * @return Importe.
     */
    public Dinero calcularImporteCuota() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Suma de créditos menos suma de débitos (RN-21).
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return Saldo mayor o igual a 0.
     */
    public Dinero saldoAFavor() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Crea la {@code Cuota} con un {@code ItemCuota} por integrante activo, {@code creditoAplicado = min(saldo,
     * importe)}, y la agrega a {@code cuotas}.
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo AAAA-MM.
     * @param fechas Emisión y vencimiento.
     * @param saldo Saldo a favor disponible.
     * @return {@code Cuota} emitida.
     */
    public Cuota emitirCuota(String periodo, FechasCuota fechas, Dinero saldo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Recorre {@code cuotas} y pregunta {@code estaVencida()} a cada una; lo usa {@code ValidadorSocioHabilitado}
     * (RN-11).
     *
     * <p>Casos de uso: CU-04
     * @return true si alguna cuota está vencida.
     */
    public boolean tieneCuotaVencida() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param id valor de {@code id}
     * @param fechaCreacion valor de {@code fechaCreacion}
     * @param vigente valor de {@code vigente}
     * @param integrantes valor de {@code integrantes}
     * @param cuotas valor de {@code cuotas}
     * @param movimientos valor de {@code movimientos}
     * @return el resultado
     */
    public static GrupoFamiliar reconstituir(Long id, LocalDate fechaCreacion, Boolean vigente, List<Socio> integrantes, List<Cuota> cuotas, List<MovimientoSaldo> movimientos) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: todos
     * @param titular valor de {@code titular}
     */
    public void restaurarAsociaciones(Socio titular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Devuelve {@code fechaCreacion}: Creación del grupo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    /**
     * Devuelve {@code vigente}: Indica si tiene integrantes y recibe cuotas.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Boolean getVigente() {
        return vigente;
    }

    /**
     * Devuelve {@code titular}: Integrante titular.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public Socio getTitular() {
        return titular;
    }

    /**
     * Devuelve {@code integrantes}: Socios del grupo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public List<Socio> getIntegrantes() {
        return integrantes;
    }

    /**
     * Devuelve {@code cuotas}: Cuotas emitidas al grupo.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public List<Cuota> getCuotas() {
        return cuotas;
    }

    /**
     * Devuelve {@code movimientos}: Créditos y débitos del saldo a favor.
     *
     * <p>Casos de uso: todos
     * @return el valor del atributo
     */
    public List<MovimientoSaldo> getMovimientos() {
        return movimientos;
    }
}
