package ar.edu.ub.club.reservas.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.cuotas.ui.FilaCuota;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.util.List;

/**
 * Vista de la pantalla P-19: Grilla de turnos por cancha y horario para una fecha, con filtros por deporte y
 * cancha.
 *
 * <p>Ruta: {@code turnos}. Permiso: {@code CONSULTAR_DISPONIBILIDAD}.
 *
 * <p>Casos de uso: CU-26, CU-27, CU-28
 */
public class VistaDisponibilidadCanchas extends VistaProtegida implements PantallaDisponibilidadCanchas {

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @return el resultado
     */
    protected Permiso permisoRequerido() {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param parametros valor de {@code parametros}
     * @param consulta valor de {@code consulta}
     */
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param deportes valor de {@code deportes}
     * @param canchas valor de {@code canchas}
     */
    @Override
    public void mostrarFiltros(List<OpcionSeleccion> deportes, List<OpcionSeleccion> canchas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param opciones valor de {@code opciones}
     */
    @Override
    public void mostrarCanchas(List<OpcionSeleccion> opciones) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param desde valor de {@code desde}
     * @param hasta valor de {@code hasta}
     */
    @Override
    public void mostrarRangoCalendario(LocalDate desde, LocalDate hasta) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param visible valor de {@code visible}
     */
    @Override
    public void mostrarEleccionSocio(boolean visible) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param fila valor de {@code fila}
     * @param cuotaGrupo valor de {@code cuotaGrupo}
     */
    @Override
    public void mostrarSocioElegido(FilaSocio fila, FilaCuota cuotaGrupo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param filas valor de {@code filas}
     */
    @Override
    public void mostrarTurnos(List<FilaTurno> filas) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: CU-26, CU-27, CU-28
     * @param turno valor de {@code turno}
     * @param idSocio valor de {@code idSocio}
     */
    @Override
    public void abrirConfirmacion(FilaTurno turno, Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
