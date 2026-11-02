package ar.edu.ub.club.grupos.ui;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Fila de un integrante del grupo familiar (P-05, P-08, P-11, P-24): datos, categoría con su importe, estado y si
 * es el titular.
 *
 * <p>Modelo de vista inmutable (record) que construye el presentador con {@code desde(socio, titular)}.
 *
 * <p>Casos de uso: CU-04, CU-05, CU-08, CU-09, CU-10
 *
 * @param idSocio socio
 * @param dni documento
 * @param apellido apellido
 * @param nombre nombre
 * @param categoria nombre de la categoría
 * @param importe importe de la categoría
 * @param estado estado de la cuenta
 * @param esTitular si es el titular del grupo
 */
public record FilaIntegrante(Long idSocio, String dni, String apellido, String nombre, String categoria, Dinero importe, String estado, boolean esTitular) {

    /**
     * Construye la fila de un integrante.
     *
     * <p>Casos de uso: CU-04, CU-05, CU-08, CU-09, CU-10
     * @param socio integrante
     * @param titular titular del grupo o {@code null}
     * @return fila
     */
    public static FilaIntegrante desde(Socio socio, Socio titular) {
        return new FilaIntegrante(socio.getId(), socio.getDni(), socio.getApellido(), socio.getNombre(),
                socio.getCategoria().getNombre(), socio.getCategoria().getImporte(), socio.getEstado().name(),
                titular != null && titular.equals(socio));
    }
}
