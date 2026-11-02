package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.grupos.aplicacion.CriterioBusquedaGrupo;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import ar.edu.ub.club.socios.ui.FilaSocio;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;
import java.util.Optional;

/**
 * Presentador de P-11 (búsqueda de grupos y detalle del grupo familiar: integrantes, titular e importe de la
 * próxima cuota; acciones cambiar titular, quitar integrante y asociar un socio a este grupo, que abre P-12 con
 * el grupo preseleccionado como destino).
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a las fachadas dentro de
 * {@code consultar(...)} y convierte con {@code FilaGrupo.desde}, {@code FichaGrupo.desde} y {@code FilaSocio.desde}.
 *
 * <p>Casos de uso: CU-07, CU-08, CU-09, CU-10
 */
@SpringComponent
@UIScope
public class PresentadorGrupoFamiliar extends PresentadorBase<PantallaGrupoFamiliar> {

    /** Fachada de grupos. */
    private final ServicioGruposFamiliares servicioGruposFamiliares;

    /** Fachada de socios (búsqueda del socio a asociar). */
    private final ServicioSocios servicioSocios;

    /** Grupo mostrado en el detalle. */
    private Long idGrupo;

    /** Última búsqueda de socios (para abrir P-12 con el nombre, el DNI y el grupo del socio elegido). */
    private List<FilaSocio> sociosEncontrados = List.of();

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioGruposFamiliares fachada de grupos
     * @param servicioSocios fachada de socios
     */
    public PresentadorGrupoFamiliar(ServicioGruposFamiliares servicioGruposFamiliares, ServicioSocios servicioSocios) {
        this.servicioGruposFamiliares = servicioGruposFamiliares;
        this.servicioSocios = servicioSocios;
    }

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, null))} →
     * {@code FilaGrupo.desde(r)} → {@code mostrarGrupos}; sin coincidencias lo informa (CU-10 FA1).
     *
     * <p>Casos de uso: CU-10
     * @param texto DNI o nombre de un integrante o del titular
     */
    public void alBuscarGrupo(String texto) {
        consultar(() -> servicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, null)).stream()
                .map(FilaGrupo::desde).toList()).ifPresent(filas -> {
                    pantalla.mostrarGrupos(filas);
                    if (filas.isEmpty()) {
                        pantalla.mostrarMensaje("No hay grupos que coincidan con la búsqueda.");
                    }
                });
    }

    /**
     * {@code ServicioGruposFamiliares.consultarGrupo(idGrupo)} → {@code FichaGrupo.desde(detalle)} →
     * {@code mostrarGrupo}.
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idGrupo grupo
     */
    public void cargar(Long idGrupo) {
        this.idGrupo = idGrupo;
        consultar(() -> FichaGrupo.desde(servicioGruposFamiliares.consultarGrupo(idGrupo)))
                .ifPresent(pantalla::mostrarGrupo);
    }

    /**
     * {@code ServicioGruposFamiliares.cambiarTitular(idGrupo, idNuevoTitular)} → {@code mostrarGrupo} (la vista ya
     * pidió la confirmación con {@code DialogoConfirmacion}).
     *
     * <p>Casos de uso: CU-08
     * @param idNuevoTitular integrante elegido
     */
    public void alCambiarTitular(Long idNuevoTitular) {
        consultar(() -> FichaGrupo.desde(servicioGruposFamiliares.cambiarTitular(idGrupo, idNuevoTitular)))
                .ifPresent(ficha -> {
                    pantalla.mostrarGrupo(ficha);
                    pantalla.mostrarMensaje("Titular cambiado: " + ficha.titular() + " es el responsable de la cuota.");
                });
    }

    /**
     * {@code ServicioGruposFamiliares.quitarSocio(idGrupo, idSocio)} → {@code mostrarGrupo} (la vista ya pidió la
     * confirmación).
     *
     * <p>Casos de uso: CU-09
     * @param idSocio integrante a quitar
     */
    public void alQuitar(Long idSocio) {
        consultar(() -> FichaGrupo.desde(servicioGruposFamiliares.quitarSocio(idGrupo, idSocio)))
                .ifPresent(ficha -> {
                    pantalla.mostrarGrupo(ficha);
                    pantalla.mostrarMensaje("Integrante quitado: pasó a un grupo unipersonal propio.");
                });
    }

    /**
     * {@code ServicioSocios.buscarSocios(criterio)} → {@code FilaSocio.desde} → {@code mostrarSocios} (búsqueda
     * del socio que se asociará a este grupo).
     *
     * <p>Casos de uso: CU-07
     * @param criterio texto y estado
     */
    public void alBuscarSocio(CriterioBusquedaSocio criterio) {
        consultar(() -> servicioSocios.buscarSocios(criterio).stream().map(FilaSocio::desde).toList())
                .ifPresent(filas -> {
                    this.sociosEncontrados = filas;
                    pantalla.mostrarSocios(filas);
                });
    }

    /**
     * Con la {@code FilaSocio} elegida abre P-12 con este grupo como destino:
     * {@code pantalla.abrirAsociacion(idSocio, nombre, dni, idGrupoActual, grupoActual, idGrupo)}; el texto del
     * grupo actual se arma con su titular ({@code consultarGrupo}).
     *
     * <p>Casos de uso: CU-07
     * @param idSocio socio elegido
     */
    public void alElegirSocioParaAsociar(Long idSocio) {
        Optional<FilaSocio> fila = sociosEncontrados.stream().filter(f -> f.id().equals(idSocio)).findFirst();
        if (fila.isEmpty()) {
            return;
        }
        FilaSocio socio = fila.get();
        String grupoActual = consultar(() -> FichaGrupo.desde(servicioGruposFamiliares.consultarGrupo(socio.idGrupo())))
                .map(f -> "Grupo " + f.id() + ": " + f.titular()).orElse("Grupo " + socio.idGrupo());
        pantalla.abrirAsociacion(idSocio, socio.apellido() + ", " + socio.nombre(), socio.dni(), socio.idGrupo(),
                grupoActual, idGrupo);
    }
}
