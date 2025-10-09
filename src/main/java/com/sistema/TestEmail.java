package com.sistema;

import com.sistema.servicios.ServicioEmail;

public class TestEmail {
    public static void main(String[] args) {
        ServicioEmail.enviarCorreo(
                "danielalejo99@gmail.com",
                "Prueba de envío",
                "Hola, este es un mensaje de prueba del Sistema de Inventario."
        );
    }
}
