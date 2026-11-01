package ar.edu.ub.club.notificaciones.integracion;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

/**
 * Adaptador concreto del patrón Adapter (GoF): adapta {@code JavaMailSender} de Spring (SMTP con TLS, servidor
 * configurado en {@code spring.mail.*}) a la interfaz {@code ServicioCorreo}. Es el adaptador activo cuando {@code
 * club.correo.modo=smtp}. Una falla del proveedor se devuelve como {@code ResultadoEnvio.fallo}, sin excepción
 * (RN-19).
 *
 * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
 */
@Component
@ConditionalOnProperty(name = "club.correo.modo", havingValue = "smtp")
public class CorreoSmtpAdapter implements ServicioCorreo {

    /** Cliente SMTP de Spring (adaptado). */
    private final JavaMailSender enviadorCorreo;

    /** Dirección del remitente ({@code club.correo.remitente}). */
    private final String remitente;

    /**
     * Crea el adaptador (bean de Spring).
     *
     * <p>Casos de uso: ninguno (configuración)
     * @param enviadorCorreo cliente SMTP de Spring
     * @param remitente dirección del remitente
     */
    public CorreoSmtpAdapter(JavaMailSender enviadorCorreo, @Value("${club.correo.remitente}") String remitente) {
        this.enviadorCorreo = enviadorCorreo;
        this.remitente = remitente;
    }

    /**
     * Arma un {@code SimpleMailMessage} y lo envía por SMTP.
     *
     * <p>Casos de uso: CU-03, CU-13, CU-20, CU-23, CU-27, CU-28, CU-30, CU-32, CU-33
     * @param mensaje mensaje a enviar
     * @return éxito, o fallo con el mensaje de la excepción del proveedor
     */
    @Override
    public ResultadoEnvio enviar(MensajeCorreo mensaje) {
        SimpleMailMessage correo = new SimpleMailMessage();
        correo.setFrom(remitente);
        correo.setTo(mensaje.getDestinatario());
        correo.setSubject(mensaje.getAsunto());
        correo.setText(mensaje.getCuerpo());
        try {
            enviadorCorreo.send(correo);
            return ResultadoEnvio.exito();
        } catch (MailException e) {
            return ResultadoEnvio.fallo(e.getMessage());
        }
    }
}
