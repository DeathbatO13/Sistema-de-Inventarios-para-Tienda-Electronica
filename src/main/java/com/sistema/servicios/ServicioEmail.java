package com.sistema.servicios;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Clase de utilidad para el envío de correos electrónicos mediante el protocolo SMTP.
 * Utiliza las credenciales de una cuenta de Gmail configurada con App Password
 * para enviar mensajes de verificación, recuperación de contraseña u otros avisos.
 *
 * <p>Funcionalidades:
 * <ul>
 *   <li>Envío de correos con asunto y cuerpo personalizados a un destinatario.</li>
 *   <li>Configuración segura mediante TLS y autenticación SMTP.</li>
 *   <li>Manejo de excepciones con registro de errores en consola.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Advertencia:</strong> Las credenciales están hardcoded.
 * En entornos de producción, se recomienda usar variables de entorno o un archivo de configuración seguro.</p>
 */
public class ServicioEmail {

    //Credenciales del correo remitente
    private static final String REMITENTE = "electrostock.contacto@gmail.com";
    private static final String CONTRASENA = "ohpl zlca dkln ctop";
    private static final String HOST = "smtp.gmail.com";

    /**
     * Envía un correo electrónico al destinatario especificado con el asunto y cuerpo proporcionados.
     * Utiliza las credenciales y configuración del servidor SMTP de Gmail para enviar el mensaje.
     * @param destinatario El correo electrónico del usuario destinatario.
     * @param asunto El asunto del correo, como verificación de cuenta.
     * @param cuerpo El contenido del correo, que puede incluir un código de verificación.
     */
    public static void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(REMITENTE, CONTRASENA);
            }
        });

        try {
            Message mensaje = new MimeMessage(session);
            mensaje.setFrom(new InternetAddress(REMITENTE));
            mensaje.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            mensaje.setSubject(asunto);
            mensaje.setText(cuerpo);

            Transport.send(mensaje);
            System.out.println("Correo enviado correctamente a " + destinatario);

        } catch (MessagingException e) {
            System.err.println("Error al enviar el correo: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
