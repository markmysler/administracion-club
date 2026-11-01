package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosDisponibilidad;
import ar.edu.ub.club.agenda.aplicacion.DatosFeriado;
import ar.edu.ub.club.agenda.dominio.DiaSemana;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.timepicker.TimePicker;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.data.validator.StringLengthValidator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

/**
 * Vista de la pantalla P-17: Configuración de días y horario de atención y lista de feriados. Patrón MVP (Vista
 * Pasiva): tiene dos formularios (horario y alta de feriado) y la grilla de feriados futuros; reenvía cada acción
 * a {@code PresentadorDisponibilidadClub}. Para el protocolo de confirmación recuerda la última operación enviada
 * y, si el administrador acepta las reservas afectadas, la reenvía con {@code confirmar = true}.
 *
 * <p>Ruta: {@code agenda/disponibilidad}. Permiso: {@code CONFIGURAR_AGENDA}.
 *
 * <p>Casos de uso: CU-24
 */
@Route(value = "agenda/disponibilidad", layout = DisposicionPrincipal.class)
@PageTitle("Disponibilidad del club")
public class VistaDisponibilidadClub extends VistaProtegida implements PantallaDisponibilidadClub {

    /** Presentador de la pantalla. */
    private final PresentadorDisponibilidadClub presentador;

    /** Formulario de días y horario. */
    private final FormularioEntidad<DatosDisponibilidad> formularioHorario = new FormularioEntidad<>(DatosDisponibilidad.class);

    /** Formulario de alta de feriado. */
    private final FormularioEntidad<DatosFeriado> formularioFeriado = new FormularioEntidad<>(DatosFeriado.class);

    /** Selector de la fecha del feriado (acotado por {@code mostrarRangoCalendario}). */
    private final DatePicker fechaFeriado = new DatePicker("Fecha del feriado");

    /** Grilla de feriados futuros. */
    private final TablaDatos<FilaFeriado> tablaFeriados = new TablaDatos<>(FilaFeriado.class);

    /** Operación a reenviar con {@code confirmar = true} si el administrador confirma (null si no hay). */
    private Runnable reenvioConfirmado;

    /**
     * Crea la vista, se vincula con el presentador y arma los componentes.
     *
     * <p>Casos de uso: CU-24
     * @param presentador presentador de P-17 (inyectado por Spring)
     */
    public VistaDisponibilidadClub(PresentadorDisponibilidadClub presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        construir();
    }

    /**
     * Arma el formulario de horario, el de feriado y la grilla de feriados con la acción Eliminar.
     *
     * <p>Casos de uso: CU-24
     */
    private void construir() {
        CheckboxGroup<DiaSemana> dias = new CheckboxGroup<>("Días de atención");
        dias.setItems(DiaSemana.values());
        dias.setItemLabelGenerator(VistaDisponibilidadClub::etiqueta);
        TimePicker apertura = new TimePicker("Apertura");
        TimePicker cierre = new TimePicker("Cierre");
        formularioHorario.vincular(dias, "diasAtencion",
                List.of(Validator.<Set<DiaSemana>>from(v -> v != null && !v.isEmpty(), "Elija al menos un día de atención.")));
        formularioHorario.vincular(apertura, "horaApertura",
                List.of(Validator.<LocalTime>from(v -> v != null, "Ingrese la hora de apertura.")));
        formularioHorario.vincular(cierre, "horaCierre",
                List.of(Validator.<LocalTime>from(v -> v != null, "Ingrese la hora de cierre.")));
        formularioHorario.alEnviar(this::guardarHorario);

        fechaFeriado.setLocale(FormatoUI.ES_AR);
        TextField descripcion = new TextField("Descripción del feriado");
        descripcion.setMaxLength(100);
        formularioFeriado.vincular(fechaFeriado, "fecha",
                List.of(Validator.<LocalDate>from(v -> v != null, "Ingrese la fecha del feriado.")));
        formularioFeriado.vincular(descripcion, "descripcion",
                List.of(new StringLengthValidator("Ingrese una descripción de hasta 100 caracteres.", 1, 100)));
        formularioFeriado.alEnviar(this::agregarFeriado);
        formularioFeriado.cargar(new DatosFeriado());

        tablaFeriados.agregarColumna("Fecha", f -> FormatoUI.fecha(f.fecha()));
        tablaFeriados.agregarColumna("Descripción", FilaFeriado::descripcion);
        tablaFeriados.agregarColumnaAccion("Eliminar", this::confirmarEliminacion);

        getContent().add(new H2("Disponibilidad del club"), new H3("Días y horario de atención"), formularioHorario,
                new H3("Feriados"), tablaFeriados, new H3("Agregar feriado"), formularioFeriado);
    }

    /**
     * Envía el horario con {@code confirmar = false} y recuerda el reenvío confirmado (CU-24 paso 3).
     *
     * <p>Casos de uso: CU-24
     * @param datos días y horario
     */
    private void guardarHorario(DatosDisponibilidad datos) {
        datos.setConfirmar(false);
        reenvioConfirmado = () -> {
            datos.setConfirmar(true);
            presentador.alGuardarHorario(datos);
        };
        presentador.alGuardarHorario(datos);
    }

    /**
     * Envía el feriado con {@code confirmar = false} y recuerda el reenvío confirmado (CU-24 FA1, 3.1).
     *
     * <p>Casos de uso: CU-24
     * @param datos fecha y descripción
     */
    private void agregarFeriado(DatosFeriado datos) {
        datos.setConfirmar(false);
        reenvioConfirmado = () -> {
            datos.setConfirmar(true);
            presentador.alAgregarFeriado(datos);
        };
        presentador.alAgregarFeriado(datos);
    }

    /**
     * Pide confirmación para eliminar un feriado y, al confirmar, llama a {@code alEliminarFeriado} (FA2, 3.2–3.3).
     *
     * <p>Casos de uso: CU-24
     * @param fila feriado elegido
     */
    private void confirmarEliminacion(FilaFeriado fila) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Eliminar feriado",
                "¿Eliminar el feriado del " + FormatoUI.fecha(fila.fecha()) + "?");
        dialogo.alConfirmar(motivo -> presentador.alEliminarFeriado(fila.id()));
        dialogo.abrir();
    }

    /**
     * Etiqueta en español de un día de la semana.
     *
     * <p>Casos de uso: CU-24
     * @param dia día
     * @return nombre del día
     */
    private static String etiqueta(DiaSemana dia) {
        return switch (dia) {
            case LUNES -> "Lunes";
            case MARTES -> "Martes";
            case MIERCOLES -> "Miércoles";
            case JUEVES -> "Jueves";
            case VIERNES -> "Viernes";
            case SABADO -> "Sábado";
            case DOMINGO -> "Domingo";
        };
    }

    /**
     * Permiso que exige la ruta.
     *
     * <p>Casos de uso: CU-24
     * @return {@code CONFIGURAR_AGENDA}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONFIGURAR_AGENDA;
    }

    /**
     * Al entrar a la ruta pide al presentador cargar la disponibilidad (pasos 1 y 2).
     *
     * <p>Casos de uso: CU-24
     * @param parametros parámetros de ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Marca el campo del {@code ErrorNegocio} en el formulario que lo tenga (horario o feriado); si ninguno lo
     * tiene, muestra el mensaje.
     *
     * <p>Casos de uso: CU-24
     * @param campo propiedad del objeto de comando
     * @param mensaje mensaje para el usuario
     */
    @Override
    public void marcarCampo(String campo, String mensaje) {
        if (formularioHorario.tieneCampo(campo)) {
            formularioHorario.marcarError(campo, mensaje);
        } else if (formularioFeriado.tieneCampo(campo)) {
            formularioFeriado.marcarError(campo, mensaje);
        } else {
            mostrarError(mensaje);
        }
    }

    /**
     * Carga el horario en su formulario, la grilla de feriados y vacía el formulario de feriado.
     *
     * <p>Casos de uso: CU-24
     * @param datos días y horario
     * @param feriados feriados futuros
     */
    @Override
    public void mostrarDisponibilidad(DatosDisponibilidad datos, List<FilaFeriado> feriados) {
        formularioHorario.cargar(datos);
        tablaFeriados.mostrar(feriados);
        formularioFeriado.cargar(new DatosFeriado());
        reenvioConfirmado = null;
    }

    /**
     * Acota el selector de fecha del feriado (RN-13).
     *
     * <p>Casos de uso: CU-24
     * @param desde primera fecha elegible
     * @param hasta última fecha elegible
     */
    @Override
    public void mostrarRangoCalendario(LocalDate desde, LocalDate hasta) {
        fechaFeriado.setMin(desde);
        fechaFeriado.setMax(hasta);
    }

    /**
     * Muestra las reservas afectadas, que se conservan y pueden cancelarse con CU-33; al confirmar reenvía la última
     * operación con {@code confirmar = true} (6.2 o 3.4).
     *
     * <p>Casos de uso: CU-24
     * @param afectadas reservas afectadas
     */
    @Override
    public void pedirConfirmacion(List<FilaResumenReserva> afectadas) {
        DialogoConfirmacion dialogo = new DialogoConfirmacion("Reservas afectadas",
                "Hay " + afectadas.size() + " reservas confirmadas afectadas. Se conservan y pueden cancelarse desde"
                        + " la gestión de reservas. ¿Confirma el cambio?");
        dialogo.mostrarAdvertencias(afectadas.stream().map(FilaResumenReserva::comoTexto).toList());
        Runnable reenvio = reenvioConfirmado;
        dialogo.alConfirmar(motivo -> {
            if (reenvio != null) {
                reenvio.run();
            }
        });
        dialogo.abrir();
    }
}
