package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.grupos.aplicacion.ServicioGruposFamiliares;
import ar.edu.ub.club.socios.aplicacion.CriterioBusquedaSocio;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;

/**
 * Presentador de la pantalla P-11: Búsqueda de grupos y detalle del grupo familiar: integrantes, titular e
 * importe de la próxima cuota; acciones cambiar titular, quitar integrante y asociar un socio (busca primero el
 * socio y abre P-12 con este grupo preseleccionado como destino).
 *
 * <p>Casos de uso: todos
 */
public class PresentadorGrupoFamiliar extends PresentadorBase<PantallaGrupoFamiliar> {

    /** Atributo servicioGruposFamiliares. */
    private ServicioGruposFamiliares servicioGruposFamiliares;

    /** Atributo servicioSocios. */
    private ServicioSocios servicioSocios;

    /** Atributo idGrupo. */
    private Long idGrupo;

    /**
     * {@code ServicioGruposFamiliares.buscarGrupos(new CriterioBusquedaGrupo(texto, null)):
     * List&lt;ResumenGrupo&gt;} → {@code FilaGrupo.desde(r)}
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param texto valor de {@code texto}
     */
    public void alBuscarGrupo(String texto) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.consultarGrupo(idGrupo): DetalleGrupo} → {@code FichaGrupo.desde(detalle)}
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idGrupo valor de {@code idGrupo}
     */
    public void cargar(Long idGrupo) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.cambiarTitular(idGrupo, idNuevoTitular): DetalleGrupo}
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idNuevoTitular valor de {@code idNuevoTitular}
     */
    public void alCambiarTitular(Long idNuevoTitular) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioGruposFamiliares.quitarSocio(idGrupo, idSocio): DetalleGrupo}
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idSocio valor de {@code idSocio}
     */
    public void alQuitar(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * {@code ServicioSocios.buscarSocios(criterio)} → {@code FilaSocio}
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param criterio valor de {@code criterio}
     */
    public void alBuscarSocio(CriterioBusquedaSocio criterio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * con la {@code FilaSocio} elegida, {@code pantalla.abrirAsociacion(idSocio, fila.nombre, fila.dni,
     * fila.idGrupo, grupoActual, idGrupo)} (P-12)
     *
     * <p>Casos de uso: CU-08, CU-09, CU-10
     * @param idSocio valor de {@code idSocio}
     */
    public void alElegirSocioParaAsociar(Long idSocio) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
