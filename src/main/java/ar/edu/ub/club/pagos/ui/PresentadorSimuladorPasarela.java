package ar.edu.ub.club.pagos.ui;

import ar.edu.ub.club.compartido.excepciones.ErrorNegocio;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.pagos.integracion.IntencionSimulada;
import ar.edu.ub.club.pagos.integracion.PasarelaSimuladaAdapter;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;

/**
 * Presentador de la pantalla P-27 Simulador de pasarela (MVP Vista Pasiva; no usa tipos de Vaadin). Es la única
 * clase de la interfaz que usa una clase de integración, {@code PasarelaSimuladaAdapter}: representa la página
 * externa de la pasarela, no un caso de uso del club. Muestra la intención y, al aprobar o rechazar, hace que el
 * adaptador notifique al webhook y lleva el navegador a la {@code urlRetorno}. Diagrama sec_cu35_p5.
 *
 * <p>Casos de uso: CU-35
 */
@SpringComponent
@UIScope
public class PresentadorSimuladorPasarela extends PresentadorBase<PantallaSimuladorPasarela> {

    /** Adaptador de la pasarela simulada. */
    private final PasarelaSimuladaAdapter pasarelaSimulada;

    /** Preferencia que se está mostrando. */
    private String idPreferencia;

    /**
     * Crea el presentador con el adaptador simulado.
     *
     * <p>Casos de uso: CU-35
     * @param pasarelaSimulada adaptador de la pasarela simulada
     */
    public PresentadorSimuladorPasarela(PasarelaSimuladaAdapter pasarelaSimulada) {
        this.pasarelaSimulada = pasarelaSimulada;
    }

    /**
     * Carga la intención: {@code PasarelaSimuladaAdapter.obtenerIntencion(idPreferencia)} → {@code
     * ResumenIntencionSimulada.desde(i)} → {@code pantalla.mostrarIntencion}. Si no existe, informa «La intención
     * de pago no existe o venció» y no se ofrecen los botones.
     *
     * <p>Casos de uso: CU-35
     * @param idPreferencia identificador de la preferencia (de la ruta)
     */
    public void cargar(String idPreferencia) {
        this.idPreferencia = idPreferencia;
        consultar(() -> {
            IntencionSimulada intencion = pasarelaSimulada.obtenerIntencion(idPreferencia);
            if (intencion == null) {
                throw new ErrorNegocio("La intención de pago no existe o venció");
            }
            return ResumenIntencionSimulada.desde(intencion);
        }).ifPresent(pantalla::mostrarIntencion);
    }

    /**
     * Resuelve el pago (paso 13): {@code PasarelaSimuladaAdapter.resolverPago(idPreferencia, aprobar)} envía la
     * notificación al webhook (paso 14) y devuelve la {@code urlRetorno} → {@code pantalla.redirigirA(urlRetorno)}.
     *
     * <p>Casos de uso: CU-35
     * @param aprobar {@code true} para aprobar, {@code false} para rechazar (FA8)
     */
    public void alResolver(boolean aprobar) {
        consultar(() -> pasarelaSimulada.resolverPago(idPreferencia, aprobar)).ifPresent(pantalla::redirigirA);
    }
}
