package com.sistema;

import com.sistema.servicios.SistemaAutenticacion;

public class TestAuth {
    public static void main(String[] args) {
        SistemaAutenticacion auth = new SistemaAutenticacion();

        // Registro
        auth.registrarUsuario("daniel", "daniel@example.com", "123456");

        // Inicio de sesión correcto
        auth.iniciarSesion("daniel@example.com", "123456");

        // Intento con contraseña incorrecta
        auth.iniciarSesion("daniel@example.com", "000000");
    }
}
