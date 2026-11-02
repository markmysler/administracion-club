package ar.edu.ub.club.accesos.dominio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Conjunto de contraseñas comunes o filtradas, cargado desde un recurso de la aplicación al iniciar
 * ({@code ar/edu/ub/club/accesos/dominio/contrasenas-prohibidas.txt}, una por línea; las líneas que empiezan con
 * {@code #} son comentarios). Bean de Spring (único) que usa {@code PoliticaContrasenas} (RNF-03, NIST SP
 * 800-63B-4: comparar con listas de claves comprometidas).
 *
 * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
 */
@Component
public class ListaContrasenasProhibidas {

    /** Recurso del classpath con la lista. */
    private static final String RECURSO = "/ar/edu/ub/club/accesos/dominio/contrasenas-prohibidas.txt";

    /** Contraseñas prohibidas normalizadas a minúsculas. */
    private Set<String> contrasenas;

    /**
     * Carga la lista desde el recurso de la aplicación.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @throws UncheckedIOException si el recurso no existe o no se puede leer
     */
    @Autowired
    public ListaContrasenasProhibidas() {
        this.contrasenas = cargar();
    }

    /**
     * Crea la lista con un conjunto dado (pruebas unitarias). Agregado en la implementación.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param contrasenas contraseñas prohibidas (se normalizan)
     */
    public ListaContrasenasProhibidas(Set<String> contrasenas) {
        this.contrasenas = new HashSet<>();
        for (String c : contrasenas) {
            this.contrasenas.add(normalizar(c));
        }
    }

    /**
     * Indica si la clave, normalizada, está en la lista.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param clave Contraseña propuesta.
     * @return true si está prohibida.
     */
    public boolean contiene(String clave) {
        return clave != null && contrasenas.contains(normalizar(clave));
    }

    /**
     * Normaliza una clave para compararla: sin espacios en los extremos y en minúsculas.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @param clave clave
     * @return clave normalizada
     */
    private static String normalizar(String clave) {
        return clave.strip().toLowerCase(Locale.ROOT);
    }

    /**
     * Lee el recurso línea por línea.
     *
     * <p>Casos de uso: CU-01, CU-13, CU-14, CU-15
     * @return conjunto normalizado
     */
    private static Set<String> cargar() {
        Set<String> resultado = new HashSet<>();
        try (InputStream entrada = ListaContrasenasProhibidas.class.getResourceAsStream(RECURSO)) {
            if (entrada == null) {
                throw new UncheckedIOException(new IOException("No se encontró el recurso " + RECURSO));
            }
            BufferedReader lector = new BufferedReader(new InputStreamReader(entrada, StandardCharsets.UTF_8));
            String linea;
            while ((linea = lector.readLine()) != null) {
                if (!linea.isBlank() && !linea.startsWith("#")) {
                    resultado.add(normalizar(linea));
                }
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        return resultado;
    }
}
