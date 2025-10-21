package com.sistema;

import com.sistema.servicios.ServicioEmail;

/**
 * Clase de prueba para verificar el envío de correos electrónicos.
 * Utiliza la clase ServicioEmail para enviar un correo de prueba a una dirección específica.
 */
public class TestEmail {

    /**
     * Punto de entrada principal para la prueba de envío de correo.
     * Envía un correo electrónico de prueba a una dirección predefinida con un asunto y cuerpo específicos.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        ServicioEmail.enviarCorreo(
                "danielalejo99@gmail.com",
                "Prueba de envío",
                "Hola, este es un mensaje de prueba del Sistema de Inventario."
        );
    }
}
