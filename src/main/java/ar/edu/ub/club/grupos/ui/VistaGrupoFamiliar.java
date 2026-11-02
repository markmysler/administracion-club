package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.seguridad.Permiso;
import ar.edu.ub.club.compartido.ui.DialogoConfirmacion;
import ar.edu.ub.club.compartido.ui.DialogoResueltoEvento;
import ar.edu.ub.club.compartido.ui.DisposicionPrincipal;
import ar.edu.ub.club.compartido.ui.FormatoUI;
import ar.edu.ub.club.compartido.ui.TablaDatos;
import ar.edu.ub.club.compartido.ui.VistaProtegida;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import java.util.List;

/**
 * Vista de la pantalla P-11: búsqueda de grupos y detalle del grupo familiar (integrantes, titular e importe de la
 * próxima cuota) con las acciones cambiar titular y quitar integrante (ambas pasan antes por
 * {@code DialogoConfirmacion}) y asociar un socio a este grupo (busca el socio y abre P-12 con el grupo
 * preseleccionado).
 *
 * <p>Ruta: {@code grupos/:id?}. Permiso: {@code GESTIONAR_GRUPOS}.
 *
 * <p>Vista pasiva del patrón MVP: sus oyentes solo llaman a {@code presentador.al…(...)}.
 *
 * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
 */
@Route(value = "grupos/:id?", layout = DisposicionPrincipal.class)
@PageTitle("Grupos familiares")
public class VistaGrupoFamiliar extends VistaProtegida implements PantallaGrupoFamiliar {

    /** Presentador de la pantalla. */
    private final PresentadorGrupoFamiliar presentador;

    /** Presentador del diálogo de asociación (P-12). */
    private final PresentadorAsociarSocio presentadorAsociar;

    /** Texto de búsqueda de grupos. */
    private final TextField textoGrupo = new TextField("DNI o nombre de un integrante o del titular");

    /** Grupos encontrados. */
    private final TablaDatos<FilaGrupo> grupos = new TablaDatos<>(FilaGrupo.class);

    /** Sección del detalle (oculta hasta elegir un grupo). */
    private final VerticalLayout detalle = new VerticalLayout();

    /** Encabezado del detalle. */
    private final H3 tituloGrupo = new H3();

    /** Datos del grupo. */
    private final Paragraph datosGrupo = new Paragraph();

    /** Integrantes del grupo. */
    private final TablaDatos<FilaIntegrante> integrantes = new TablaDatos<>(FilaIntegrante.class);

    /** Texto de búsqueda del socio a asociar. */
    private final TextField textoSocio = new TextField("Socio a asociar (DNI, nombre o apellido)");

    /** Socios encontrados para asociar. */
    private final TablaDatos<FilaSocio> socios = new TablaDatos<>(FilaSocio.class);

    /** Grupo mostrado (para recargar al resolverse P-12). */
    private Long idGrupo;

    /**
     * Arma la búsqueda, el detalle y las acciones y se vincula con el presentador.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @param presentador presentador de P-11
     * @param presentadorAsociar presentador de P-12
     */
    public VistaGrupoFamiliar(PresentadorGrupoFamiliar presentador, PresentadorAsociarSocio presentadorAsociar) {
        this.presentador = presentador;
        this.presentadorAsociar = presentadorAsociar;
        presentador.vincular(this);
        Button buscarGrupo = new Button("Buscar", e -> presentador.alBuscarGrupo(textoGrupo.getValue()));
        textoGrupo.addKeyPressListener(Key.ENTER, e -> presentador.alBuscarGrupo(textoGrupo.getValue()));
        HorizontalLayout busqueda = new HorizontalLayout(textoGrupo, buscarGrupo);
        busqueda.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        grupos.agregarColumna("Grupo", FilaGrupo::idGrupo);
        grupos.agregarColumna("Titular", FilaGrupo::titular);
        grupos.agregarColumna("DNI titular", FilaGrupo::dniTitular);
        grupos.agregarColumna("Integrantes", FilaGrupo::cantidadIntegrantes);
        grupos.agregarColumna("Vigente", f -> f.vigente() ? "Sí" : "No");
        grupos.agregarColumnaAccion("Ver detalle", f -> presentador.cargar(f.idGrupo()));
        integrantes.agregarColumna("DNI", FilaIntegrante::dni);
        integrantes.agregarColumna("Apellido", FilaIntegrante::apellido);
        integrantes.agregarColumna("Nombre", FilaIntegrante::nombre);
        integrantes.agregarColumna("Categoría", FilaIntegrante::categoria);
        integrantes.agregarColumna("Importe", f -> FormatoUI.dinero(f.importe()));
        integrantes.agregarColumna("Estado", FilaIntegrante::estado);
        integrantes.agregarColumna("Titular", f -> f.esTitular() ? "Sí" : "");
        integrantes.agregarColumnaAccion("Designar titular", this::confirmarCambioTitular);
        integrantes.agregarColumnaAccion("Quitar del grupo", this::confirmarQuitar);
        Button buscarSocio = new Button("Buscar socio", e -> {
            CriterioBusquedaSocio criterio = new CriterioBusquedaSocio();
            criterio.setTexto(textoSocio.getValue());
            presentador.alBuscarSocio(criterio);
        });
        HorizontalLayout asociar = new HorizontalLayout(textoSocio, buscarSocio);
        asociar.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.BASELINE);
        socios.agregarColumna("DNI", FilaSocio::dni);
        socios.agregarColumna("Apellido", FilaSocio::apellido);
        socios.agregarColumna("Nombre", FilaSocio::nombre);
        socios.agregarColumna("Grupo actual", FilaSocio::idGrupo);
        socios.agregarColumnaAccion("Asociar a este grupo", f -> presentador.alElegirSocioParaAsociar(f.id()));
        detalle.setPadding(false);
        detalle.add(tituloGrupo, datosGrupo, integrantes, new H3("Asociar un socio a este grupo"), asociar, socios);
        detalle.setVisible(false);
        getContent().add(new H2("Grupos familiares"), busqueda, grupos, detalle);
    }

    /**
     * Permiso de la pantalla.
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @return {@code GESTIONAR_GRUPOS}
     */
    @Override
    protected Permiso permisoRequerido() {
        return Permiso.GESTIONAR_GRUPOS;
    }

    /**
     * Si la ruta trae el id del grupo pide su detalle.
     *
     * <p>Casos de uso: CU-07, CU-10
     * @param parametros parámetros de la ruta ({@code id} opcional)
     * @param consulta parámetros de consulta (sin uso)
     */
    @Override
    protected void alEntrar(RouteParameters parametros, QueryParameters consulta) {
        parametros.getLong("id").ifPresent(presentador::cargar);
    }

    /**
     * Muestra los grupos encontrados.
     *
     * <p>Casos de uso: CU-10
     * @param filas grupos
     */
    @Override
    public void mostrarGrupos(List<FilaGrupo> filas) {
        grupos.mostrar(filas);
    }

    /**
     * Muestra el detalle del grupo; un grupo no vigente se marca como solo consulta histórica (CU-10 FA2).
     *
     * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
     * @param ficha modelo de vista del grupo
     */
    @Override
    public void mostrarGrupo(FichaGrupo ficha) {
        idGrupo = ficha.id();
        tituloGrupo.setText("Grupo familiar " + ficha.id() + (ficha.vigente() ? "" : " (no vigente: consulta histórica)"));
        datosGrupo.setText("Creado el " + FormatoUI.fecha(ficha.fechaCreacion()) + " · Titular: " + ficha.titular()
                + " · Importe de la próxima cuota: " + FormatoUI.dinero(ficha.importeProximaCuota()));
        integrantes.mostrar(ficha.integrantes());
        detalle.setVisible(true);
    }

    /**
     * Muestra los socios encontrados para asociar.
     *
     * <p>Casos de uso: CU-07
     * @param filas socios
     */
    @Override
    public void mostrarSocios(List<FilaSocio> filas) {
        socios.mostrar(filas);
    }

    /**
     * Crea y abre el diálogo P-12 con este grupo preseleccionado; al resolverse recarga el detalle.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio socio
     * @param nombre apellido y nombre
     * @param dni documento
     * @param idGrupoActual grupo vigente del socio
     * @param grupoActual texto del grupo vigente
     * @param idGrupoDestino este grupo
     */
    @Override
    public void abrirAsociacion(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        DialogoAsociarSocio dialogo = new DialogoAsociarSocio(presentadorAsociar);
        dialogo.addListener(DialogoResueltoEvento.class, e -> presentador.cargar(idGrupo));
        dialogo.abrir(idSocio, nombre, dni, idGrupoActual, grupoActual, idGrupoDestino);
    }

    /**
     * Pide confirmación del cambio de titular (CU-08 paso 5) y, si se confirma, llama al presentador.
     *
     * <p>Casos de uso: CU-08
     * @param fila integrante elegido
     */
    private void confirmarCambioTitular(FilaIntegrante fila) {
        DialogoConfirmacion confirmacion = new DialogoConfirmacion("Cambiar titular",
                fila.apellido() + ", " + fila.nombre() + " pasará a ser el titular y el responsable de la cuota del grupo.");
        confirmacion.alConfirmar(motivo -> presentador.alCambiarTitular(fila.idSocio()));
        confirmacion.abrir();
    }

    /**
     * Pide confirmación para quitar al integrante (CU-09 paso 4) y, si se confirma, llama al presentador.
     *
     * <p>Casos de uso: CU-09
     * @param fila integrante elegido
     */
    private void confirmarQuitar(FilaIntegrante fila) {
        DialogoConfirmacion confirmacion = new DialogoConfirmacion("Quitar del grupo",
                fila.apellido() + ", " + fila.nombre() + " pasará a un grupo familiar unipersonal propio.");
        confirmacion.alConfirmar(motivo -> presentador.alQuitar(fila.idSocio()));
        confirmacion.abrir();
    }
}
