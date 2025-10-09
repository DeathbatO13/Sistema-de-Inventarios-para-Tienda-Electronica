package com.sistema.servicios;

import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Clase para conectar al email remitente y configuracion del servicio
 */
public class ServicioEmail {

    //Credenciales del correo remitente
    private static final String REMITENTE = "electrostock.contacto@gmail.com";
    private static final String CONTRASENA = "ohpl zlca dkln ctop";
    private static final String HOST = "smtp.gmail.com";

    /**
     * Funcion que se encarga de enviar el correo de verificacion al destinatario
     * @param destinatario correo del usuario a verificar
     * @param asunto asunto del correo (verificacion de cuenta)
     * @param cuerpo texto del correo donde se adjunta el codigo de verificacion.
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
