package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.grupos.aplicacion.CriterioBusquedaGrupo;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.Objects;

/**
 * Presentador del diálogo P-12 (asociar un socio a otro grupo familiar): busca el grupo destino por DNI o nombre
 * de un integrante o del titular y asocia el socio ({@code sec_cu07_p1}, {@code sec_cu07_p2}).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)} y convierte con {@code FilaGrupo.desde(...)}.
 *
 * <p>Casos de uso: CU-07
 */
@SpringComponent
@UIScope
public class PresentadorAsociarSocio extends PresentadorBase<PantallaAsociarSocio> {

    /** Fachada de grupos. */
    private final ServicioGruposFamiliares servicioGruposFamiliares;

    /** Socio que se asocia. */
    private Long idSocio;

    /** Grupo actual del socio (se excluye de los resultados: el destino debe ser otro, CU-07 FA2). */
    private Long idGrupoActual;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioGruposFamiliares fachada de grupos
     */
    public PresentadorAsociarSocio(ServicioGruposFamiliares servicioGruposFamiliares) {
        this.servicioGruposFamiliares = servicioGruposFamiliares;
    }

    /**
     * Muestra el socio y su grupo actual ({@code mostrarSocio}) y, si hay destino, lo preselecciona.
     *
     * <p>Casos de uso: CU-07
     * @param idSocio socio que se asocia
     * @param nombre apellido y nombre
     * @param dni documento
     * @param idGrupoActual grupo vigente del socio
     * @param grupoActual texto del grupo vigente (titular y DNI)
     * @param idGrupoDestino grupo destino preseleccionado (desde P-11) o {@code null} (desde P-08)
     */
    public void alAbrir(Long idSocio, String nombre, String dni, Long idGrupoActual, String grupoActual, Long idGrupoDestino) {
        this.idSocio = idSocio;
        this.idGrupoActual = idGrupoActual;
        pantalla.mostrarSocio(nombre, dni, grupoActual);
        if (idGrupoDestino != null) {
            pantalla.preseleccionar(idGrupoDestino);
        }
    }

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, true))} → {@code FilaGrupo}
     * (sin el grupo actual del socio) → {@code mostrarGrupos}; sin coincidencias lo informa (FA1).
     *
     * <p>Casos de uso: CU-07
     * @param texto DNI o nombre del titular o de un integrante
     */
    public void alBuscar(String texto) {
        consultar(() -> servicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, true)).stream()
                .map(FilaGrupo::desde).filter(f -> !Objects.equals(f.idGrupo(), idGrupoActual)).toList())
                .ifPresent(filas -> {
                    pantalla.mostrarGrupos(filas);
                    if (filas.isEmpty()) {
                        pantalla.mostrarMensaje("No hay grupos vigentes que coincidan con la búsqueda.");
                    }
                });
    }

    /**
     * {@code ServicioGruposFamiliares.asociarSocio(idSocio, idGrupoDestino)}; si se realizó cierra con éxito y
     * navega a P-11 del grupo destino ({@code grupos/idGrupoDestino}), que indica que las cuotas ya emitidas no
     * cambian. Un {@code ErrorNegocio} (FA2, FA3) se muestra en el diálogo.
     *
     * <p>Casos de uso: CU-07
     * @param idGrupoDestino grupo destino elegido
     */
    public void alConfirmar(Long idGrupoDestino) {
        if (idGrupoDestino == null) {
            pantalla.mostrarError("Elija el grupo destino.");
            return;
        }
        consultar(() -> servicioGruposFamiliares.asociarSocio(idSocio, idGrupoDestino)).ifPresent(detalle -> {
            pantalla.cerrarConExito();
            pantalla.mostrarMensaje("Socio asociado al grupo. Las cuotas ya emitidas no se modifican.");
            pantalla.navegarA("grupos/" + idGrupoDestino);
        });
    }
}
