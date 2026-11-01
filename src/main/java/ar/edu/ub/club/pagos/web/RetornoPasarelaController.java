package ar.edu.ub.club.pagos.web;

import java.util.UUID;

/**
 * Controlador web mínimo (no REST) de la ruta pública {@code GET /pagos/retorno}, a la que la pasarela devuelve
 * el navegador ({@code urlRetorno}). Resuelve el retorno con las cookies {@code SameSite=Strict}: la vuelta
 * desde Mercado Pago es una navegación entre sitios y llega sin cookie de sesión; este controlador no usa la
 * sesión (no la crea ni la lee) y no muestra datos, sino que devuelve una página HTML mínima que lleva al
 * navegador a /pagos/resultado?referencia=(uuid} (P-26). Esa segunda navegación la inicia una página del propio
 * sitio, por lo que es del mismo sitio: el navegador envía la cookie de sesión y la de «Mantener la sesión
 * iniciada», y P-26 se abre con el socio autenticado.
 *
 * <p>Casos de uso: CU-35
 */
public class RetornoPasarelaController {

    /**
     * Valida que {@code referencia} sea un UUID (si no, responde 400) y devuelve la página de redirección con {@code
     * &lt;meta http-equiv="refresh"&gt;} y {@code location.replace(...)} hacia /pagos/resultado?referencia=(uuid}.
     *
     * <p>Casos de uso: CU-35
     * @param referencia Referencia externa del pago, parámetro de consulta.
     * @return Página HTML de redirección.
     */
    public String retornar(UUID referencia) {
        throw new UnsupportedOperationException("PENDIENTE");
    }
}
