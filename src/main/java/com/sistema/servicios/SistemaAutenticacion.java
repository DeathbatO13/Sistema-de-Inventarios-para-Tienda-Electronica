package com.sistema.servicios;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Clase encargada de manejar el registro y autenticación de usuarios.
 * Incluye hashing de contraseñas con BCrypt.
 */
public class SistemaAutenticacion {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Registra un nuevo usuario con contraseña encriptada.
     */
    public boolean registrarUsuario(String nombre, String email, String contrasena) {
        // Verificar si el correo ya existe
        if (usuarioDAO.buscarPorEmail(email) != null) {
            System.out.println("El correo ya está registrado.");
            return false;
        }

        // Encriptar la contraseña
        String contrasenaHash = BCrypt.hashpw(contrasena, BCrypt.gensalt());

        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setEmail(email);
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setVerificado(true); // opcional, si no usarás verificación por token ahora

        return usuarioDAO.registrarUsuario(usuario);
    }

    /**
     * Verifica las credenciales de inicio de sesión.
     */
    public boolean iniciarSesion(String email, String contrasena) {
        Usuario usuario = usuarioDAO.buscarPorEmail(email);

        if (usuario == null) {
            System.out.println("Usuario no encontrado.");
            return false;
        }

        // Comparar contraseña ingresada con el hash
        if (BCrypt.checkpw(contrasena, usuario.getContrasenaHash())) {
            System.out.println("Inicio de sesión exitoso. Bienvenido " + usuario.getNombreUsuario());
            return true;
        } else {
            System.out.println("Contraseña incorrecta.");
            return false;
        }
    }
}

