package com.sistema;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;

public class TestUsuarioDAO {
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
