package com.sistema;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;

/**
 * Clase de prueba para verificar las operaciones del DAO de usuarios.
 * Prueba el registro de un nuevo usuario y la búsqueda de un usuario por correo electrónico
 * utilizando la clase UsuarioDAO.
 */
public class TestUsuarioDAO {

    /**
     * Punto de entrada principal para la prueba del DAO de usuarios.
     * Crea un usuario de prueba, lo registra en la base de datos y realiza una búsqueda
     * por correo electrónico para verificar la funcionalidad de UsuarioDAO.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {

        UsuarioDAO dao = new UsuarioDAO();

        Usuario nuevo = new Usuario();
        nuevo.setNombreUsuario("daniel");
        nuevo.setEmail("daniel@example.com");
        nuevo.setContrasenaHash("123456");
        nuevo.setTokenVerificado("abc123");
        nuevo.setVerificado(false);

        if (dao.registrarUsuario(nuevo)) {
            System.out.println("Usuario registrado con ID: " + nuevo.getId());
        }

        Usuario encontrado = dao.buscarPorEmail("daniel@example.com");
        if (encontrado != null) {
            System.out.println("Usuario encontrado: " + encontrado.getNombreUsuario());
        }

    }
}
