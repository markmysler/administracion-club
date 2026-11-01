package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.DatosSocio;
import ar.edu.ub.club.socios.aplicacion.FichaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Presentador de P-07 (formulario de alta y modificación de socio). En el alta incluye usuario, correo y
 * contraseña inicial; el tipo de cuenta no es un campo porque queda fijado al crear un {@code Socio} (CU-16).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)}; los {@code ErrorNegocio} con campo se marcan en el formulario
 * ({@code PresentadorBase} → {@code pantalla.marcarCampo}).
 *
 * <p>Casos de uso: CU-01, CU-02, CU-16
 */
@SpringComponent
@UIScope
public class PresentadorFormularioSocio extends PresentadorBase<PantallaFormularioSocio> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /** Socio que se modifica ({@code null} en el alta). */
    private Long idSocio;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     */
    public PresentadorFormularioSocio(ServicioSocios servicioSocios) {
        this.servicioSocios = servicioSocios;
    }

    /**
     * {@code ServicioSocios.listarCategoriasActivas()} (objeto consulta {@code CategoriasActivas}) → opciones del
     * selector (más la categoría actual del socio en la modificación, aunque esté inactiva); con id, {@code ServicioSocios.consultarSocio(idSocio)} → {@code DatosSocio.desde(ficha.socio)};
     * sin id, un {@code DatosSocio} vacío. Sin categorías activas no habilita el alta (CU-01 FA1).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param idSocio socio a modificar o {@code null} para el alta
     */
    public void cargar(Long idSocio) {
        this.idSocio = idSocio;
        Optional<List<CategoriaSocio>> categorias = consultar(servicioSocios::listarCategoriasActivas);
        if (categorias.isEmpty()) {
            return;
        }
        if (idSocio == null) {
            List<OpcionSeleccion> opciones = opcionesDe(categorias.get(), null);
            if (opciones.isEmpty()) {
                pantalla.mostrarError("Primero debe configurarse una categoría de socio activa (CU-06).");
                return;
            }
            pantalla.mostrarCategorias(opciones);
            pantalla.mostrarFormulario(new DatosSocio(), true);
            return;
        }
        Optional<FichaSocio> ficha = consultar(() -> servicioSocios.consultarSocio(idSocio));
        if (ficha.isPresent()) {
            DatosSocio datos = DatosSocio.desde(ficha.get().getSocio());
            pantalla.mostrarCategorias(opcionesDe(categorias.get(), ficha.get().getSocio().getCategoria()));
            pantalla.mostrarFormulario(datos, false);
        }
    }

    /**
     * Alta: {@code ServicioSocios.registrarSocio(datos)}; modificación: {@code ServicioSocios.modificarSocio(idSocio,
     * datos)}; luego informa y navega a la ficha ({@code socios/ficha/id}).
     *
     * <p>Casos de uso: CU-01, CU-02, CU-16
     * @param datos datos leídos del formulario (ya validados en su formato por {@code FormularioEntidad.leer()})
     */
    public void alGuardar(DatosSocio datos) {
        if (idSocio == null) {
            consultar(() -> servicioSocios.registrarSocio(datos)).ifPresent(socio -> {
                pantalla.mostrarMensaje("Socio registrado.");
                pantalla.navegarA("socios/ficha/" + socio.getId());
            });
        } else {
            consultar(() -> servicioSocios.modificarSocio(idSocio, datos)).ifPresent(socio -> {
                pantalla.mostrarMensaje("Datos del socio guardados. Si cambió la categoría, el nuevo importe se "
                        + "aplica desde la próxima cuota emitida.");
                pantalla.navegarA("socios/ficha/" + socio.getId());
            });
        }
    }

    /**
     * Descarta los datos: vuelve a la ficha (modificación) o al listado (alta).
     *
     * <p>Casos de uso: CU-01, CU-02
     */
    public void alCancelar() {
        pantalla.navegarA(idSocio == null ? "socios" : "socios/ficha/" + idSocio);
    }

    /**
     * Opciones del selector: las categorías activas más la categoría actual del socio si está inactiva (para no
     * perderla al modificar otros datos).
     *
     * <p>Casos de uso: CU-01, CU-02
     * @param activas categorías activas
     * @param actual categoría actual del socio o {@code null} en el alta
     * @return opciones (id, «nombre»)
     */
    private static List<OpcionSeleccion> opcionesDe(List<CategoriaSocio> activas, CategoriaSocio actual) {
        List<OpcionSeleccion> opciones = new java.util.ArrayList<>(activas.stream()
                .map(c -> new OpcionSeleccion(c.getId(), c.getNombre()))
                .toList());
        if (actual != null && activas.stream().noneMatch(c -> Objects.equals(c.getId(), actual.getId()))) {
            opciones.add(new OpcionSeleccion(actual.getId(), actual.getNombre() + " (inactiva)"));
        }
        return opciones;
    }
}
