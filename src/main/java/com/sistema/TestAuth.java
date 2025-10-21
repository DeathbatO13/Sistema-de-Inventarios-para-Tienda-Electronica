package com.sistema;

import com.sistema.servicios.SistemaAutenticacion;

/**
 * Clase de prueba para el sistema de autenticación.
 * Ejecuta pruebas básicas para registrar un usuario y realizar intentos de inicio de sesión
 * con credenciales correctas e incorrectas utilizando la clase SistemaAutenticacion.
 */
public class TestAuth {

    /**
     * Punto de entrada principal para las pruebas de autenticación.
     * Registra un usuario de prueba y realiza intentos de inicio de sesión con
     * credenciales correctas e incorrectas.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
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
