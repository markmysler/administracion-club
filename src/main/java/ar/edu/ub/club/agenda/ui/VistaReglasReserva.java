package ar.edu.ub.club.agenda.ui;

import ar.edu.ub.club.agenda.aplicacion.DatosReglaReserva;
import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormularioEntidad;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.data.binder.Validator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-18: Formulario de las seis reglas de reserva. Patrón MVP (Vista Pasiva): el
 * {@code Binder} valida el formato (enteros mayores que cero, CU-25 FA2) y la compatibilidad la decide la fachada.
 *
 * <p>Ruta: {@code agenda/reglas}. Permiso: {@code CONFIGURAR_AGENDA}.
 *
 * <p>Casos de uso: CU-25
 */
@Route(value = "agenda/reglas", layout = DisposicionPrincipal.class)
@PageTitle("Reglas de reserva")
public class VistaReglasReserva extends VistaProtegida implements PantallaReglasReserva {

    /** Presentador de la pantalla. */
    private final PresentadorReglasReserva presentador;

    /** Formulario de las seis reglas. */
    private final FormularioEntidad<DatosReglaReserva> formulario = new FormularioEntidad<>(DatosReglaReserva.class);

    /**
     * Crea la vista, se vincula con el presentador y arma el formulario.
     *
     * <p>Casos de uso: CU-25
     * @param presentador presentador de P-18 (inyectado por Spring)
     */
    public VistaReglasReserva(PresentadorReglasReserva presentador) {
        this.presentador = presentador;
        presentador.vincular(this);
        construir();
    }

    /**
     * Arma los seis campos numéricos y los botones Guardar y Cancelar.
     *
     * <p>Casos de uso: CU-25
     */
    private void construir() {
        campo("Anticipación mínima para reservar (horas)", "anticipacionMinimaHoras");
        campo("Anticipación máxima para reservar (días)", "anticipacionMaximaDias");
        campo("Duración del turno (minutos)", "duracionTurnoMinutos");
        campo("Límite de reservas vigentes por socio", "limiteReservasVigentesPorSocio");
        campo("Anticipación mínima para cancelar (horas)", "anticipacionMinimaCancelacionHoras");
        campo("Anticipación mínima para modificar (horas)", "anticipacionMinimaModificacionHoras");
        formulario.alEnviar(presentador::alGuardar);
        Button cancelar = new Button("Cancelar", e -> presentador.cargar());
        getContent().add(new H2("Reglas de reserva"), formulario, cancelar);
    }

    /**
     * Crea un campo entero obligatorio mayor que cero y lo vincula a la propiedad.
     *
     * <p>Casos de uso: CU-25
     * @param etiqueta etiqueta visible
     * @param propiedad propiedad de {@code DatosReglaReserva}
     */
    private void campo(String etiqueta, String propiedad) {
        IntegerField campo = new IntegerField(etiqueta);
        campo.setMin(1);
        campo.setRequiredIndicatorVisible(true);
        formulario.vincular(campo, propiedad,
                List.of(Validator.<Integer>from(v -> v != null && v > 0, "Ingrese un número entero mayor que cero.")));
    }

    /**
     * Permiso que exige la ruta.
     *
     * <p>Casos de uso: CU-25
     * @return {@code CONFIGURAR_AGENDA}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.CONFIGURAR_AGENDA;
    }

    /**
     * Al entrar a la ruta pide al presentador cargar las reglas vigentes (paso 2).
     *
     * <p>Casos de uso: CU-25
     * @param parametros parámetros de ruta (no usa)
     * @param consulta parámetros de consulta (no usa)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        presentador.cargar();
    }

    /**
     * Formulario de la vista, para que {@code marcarCampo} marque el campo del {@code ErrorNegocio}.
     *
     * <p>Casos de uso: CU-25
     * @return formulario de reglas
     */
    @Override
    protected FormularioEntidad<?> formulario() {
        return formulario;
    }

    /**
     * Carga las reglas en el formulario.
     *
     * <p>Casos de uso: CU-25
     * @param datos reglas vigentes
     */
    @Override
    public void mostrarReglas(DatosReglaReserva datos) {
        formulario.cargar(datos);
    }
}
