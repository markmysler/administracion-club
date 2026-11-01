package ar.edu.ub.club.cuotas.dominio;

import ar.edu.ub.club.compartido.dominio.Dinero;
import ar.edu.ub.club.compartido.persistencia.EntidadPersistente;
import ar.edu.ub.club.socios.dominio.CategoriaSocio;
import ar.edu.ub.club.socios.dominio.Socio;

/**
 * Aporte de cada integrante activo a la cuota, copiado al emitirla: conserva socio, categoría e importe de ese
 * momento. Extiende {@code EntidadPersistente}.
 *
 * <p>Casos de uso: PENDIENTE
 */
public class ItemCuota extends EntidadPersistente {

    /** Integrante. */
    private Socio socio;

    /** Categoría del socio al emitir. */
    private CategoriaSocio categoria;

    /** Importe de la categoría al emitir. */
    private Dinero importe;

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param id valor de {@code id}
     * @param importe valor de {@code importe}
     * @return el resultado
     */
    public static ItemCuota reconstituir(Long id, Dinero importe) {
        throw new UnsupportedOperationException("PENDIENTE");
    }

    /**
     * PENDIENTE: documentar.
     *
     * <p>Casos de uso: PENDIENTE
     * @param socio valor de {@code socio}
     * @param categoria valor de {@code categoria}
     */
    public void restaurarAsociaciones(Socio socio, CategoriaSocio categoria) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
