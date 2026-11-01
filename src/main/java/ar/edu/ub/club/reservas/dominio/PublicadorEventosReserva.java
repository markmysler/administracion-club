package ar.edu.ub.club.reservas.dominio;

import java.util.ArrayList;
import java.util.List;

/**
 * Sujeto del patrón Observer, en {@code reservas.dominio}, sin dependencias de Spring. Cada observador se
 * suscribe una sola vez al iniciar la aplicación (lo hace {@code ConfiguracionReservas} con todos los beans {@code
 * ObservadorReserva}). {@code publicar} entrega el evento en forma sincrónica, dentro de la transacción de la
 * operación y en el orden de suscripción; cada observador decide su momento: {@code AuditorReservas} escribe la
 * auditoría de inmediato (RNF-12) y {@code NotificadorReservas} difiere el envío hasta después del commit (RN-19).
 *
 * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
public class PublicadorEventosReserva {

    /** Observadores suscriptos (sin repetidos, en orden de suscripción). */
    private final List<ObservadorReserva> observadores = new ArrayList<>();

    /**
     * Agrega el observador si no estaba suscripto.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param o Observador.
     */
    public synchronized void suscribir(ObservadorReserva o) {
        if (o != null && !observadores.contains(o)) {
            observadores.add(o);
        }
    }

    /**
     * Invoca {@code alOcurrir(e)} en cada observador; una excepción de un observador se propaga y revierte la
     * operación.
     *
     * <p>Casos de uso: CU-03, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param e Evento a comunicar.
     */
    public void publicar(EventoReserva e) {
        List<ObservadorReserva> copia;
        synchronized (this) {
            copia = new ArrayList<>(observadores);
        }
        for (ObservadorReserva o : copia) {
            o.alOcurrir(e);
        }
    }
}
