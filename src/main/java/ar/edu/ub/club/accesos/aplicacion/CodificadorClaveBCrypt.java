package ar.edu.ub.club.accesos.aplicacion;

import ar.edu.ub.club.accesos.dominio.CodificadorClave;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Implementación de {@code CodificadorClave} con BCrypt (Spring Security, costo 10). BCrypt solo usa los primeros
 * 72 bytes de la clave: si la clave en UTF-8 los supera, primero se calcula su SHA-256 en Base64 (prehash, 44
 * bytes) para no truncarla en silencio (07B, clase {@code CodificadorClave}; RNF-03).
 *
 * <p>Agregado en la implementación: el diseño solo define la interfaz y nombra «la implementación BCrypt».
 *
 * <p>Casos de uso: CU-01, CU-11, CU-13, CU-14, CU-15, CU-16
 */
@Component
public class CodificadorClaveBCrypt implements CodificadorClave {

    /** Límite de bytes que procesa BCrypt. */
    private static final int LIMITE_BCRYPT = 72;

    /** Codificador BCrypt de Spring Security. */
    private final BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder(10);

    /**
     * Calcula el hash BCrypt de una contraseña nueva.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15, CU-16
     * @param clave Contraseña en texto plano.
     * @return Hash BCrypt.
     */
    @Override
    public String codificar(String clave) {
        return bcrypt.encode(preparar(clave));
    }

    /**
     * Verifica una contraseña contra un hash BCrypt guardado.
     *
     * <p>Casos de uso: CU-11
     * @param clave Contraseña ingresada.
     * @param hash Hash guardado.
     * @return true si coincide.
     */
    @Override
    public boolean coincide(String clave, String hash) {
        if (clave == null || hash == null) {
            return false;
        }
        return bcrypt.matches(preparar(clave), hash);
    }

    /**
     * Aplica el prehash SHA-256 (Base64) cuando la clave supera 72 bytes en UTF-8.
     *
     * <p>Casos de uso: CU-01, CU-11, CU-13, CU-14, CU-15
     * @param clave clave en texto plano
     * @return la misma clave o su prehash
     */
    private static String preparar(String clave) {
        byte[] bytes = clave.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= LIMITE_BCRYPT) {
            return clave;
        }
        try {
            return Base64.getEncoder().encodeToString(MessageDigest.getInstance("SHA-256").digest(bytes));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("SHA-256 no disponible", e);
        }
    }
}
