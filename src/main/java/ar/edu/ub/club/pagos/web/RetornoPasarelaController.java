package ar.edu.ub.club.pagos.web;

import java.util.UUID;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Controlador web mínimo (no REST) de la ruta pública {@code GET /pagos/retorno}, a la que la pasarela devuelve
 * el navegador ({@code urlRetorno}). Resuelve el retorno con las cookies {@code SameSite=Strict}: la vuelta
 * desde Mercado Pago es una navegación entre sitios y llega sin cookie de sesión; este controlador no usa la
 * sesión (no la crea ni la lee) y no muestra datos, sino que devuelve una página HTML mínima que lleva al
 * navegador a /pagos/resultado?referencia=(uuid) (P-26). Esa segunda navegación la inicia una página del propio
 * sitio, por lo que es del mismo sitio: el navegador envía la cookie de sesión y la de «Mantener la sesión
 * iniciada», y P-26 se abre con el socio autenticado.
 *
 * <p>Casos de uso: CU-35
 */
@Controller
public class RetornoPasarelaController {

    /**
     * Crea el controlador (sin atributos).
     *
     * <p>Casos de uso: CU-35
     */
    public RetornoPasarelaController() {
        // sin estado
    }

    /**
     * Valida que {@code referencia} sea un UUID (si no, Spring responde 400 por la conversión del parámetro) y
     * devuelve la página de redirección con {@code &lt;meta http-equiv="refresh"&gt;} y {@code location.replace(...)}
     * hacia /pagos/resultado?referencia=(uuid). La URL destino se arma con el UUID ya validado (sin inyección).
     *
     * <p>Casos de uso: CU-35
     * @param referencia Referencia externa del pago, parámetro de consulta.
     * @return Página HTML de redirección.
     */
    @GetMapping(value = "/pagos/retorno", produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public String retornar(@RequestParam("referencia") UUID referencia) {
        String destino = "/pagos/resultado?referencia=" + referencia;
        return "<!DOCTYPE html>\n<html lang=\"es\"><head><meta charset=\"utf-8\">"
                + "<meta http-equiv=\"refresh\" content=\"0;url=" + destino + "\">"
                + "<title>Volviendo al club</title></head><body>"
                + "<p>Volviendo al sistema del club… Si no se redirige, <a href=\"" + destino + "\">siga este enlace</a>.</p>"
                + "<script>location.replace('" + destino + "');</script>"
                + "</body></html>";
    }
}
