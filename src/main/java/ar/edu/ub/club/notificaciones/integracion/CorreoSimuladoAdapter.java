package ar.edu.ub.club.notificaciones.integracion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

/**
 * Adaptador concreto del patrón Adapter (GoF) para desarrollo y demostración: en lugar de enviar el correo lo
 * registra en la bitácora y responde que se envió. Es el adaptador activo cuando {@code club.correo.modo=simulado}
 * (valor por omisión).
 *
 * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
@Component
@ConditionalOnProperty(name = "club.correo.modo", havingValue = "simulado", matchIfMissing = true)
public class CorreoSimuladoAdapter implements ServicioCorreo {

    /** Bitácora donde queda el correo simulado. */
    private static final Logger BITACORA = LoggerFactory.getLogger(CorreoSimuladoAdapter.class);

    /**
     * Crea el adaptador (bean de Spring).
     *
     * <p>Casos de uso: ninguno (configuración)
     */
    public CorreoSimuladoAdapter() {
        // sin dependencias
    }

    /**
     * Registra destinatario, asunto y cuerpo en la bitácora (nivel INFO) y devuelve éxito.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param mensaje mensaje a enviar
     * @return {@code ResultadoEnvio.exito()}
     */
    @Override
    public ResultadoEnvio enviar(MensajeCorreo mensaje) {
        BITACORA.info("[CORREO SIMULADO] Para: {} | Asunto: {}\n{}", mensaje.getDestinatario(), mensaje.getAsunto(),
                mensaje.getCuerpo());
        return ResultadoEnvio.exito();
    }
}
