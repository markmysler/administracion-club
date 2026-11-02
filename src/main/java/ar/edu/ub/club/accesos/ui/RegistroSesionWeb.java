package ar.edu.ub.club.accesos.ui;

import ar.edu.ub.club.accesos.aplicacion.SesionUsuario;

/**
 * Interfaz de {@code accesos.ui} que oculta al presentador los detalles del Servlet y de Vaadin al iniciar y
 * cerrar la sesión del servidor (Protected Variations). La implementa {@code RegistroSesionVaadin}; en las pruebas
 * se reemplaza por un doble.
 *
 * <p>Casos de uso: CU-11, CU-12
 */
public interface RegistroSesionWeb {

    /**
     * Cambia el identificador de la sesión (prevención de fijación de sesión), guarda el {@code SecurityContext} en
     * la sesión HTTP, registra la sesión en el {@code SessionRegistry} y, si {@code sesion.tokenPersistente} existe
     * («Mantener la sesión iniciada», solo cuentas de socio), escribe la cookie remember-me de 30 días.
     *
     * <p>Casos de uso: CU-11
     * @param sesion Resultado de {@code ServicioAccesos.iniciarSesion}.
     */
    void iniciar(SesionUsuario sesion);

    /**
     * Ejecuta {@code SecurityContextLogoutHandler}, invalida la sesión HTTP y la {@code VaadinSession} y borra la
     * cookie remember-me.
     *
     * <p>Casos de uso: CU-12
     */
    void cerrar();
}
