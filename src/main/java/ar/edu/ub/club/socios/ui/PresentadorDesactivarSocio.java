package ar.edu.ub.club.socios.ui;

import ar.edu.ub.club.compartido.ui.FilaResumenReserva;
import ar.edu.ub.club.compartido.ui.OpcionSeleccion;
import ar.edu.ub.club.compartido.ui.PresentadorBase;
import ar.edu.ub.club.socios.aplicacion.ServicioSocios;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import java.util.List;

/**
 * Presentador del diálogo P-09 (baja de socio en dos pasos, {@code est_dialogo_baja}): al abrir pide
 * {@code desactivarSocio(id, null, false)} y muestra las reservas futuras que se cancelarán y, si corresponde, el
 * selector de nuevo titular; al confirmar pide {@code desactivarSocio(id, idNuevoTitular, true)} y cierra con
 * éxito, lo que dispara {@code DialogoResueltoEvento} para que P-08 recargue la ficha.
 *
 * <p>Presentador del patrón MVP (Vista Pasiva): no usa tipos de Vaadin; llama a la fachada dentro de
 * {@code consultar(...)} y convierte con {@code FilaResumenReserva.desde(...)}.
 *
 * <p>Casos de uso: CU-03
 */
@SpringComponent
@UIScope
public class PresentadorDesactivarSocio extends PresentadorBase<PantallaDesactivarSocio> {

    /** Fachada de socios. */
    private final ServicioSocios servicioSocios;

    /** Socio que se da de baja. */
    private Long idSocio;

    /**
     * Crea el presentador (inyección por constructor).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param servicioSocios fachada de socios
     */
    public PresentadorDesactivarSocio(ServicioSocios servicioSocios) {
        this.servicioSocios = servicioSocios;
    }

    /**
     * Primer paso (estado «ConsultandoAfectadas»): muestra el socio y pide
     * {@code ServicioSocios.desactivarSocio(idSocio, null, false)}; si {@code realizada} cierra; si no muestra
     * {@code FilaResumenReserva.desde(...)} y los candidatos a titular (estado «Advertencia»).
     *
     * <p>Casos de uso: CU-03
     * @param idSocio socio
     * @param nombre apellido y nombre
     * @param dni documento
     * @param candidatosTitular integrantes que pueden reemplazarlo como titular (vacía si no corresponde)
     */
    public void alAbrir(Long idSocio, String nombre, String dni, List<OpcionSeleccion> candidatosTitular) {
        this.idSocio = idSocio;
        pantalla.mostrarSocio(nombre, dni);
        consultar(() -> servicioSocios.desactivarSocio(idSocio, null, false)).ifPresent(resultado -> {
            if (resultado.realizada()) {
                pantalla.cerrarConExito();
            } else {
                List<FilaResumenReserva> filas = resultado.reservasAfectadas().stream()
                        .map(FilaResumenReserva::desde).toList();
                pantalla.mostrarAfectadas(filas, candidatosTitular);
            }
        });
    }

    /**
     * Segundo paso (estado «Confirmando»): {@code ServicioSocios.desactivarSocio(idSocio, idNuevoTitular, true)};
     * informa la baja y la cantidad de reservas canceladas y cierra con éxito ({@code DialogoResueltoEvento}). Un
     * {@code ErrorNegocio} (FA5, FA6) deja el diálogo abierto con el mensaje.
     *
     * <p>Casos de uso: CU-03
     * @param idNuevoTitular nuevo titular elegido o {@code null}
     */
    public void alConfirmar(Long idNuevoTitular) {
        consultar(() -> servicioSocios.desactivarSocio(idSocio, idNuevoTitular, true)).ifPresent(resultado -> {
            pantalla.mostrarMensaje("Socio dado de baja. Reservas canceladas: "
                    + resultado.reservasAfectadas().size() + ".");
            pantalla.cerrarConExito();
        });
    }
}
