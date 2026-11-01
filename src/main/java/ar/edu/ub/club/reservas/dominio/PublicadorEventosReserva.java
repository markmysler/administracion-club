package ar.edu.ub.club.reservas.dominio;

import java.util.List;

/**
 * Sujeto del patrón Observer, en {@code reservas.dominio}, sin dependencias de Spring. Cada observador se
 * suscribe una sola vez al iniciar la aplicación. {@code publicar} entrega el evento en forma sincrónica, dentro
 * de la transacción de la operación y en el orden de suscripción.
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class PublicadorEventosReserva {

    /** Observadores suscriptos. */
    private List<ObservadorReserva> observadores;

    /**
     * Agrega el observador si no estaba suscripto.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param o Observador.
     */
    public void suscribir(ObservadorReserva o) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * Invoca {@code alOcurrir(e)} en cada observador; una excepción de un observador se propaga y revierte la
     * operación.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e Evento a comunicar.
     */
    public void publicar(EventoReserva e) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
