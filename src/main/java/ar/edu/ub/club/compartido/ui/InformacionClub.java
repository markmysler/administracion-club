package ar.edu.ub.club.compartido.ui;

/**
 * Interfaz con el nombre del club que muestra el encabezado; la implementa
 * {@code configuracion.ui.InformacionClubConfigurada}.
 *
 * <p>Casos de uso: CU-41
 */
public interface InformacionClub {

    /**
     * Nombre del club configurado.
     *
     * <p>Casos de uso: CU-41
     * @return nombre
     */
    String nombreClub();
}
