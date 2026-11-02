package ar.edu.ub.club.grupos.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.cuotas.dominio.Cuota;
import ar.edu.ub.club.cuotas.dominio.FechasCuota;
import ar.edu.ub.club.cuotas.dominio.ItemCuota;
import ar.edu.ub.club.cuotas.dominio.MovimientoSaldo;
import ar.edu.ub.club.cuotas.dominio.TipoMovimientoSaldo;
import ar.edu.ub.club.socios.dominio.Socio;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entidad de {@code grupos.dominio}. Information Expert de su composición, de su cuota, de su deuda y de su
 * saldo a favor, y Creator de {@code Cuota}. Navega sus cuotas por la asociación {@code cuotas}, lo que le
 * permite responder {@code tieneCuotaVencida()}. Las colecciones {@code integrantes}, {@code cuotas} y {@code
 * movimientos} se cargan en el primer acceso mediante un {@code ProxyListaVirtual} (Virtual Proxy) que ejecuta
 * la consulta correspondiente ({@code SociosDeGrupo}, {@code CuotasDeGrupo}, {@code MovimientosDeGrupo}) con
 * {@code FachadaPersistencia}; el dominio solo las ve como {@code List}.
 *
 * <p>Reglas que custodia: el titular es un integrante activo (RN-05); el titular no puede quitarse mientras
 * queden otros integrantes activos (RN-06); un grupo que queda sin integrantes deja de estar vigente y se conserva
 * como historia (RN-07); el importe de la cuota es la suma de las categorías de los integrantes activos.
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
     * Crea un grupo nuevo vigente, sin integrantes, cuotas ni movimientos (listas en memoria, no proxies). Lo usa
     * {@code ServicioGruposFamiliares.crearGrupoUnipersonal}, que enseguida agrega al socio y lo designa titular.
     *
     * <p>Casos de uso: CU-01, CU-09
     * @param fechaCreacion fecha del alta (hoy en la zona del club)
     */
    public GrupoFamiliar(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
        this.vigente = Boolean.TRUE;
        this.integrantes = new ArrayList<>();
        this.cuotas = new ArrayList<>();
        this.movimientos = new ArrayList<>();
    }

    /**
     * Constructor vacío que usa {@code reconstituir}.
     *
     * <p>Casos de uso: todos
     */
    private GrupoFamiliar() {
    }

    /**
     * Incorpora un socio activo como integrante no titular (RN-05) y le asigna este grupo como su grupo vigente
     * ({@code socio.asignarGrupoFamiliar(this)}), de modo que {@code BrokerGrupoFamiliar.guardarPartes} actualice
     * {@code socio.grupo_familiar_id}.
     *
     * <p>Casos de uso: CU-01, CU-07, CU-09
     * @param socio Socio.
     * @throws ErrorNegocio si el socio no está activo o ya integra el grupo (CU-07 FA2)
     */
    public void agregarIntegrante(Socio socio) {
        if (!socio.estaActiva()) {
            throw new ErrorNegocio("El socio no está activo y no puede integrar un grupo familiar.");
        }
        if (integrantes.contains(socio)) {
            throw new ErrorNegocio("El socio ya integra este grupo familiar.");
        }
        integrantes.add(socio);
        socio.asignarGrupoFamiliar(this);
    }

    /**
     * Quita un integrante; rechaza al titular si quedan otros integrantes activos (RN-06). Si el grupo queda sin
     * integrantes deja de estar vigente y se conserva como registro histórico con su último titular (RN-07).
     *
     * <p>Casos de uso: CU-07, CU-09
     * @param socio Socio.
     * @throws ErrorNegocio si el socio no pertenece al grupo o es titular con otros integrantes activos
     */
    public void quitarIntegrante(Socio socio) {
        if (!integrantes.contains(socio)) {
            throw new ErrorNegocio("El socio no pertenece a este grupo familiar.");
        }
        if (socio.equals(titular) && tieneOtrosIntegrantesActivos(socio)) {
            throw new ErrorNegocio("El socio es titular de un grupo con otros integrantes activos: "
                    + "primero debe designarse otro titular (CU-08).");
        }
        integrantes.remove(socio);
        if (integrantes.isEmpty()) {
            vigente = Boolean.FALSE;
        }
    }

    /**
     * Designa titular a un integrante activo (RN-05). Information Expert: el grupo conoce a sus integrantes y
     * decide si el cambio es válido ({@code sec_cu08_p2}).
     *
     * <p>Casos de uso: CU-01, CU-03, CU-08, CU-09
     * @param socio Nuevo titular.
     * @throws ErrorNegocio si el socio no es integrante activo del grupo (CU-08 FA2, CU-03 FA6)
     */
    public void cambiarTitular(Socio socio) {
        if (socio == null || !integrantesActivos().contains(socio)) {
            throw new ErrorNegocio("El integrante elegido no puede ser titular: debe ser un integrante activo del grupo.");
        }
        this.titular = socio;
    }

    /**
     * Integrantes con cuenta activa ({@code Usuario.estaActiva()}); el primer acceso carga el proxy de
     * integrantes con {@code SociosDeGrupo}.
     *
     * <p>Casos de uso: todos
     * @return Lista de {@code Socio}.
     */
    public List<Socio> integrantesActivos() {
        List<Socio> activos = new ArrayList<>();
        for (Socio socio : integrantes) {
            if (socio.estaActiva()) {
                activos.add(socio);
            }
        }
        return activos;
    }

    /**
     * Suma de los importes de categoría de los integrantes activos (importe de la próxima cuota).
     *
     * <p>Casos de uso: CU-10; tareas programadas: emisión mensual de cuotas
     * @return Importe.
     */
    public Dinero calcularImporteCuota() {
        Dinero total = Dinero.cero();
        for (Socio socio : integrantesActivos()) {
            total = total.sumar(socio.getCategoria().getImporte());
        }
        return total;
    }

    /**
     * Suma de créditos menos suma de débitos (RN-21). El primer acceso carga el proxy de movimientos con
     * {@code MovimientosDeGrupo}.
     *
     * <p>Casos de uso: CU-05, CU-37
     * @return Saldo mayor o igual a 0.
     */
    public Dinero saldoAFavor() {
        Dinero saldo = Dinero.cero();
        for (MovimientoSaldo movimiento : movimientos) {
            if (movimiento.getTipo() == TipoMovimientoSaldo.CREDITO) {
                saldo = saldo.sumar(movimiento.getImporte());
            } else {
                saldo = saldo.restar(movimiento.getImporte());
            }
        }
        return saldo.esNegativo() ? Dinero.cero() : saldo;
    }

    /**
     * Crea la {@code Cuota} con un {@code ItemCuota} por integrante activo, {@code creditoAplicado = min(saldo,
     * importe)}, y la agrega a {@code cuotas} (Creator: el grupo tiene los datos para crearla).
     *
     * <p>Casos de uso: tareas programadas: emisión mensual de cuotas
     * @param periodo AAAA-MM.
     * @param fechas Emisión y vencimiento.
     * @param saldo Saldo a favor disponible.
     * @return {@code Cuota} emitida.
     */
    public Cuota emitirCuota(String periodo, FechasCuota fechas, Dinero saldo) {
        Cuota cuota = new Cuota(periodo, calcularImporteCuota(), fechas);
        cuota.asignarGrupo(getId());
        for (Socio socio : integrantesActivos()) {
            cuota.agregarItem(new ItemCuota(socio, socio.getCategoria(), socio.getCategoria().getImporte()));
        }
        cuota.aplicarCredito(saldo == null ? Dinero.cero() : saldo);
        cuotas.add(cuota);
        return cuota;
    }

    /**
     * Recorre {@code cuotas} y pregunta {@code estaVencida()} a cada una; lo usa {@code ValidadorSocioHabilitado}
     * (RN-11).
     *
     * <p>Casos de uso: CU-04, CU-27, CU-28
     * @return true si alguna cuota está vencida.
     */
    public boolean tieneCuotaVencida() {
        for (Cuota cuota : cuotas) {
            if (cuota.estaVencida()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si el grupo tiene integrantes activos distintos del socio dado (titular con otros, RN-06). Lo usan
     * {@code quitarIntegrante} y las fachadas de socios y grupos (CU-03 paso 3, CU-07 paso 7).
     *
     * <p>Agregado en la implementación: condición «titularConOtros» de {@code sec_cu03_p3} y {@code sec_cu07_p3}
     * escrita una sola vez (Information Expert).
     *
     * <p>Casos de uso: CU-03, CU-07, CU-09
     * @param socio socio que se excluye del conteo
     * @return true si hay al menos otro integrante activo
     */
    public boolean tieneOtrosIntegrantesActivos(Socio socio) {
        for (Socio integrante : integrantesActivos()) {
            if (!integrante.equals(socio)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Indica si el socio es el titular del grupo.
     *
     * <p>Agregado en la implementación: consulta usada por los modelos de vista ({@code esTitular}) y por la baja.
     *
     * <p>Casos de uso: CU-03, CU-04, CU-05, CU-10
     * @param socio socio consultado
     * @return true si es el titular
     */
    public boolean esTitular(Socio socio) {
        return titular != null && titular.equals(socio);
    }

    /**
     * Reconstituye el grupo desde una fila de {@code grupo_familiar} (lo usa solo {@code BrokerGrupoFamiliar},
     * estereotipo «persistencia»); las tres listas son {@code ProxyListaVirtual} sin cargar.
     *
     * <p>Casos de uso: todos
     * @param id identificador
     * @param fechaCreacion creación del grupo
     * @param vigente vigencia
     * @param integrantes proxy de {@code SociosDeGrupo(id)}
     * @param cuotas proxy de {@code CuotasDeGrupo(id)}
     * @param movimientos proxy de {@code MovimientosDeGrupo(id)}
     * @return el grupo rehidratado sin titular
     */
    public static GrupoFamiliar reconstituir(Long id, LocalDate fechaCreacion, Boolean vigente, List<Socio> integrantes, List<Cuota> cuotas, List<MovimientoSaldo> movimientos) {
        GrupoFamiliar grupo = new GrupoFamiliar();
        grupo.asignarId(id);
        grupo.fechaCreacion = fechaCreacion;
        grupo.vigente = vigente;
        grupo.integrantes = integrantes;
        grupo.cuotas = cuotas;
        grupo.movimientos = movimientos;
        return grupo;
    }

    /**
     * Entrega el titular resuelto por {@code BrokerGrupoFamiliar.cargarAsociaciones} (estereotipo «persistencia»).
     *
     * <p>Casos de uso: todos
     * @param titular titular o {@code null}
     */
    public void restaurarAsociaciones(Socio titular) {
        this.titular = titular;
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
