package ar.edu.ub.club.reservas.persistencia;

import ar.edu.ub.club.compartido.persistencia.ConsultaEntidades;
import ar.edu.ub.club.reservas.aplicacion.FiltroReservas;
import ar.edu.ub.club.reservas.dominio.Reserva;
import java.util.ArrayList;
import java.util.List;

/**
 * Reservas que cumplen un {@code FiltroReservas} (P-21 y P-22): una condición por cada criterio informado
 * (fechas, deporte, cancha, socio, estado, texto del socio) y orden con las CONFIRMADAS primero y luego por fecha
 * descendente. Como la URL de la base usa {@code stringtype=unspecified}, el WHERE se arma solo con los criterios
 * no nulos y {@code parametros()} los devuelve en el mismo orden.
 *
 * <p>Objeto consulta (Query Object, Fowler) que devuelve objetos persistentes; lo resuelve {@code BrokerReserva}:
 * {@code sql()} se agrega a su {@code consultaBase()} (JOIN con usuario, socio y cancha).
 *
 * <p>Casos de uso: CU-29, CU-31
 */
public class ReservasPorFiltro extends ConsultaEntidades<Reserva> {

    /** Criterios de búsqueda ya validados. */
    private final FiltroReservas filtro;

    /**
     * Crea la consulta; el filtro se recibe por constructor (Query Object).
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param filtro criterios de búsqueda
     */
    public ReservasPorFiltro(FiltroReservas filtro) {
        this.filtro = filtro == null ? new FiltroReservas() : filtro;
    }

    /**
     * Clase que resuelve {@code BrokerReserva}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return {@code Reserva}
     */
    @Override
    public Class<Reserva> claseResultado() {
        return Reserva.class;
    }

    /**
     * {@code WHERE} con una condición por criterio informado y {@code ORDER BY} vigentes primero y luego fecha
     * descendente.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return condición y orden
     */
    @Override
    public String sql() {
        List<String> condiciones = new ArrayList<>();
        if (filtro.getDesde() != null) {
            condiciones.add("reserva.fecha >= ?");
        }
        if (filtro.getHasta() != null) {
            condiciones.add("reserva.fecha <= ?");
        }
        if (filtro.getIdDeporte() != null) {
            condiciones.add("cancha.deporte_id = ?");
        }
        if (filtro.getIdCancha() != null) {
            condiciones.add("reserva.cancha_id = ?");
        }
        if (filtro.getIdSocio() != null) {
            condiciones.add("reserva.socio_id = ?");
        }
        if (tieneTexto(filtro.getEstado())) {
            condiciones.add("reserva.estado = ?");
        }
        if (tieneTexto(filtro.getTextoSocio())) {
            condiciones.add("(socio.dni LIKE ? OR socio.apellido ILIKE ? OR socio.nombre ILIKE ?)");
        }
        String where = condiciones.isEmpty() ? "" : "WHERE " + String.join(" AND ", condiciones) + " ";
        return where + "ORDER BY CASE WHEN reserva.estado = 'CONFIRMADA' THEN 0 ELSE 1 END,"
                + " reserva.fecha DESC, reserva.hora_inicio DESC";
    }

    /**
     * Valores de los criterios informados, en el orden de los {@code ?}.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @return lista posiblemente vacía
     */
    @Override
    public List<Object> parametros() {
        List<Object> valores = new ArrayList<>();
        if (filtro.getDesde() != null) {
            valores.add(filtro.getDesde());
        }
        if (filtro.getHasta() != null) {
            valores.add(filtro.getHasta());
        }
        if (filtro.getIdDeporte() != null) {
            valores.add(filtro.getIdDeporte());
        }
        if (filtro.getIdCancha() != null) {
            valores.add(filtro.getIdCancha());
        }
        if (filtro.getIdSocio() != null) {
            valores.add(filtro.getIdSocio());
        }
        if (tieneTexto(filtro.getEstado())) {
            valores.add(filtro.getEstado().trim());
        }
        if (tieneTexto(filtro.getTextoSocio())) {
            String patron = "%" + filtro.getTextoSocio().trim() + "%";
            valores.add(patron);
            valores.add(patron);
            valores.add(patron);
        }
        return valores;
    }

    /**
     * Indica si un criterio de texto está informado.
     *
     * <p>Casos de uso: CU-29, CU-31
     * @param texto criterio
     * @return true si no es nulo ni vacío
     */
    private static boolean tieneTexto(String texto) {
        return texto != null && !texto.isBlank();
    }
}
