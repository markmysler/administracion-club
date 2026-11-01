package ar.edu.ub.club.agenda.persistencia;

import ar.edu.ub.club.agenda.dominio.ReglaReserva;
import ar.edu.ub.club.compartido.persistencia.BrokerBaseDatos;
import ar.edu.ub.club.compartido.persistencia.FilaResultado;
import ar.edu.ub.club.compartido.persistencia.ProveedorConexiones;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Intermediario de base de datos de {@code ReglaReserva} (tabla {@code regla_reserva}, fila única con
 * {@code id = 1}): la materializa con {@code fachadaPersistencia.get(ReglaReserva.ID_UNICO, ReglaReserva.class)}
 * y la actualiza en CU-25. Los nombres de columna son los de 07C ({@code limite_reservas_vigentes},
 * {@code anticipacion_min_cancelacion_horas}, {@code anticipacion_min_modificacion_horas}).
 *
 * <p>Broker concreto del patrón Database Broker (Larman, cap. 34); completa los ganchos del Template Method de {@code BrokerBaseDatos}.
 *
 * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
 */
@Component
public class BrokerReglaReserva extends BrokerBaseDatos<ReglaReserva> {

    /**
     * Crea el broker con el proveedor de conexiones.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     * @param proveedor proveedor de conexiones
     */
    public BrokerReglaReserva(ProveedorConexiones proveedor) {
        super(proveedor);
    }

    /**
     * Clase que maneja el broker (clave del registro en {@code FachadaPersistencia}).
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     * @return {@code ReglaReserva.class}
     */
    @Override
    public Class<ReglaReserva> claseObjeto() {
        return ReglaReserva.class;
    }

    /**
     * Gancho del Template Method: tabla principal.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     * @return nombre de la tabla {@code regla_reserva}
     */
    @Override
    protected String tabla() {
        return "regla_reserva";
    }

    /**
     * Gancho del Template Method: reconstituye la regla con sus seis parámetros.
     *
     * <p>Casos de uso: CU-24, CU-25, CU-26, CU-27, CU-28, CU-30, CU-32
     * @param fila fila de {@code regla_reserva}
     * @return la regla
     */
    @Override
    protected ReglaReserva crearObjeto(FilaResultado fila) {
        return ReglaReserva.reconstituir(fila.largo("id"), fila.entero("anticipacion_minima_horas"),
                fila.entero("anticipacion_maxima_dias"), fila.entero("duracion_turno_minutos"),
                fila.entero("limite_reservas_vigentes"), fila.entero("anticipacion_min_cancelacion_horas"),
                fila.entero("anticipacion_min_modificacion_horas"));
    }

    /**
     * Gancho del Template Method: columnas del INSERT; incluye {@code id = ID_UNICO} porque la tabla admite una
     * sola fila (en la práctica la inserta la migración V6).
     *
     * <p>Casos de uso: CU-25
     * @param objeto regla
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresInsercion(ReglaReserva objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("id", ReglaReserva.ID_UNICO);
        valores.putAll(valoresActualizacion(objeto));
        return valores;
    }

    /**
     * Gancho del Template Method: columnas del UPDATE (los seis parámetros, CU-25 paso 6).
     *
     * <p>Casos de uso: CU-25
     * @param objeto regla persistida
     * @return columna → valor
     */
    @Override
    protected Map<String, Object> valoresActualizacion(ReglaReserva objeto) {
        Map<String, Object> valores = new LinkedHashMap<>();
        valores.put("anticipacion_minima_horas", objeto.getAnticipacionMinimaHoras());
        valores.put("anticipacion_maxima_dias", objeto.getAnticipacionMaximaDias());
        valores.put("duracion_turno_minutos", objeto.getDuracionTurnoMinutos());
        valores.put("limite_reservas_vigentes", objeto.getLimiteReservasVigentesPorSocio());
        valores.put("anticipacion_min_cancelacion_horas", objeto.getAnticipacionMinimaCancelacionHoras());
        valores.put("anticipacion_min_modificacion_horas", objeto.getAnticipacionMinimaModificacionHoras());
        return valores;
    }
}
