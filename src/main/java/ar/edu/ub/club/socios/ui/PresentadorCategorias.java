package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.DatosCategoria;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de P-10 (listado de categorías con alta y edición).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)} y convierte con {@code FilaCategoria.desde(...)}.
 *
 * <p>Casos de uso: CU-06
 */
@SpringComponent
@UIScope
public class PresentadorCategorias extends PresentadorBase<PantallaCategorias> {

    /** Fachada de socios (gestiona las categorías). */
    private final ServicioSocios servicioSocios;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     */
    public PresentadorCategorias(ServicioSocios servicioSocios) {
        this.servicioSocios = servicioSocios;
    }

    /**
     * {@code ServicioSocios.listarCategorias()} → {@code FilaCategoria.desde(c)} → {@code mostrarCategorias}.
     *
     * <p>Casos de uso: CU-06
     */
    public void cargar() {
        consultar(() -> servicioSocios.listarCategorias().stream().map(FilaCategoria::desde).toList())
                .ifPresent(pantalla::mostrarCategorias);
    }

    /**
     * Precarga el formulario: {@code new DatosCategoria(fila.nombre(), fila.importe(), fila.activa())} o un
     * {@code DatosCategoria} vacío para una categoría nueva.
     *
     * <p>Casos de uso: CU-06
     * @param fila categoría elegida o {@code null} (Nueva categoría)
     */
    public void alEditar(FilaCategoria fila) {
        if (fila == null) {
            pantalla.editar(new DatosCategoria(), null);
        } else {
            pantalla.editar(new DatosCategoria(fila.nombre(), fila.importe(), fila.activa()), fila.id());
        }
    }

    /**
     * {@code ServicioSocios.registrarCategoria(datos)} o {@code modificarCategoria(idCategoria, datos)}; luego
     * {@code cargar()} e informa que los importes rigen desde la próxima cuota (RN-08).
     *
     * <p>Casos de uso: CU-06
     * @param idCategoria categoría modificada o {@code null} para el alta
     * @param datos datos del formulario
     */
    public void alGuardar(Long idCategoria, DatosCategoria datos) {
        boolean guardada = ejecutar(() -> {
            if (idCategoria == null) {
                servicioSocios.registrarCategoria(datos);
            } else {
                servicioSocios.modificarCategoria(idCategoria, datos);
            }
        });
        if (guardada) {
            cargar();
            pantalla.mostrarMensaje("Categoría guardada. Los importes se aplican desde la próxima cuota emitida.");
        }
    }
}
