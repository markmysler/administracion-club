package ar.edu.ub.club.recursos.ui;

import ar.edu.ub.club.compartido.dominio.ResultadoBaja;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.recursos.aplicacion.DatosDeporte;
import ar.edu.ub.club.recursos.aplicacion.ServicioRecursos;
import ar.edu.ub.club.recursos.dominio.Cancha;
import ar.edu.ub.club.recursos.dominio.Deporte;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Presentador de la pantalla P-15: Listado de deportes con alta, edición y desactivación con advertencia.
 * Patrón MVP (Vista Pasiva): recibe los eventos de {@code VistaDeportes}, llama a {@code ServicioRecursos}
 * dentro de {@code ejecutar}/{@code consultar} (que traducen las excepciones a mensajes) y convierte las
 * entidades en {@code FilaDeporte}. No usa tipos de Vaadin.
 *
 * <p>Casos de uso: CU-18, CU-19, CU-20
 */
@SpringComponent
@UIScope
public class PresentadorDeportes extends PresentadorBase<PantallaDeportes> {

    /** Fachada del módulo recursos. */
    private final ServicioRecursos servicioRecursos;

    /**
     * Crea el presentador con la fachada (inyección por constructor).
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     * @param servicioRecursos fachada del módulo recursos
     */
    public PresentadorDeportes(ServicioRecursos servicioRecursos) {
        this.servicioRecursos = servicioRecursos;
    }

    /**
     * {@code ServicioRecursos.listarDeportes(): List&lt;Deporte&gt;} y {@code ServicioRecursos.listarCanchas(null):
     * List&lt;Cancha&gt;} (agrupadas por deporte) → {@code FilaDeporte.desde(d, cantidadCanchas)}
     *
     * <p>Casos de uso: CU-18, CU-19, CU-20
     */
    public void cargar() {
        Optional<List<FilaDeporte>> filas = consultar(() -> {
            Map<Long, Integer> cantidades = new HashMap<>();
            for (Cancha cancha : servicioRecursos.listarCanchas(null)) {
                cantidades.merge(cancha.getDeporte().getId(), 1, Integer::sum);
            }
            List<Deporte> deportes = servicioRecursos.listarDeportes();
            return deportes.stream().map(d -> FilaDeporte.desde(d, cantidades.getOrDefault(d.getId(), 0))).toList();
        });
        filas.ifPresent(pantalla::mostrarDeportes);
    }

    /**
     * precarga {@code new DatosDeporte(fila.nombre(), fila.descripcion(), fila.activo())}; con {@code fila = null}
     * (Nuevo deporte, CU-18 paso 1) abre el formulario vacío.
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param fila fila elegida o null para un alta
     */
    public void alEditar(FilaDeporte fila) {
        if (fila == null) {
            pantalla.editar(new DatosDeporte(), null);
        } else {
            pantalla.editar(new DatosDeporte(fila.nombre(), fila.descripcion(), fila.activo()), fila.id());
        }
    }

    /**
     * {@code ServicioRecursos.registrarDeporte(datos)} o {@code modificarDeporte(idDeporte, datos)}; si termina bien
     * recarga la grilla e informa (paso 8); un {@code ErrorNegocio} con campo marca el campo (FA3/FA4).
     *
     * <p>Casos de uso: CU-18, CU-19
     * @param idDeporte deporte que se modifica (null en el alta)
     * @param datos datos del formulario
     */
    public void alGuardar(Long idDeporte, DatosDeporte datos) {
        boolean guardado = ejecutar(() -> {
            if (idDeporte == null) {
                servicioRecursos.registrarDeporte(datos);
            } else {
                servicioRecursos.modificarDeporte(idDeporte, datos);
            }
        });
        if (guardado) {
            cargar();
            pantalla.mostrarMensaje(idDeporte == null ? "Deporte registrado." : "Deporte modificado.");
        }
    }

    /**
     * {@code ServicioRecursos.desactivarDeporte(idDeporte, false): ResultadoBaja}; si no {@code realizada}, {@code
     * pantalla.pedirConfirmacionBaja(idDeporte, afectadas)}
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte a desactivar
     */
    public void alDesactivar(Long idDeporte) {
        consultar(() -> servicioRecursos.desactivarDeporte(idDeporte, false)).ifPresent(resultado -> {
            if (resultado.realizada()) {
                informarBaja(resultado);
            } else {
                pantalla.pedirConfirmacionBaja(idDeporte, filas(resultado));
            }
        });
    }

    /**
     * {@code ServicioRecursos.desactivarDeporte(idDeporte, true)}; recarga la grilla e informa la cantidad de
     * reservas canceladas (paso 10).
     *
     * <p>Casos de uso: CU-20
     * @param idDeporte deporte a desactivar
     */
    public void alConfirmarDesactivacion(Long idDeporte) {
        consultar(() -> servicioRecursos.desactivarDeporte(idDeporte, true)).ifPresent(this::informarBaja);
    }

    /**
     * Recarga la grilla e informa la baja con la cantidad de reservas canceladas (CU-20 paso 10).
     *
     * <p>Casos de uso: CU-20
     * @param resultado resultado de la baja realizada
     */
    private void informarBaja(ResultadoBaja resultado) {
        cargar();
        pantalla.mostrarMensaje("Deporte desactivado. Reservas canceladas: " + resultado.reservasAfectadas().size() + ".");
    }

    /**
     * Convierte las reservas afectadas en modelos de vista ({@code FilaResumenReserva.desde(r)} por cada una).
     *
     * <p>Casos de uso: CU-20
     * @param resultado resultado de la baja
     * @return filas para el diálogo
     */
    private static List<FilaResumenReserva> filas(ResultadoBaja resultado) {
        return resultado.reservasAfectadas().stream().map(FilaResumenReserva::desde).toList();
    }
}
