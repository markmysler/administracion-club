package ar.edu.ub.club.accesos.dominio;

/**
 * Interfaz del cálculo y la verificación de hashes de contraseña; la implementación usa BCrypt con prehash
 * cuando la clave en UTF-8 supera 72 bytes (nunca la trunca en silencio).
 *
 * <p>Casos de uso: CU-01, CU-11, CU-13, CU-14, CU-15, CU-16
 */
public interface CodificadorClave {

    /**
     * Calcula el hash de una contraseña nueva.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @param clave Contraseña en texto plano.
     * @return Hash BCrypt.
     */
    String codificar(String clave);

    /**
     * Verifica una contraseña contra un hash.
     *
     * <p>Casos de uso: CU-11
     * @param clave Contraseña ingresada.
     * @param hash Hash guardado.
     * @return true si coincide.
     */
    boolean coincide(String clave, String hash);
}
