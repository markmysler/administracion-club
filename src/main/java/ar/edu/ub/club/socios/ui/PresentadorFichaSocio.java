package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.grupos.ui.FilaIntegrante;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

/**
 * Presentador de P-08 (ficha del socio con su grupo, cuotas y reservas futuras; acciones modificar, modificar
 * acceso, ver grupo, asociar a otro grupo y desactivar).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)} y convierte con {@code DetalleSocio.desde(ficha)}. Calcula los candidatos a titular que
 * recibe el diálogo de baja (P-09): los integrantes activos distintos del socio cuando el socio es el titular.
 *
 * <p>Casos de uso: CU-02, CU-03, CU-04, CU-07, CU-15
 */
@SpringComponent
@UIScope
public class PresentadorFichaSocio extends PresentadorBase<PantallaFichaSocio> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /** Socio de la ficha. */
    private Long idSocio;

    /** Grupo vigente del socio. */
    private Long idGrupo;

    /** Integrantes activos que pueden reemplazar al socio como titular (vacía si no es titular). */
    private List<OpcionSeleccion> candidatosTitular = List.of();

    /** Último detalle mostrado (nombre, DNI y grupo para abrir los diálogos P-09 y P-12). */
    private DetalleSocio detalle;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     */
    public PresentadorFichaSocio(ServicioSocios servicioSocios) {
        this.servicioSocios = servicioSocios;
    }

    /**
     * {@code ServicioSocios.consultarSocio(idSocio)} → {@code DetalleSocio.desde(ficha)} → {@code mostrarFicha}.
     * Si el socio no existe {@code PresentadorBase} muestra «El elemento ya no existe.» (CU-04 FA2).
     *
     * <p>Casos de uso: CU-03, CU-04
     * @param idSocio socio
     */
    public void cargar(Long idSocio) {
        this.idSocio = idSocio;
        consultar(() -> DetalleSocio.desde(servicioSocios.consultarSocio(idSocio))).ifPresent(d -> {
            this.detalle = d;
            this.idGrupo = d.idGrupo();
            this.candidatosTitular = candidatos(d);
            pantalla.mostrarFicha(d);
        });
    }

    /**
     * Abre el formulario de modificación (P-07).
     *
     * <p>Casos de uso: CU-02
     */
    public void alModificar() {
        pantalla.navegarA("socios/formulario/" + idSocio);
    }

    /**
     * Abre el formulario de datos de acceso de la cuenta (P-14, CU-15).
     *
     * <p>Casos de uso: CU-15
     */
    public void alModificarAcceso() {
        pantalla.navegarA("cuentas/formulario/" + idSocio);
    }

    /**
     * Abre el detalle del grupo familiar vigente (P-11).
     *
     * <p>Casos de uso: CU-10
     */
    public void alVerGrupo() {
        pantalla.navegarA("grupos/" + idGrupo);
    }

    /**
     * Abre el diálogo de baja (P-09) con el nombre, el DNI y los candidatos a titular.
     *
     * <p>Casos de uso: CU-03
     */
    public void alDesactivar() {
        if (detalle == null) {
            return;
        }
        pantalla.abrirDesactivacion(idSocio, detalle.apellido() + ", " + detalle.nombre(), detalle.dni(),
                candidatosTitular);
    }

    /**
     * Abre el diálogo de asociación (P-12) sin grupo destino preseleccionado; {@code grupoActual} es el texto con
     * el titular y el DNI del grupo vigente.
     *
     * <p>Casos de uso: CU-07
     */
    public void alAsociarAGrupo() {
        if (detalle == null) {
            return;
        }
        pantalla.abrirAsociacion(idSocio, detalle.apellido() + ", " + detalle.nombre(), detalle.dni(), idGrupo,
                grupoActual(detalle), null);
    }

    /**
     * Candidatos a titular: si el socio es el titular de su grupo, los demás integrantes activos.
     *
     * <p>Casos de uso: CU-03
     * @param d detalle del socio
     * @return opciones (id, «Apellido, Nombre (DNI)»)
     */
    private static List<OpcionSeleccion> candidatos(DetalleSocio d) {
        boolean esTitular = d.integrantes().stream().anyMatch(i -> i.esTitular() && i.idSocio().equals(d.id()));
        if (!esTitular) {
            return List.of();
        }
        return d.integrantes().stream()
                .filter(i -> !i.idSocio().equals(d.id()) && "ACTIVA".equals(i.estado()))
                .map(i -> new OpcionSeleccion(i.idSocio(), i.apellido() + ", " + i.nombre() + " (DNI " + i.dni() + ")"))
                .toList();
    }

    /**
     * Texto del grupo vigente: titular y su DNI.
     *
     * <p>Casos de uso: CU-07
     * @param d detalle del socio
     * @return «Grupo N: Apellido, Nombre (DNI ...)»
     */
    private static String grupoActual(DetalleSocio d) {
        return d.integrantes().stream().filter(FilaIntegrante::esTitular).findFirst()
                .map(t -> "Grupo " + d.idGrupo() + ": " + t.apellido() + ", " + t.nombre() + " (DNI " + t.dni() + ")")
                .orElse("Grupo " + d.idGrupo());
    }
}
