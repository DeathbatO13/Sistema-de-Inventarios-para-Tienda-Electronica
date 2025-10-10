package com.sistema.servicios;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

/**
 * Clase encargada de manejar el registro y autenticación de usuarios.
 * Incluye hashing de contraseñas con BCrypt.
 */
public class SistemaAutenticacion {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    public static String generarCodigo() {
        String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder codigo = new StringBuilder(8);
        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(caracteres.length());
            codigo.append(caracteres.charAt(index));
        }
        return codigo.toString();
    }

    /**
     * Registra un nuevo usuario con contraseña encriptada y envia el codigo de verificacion por correo.
     */
    public boolean registrarUsuario(String nombre, String email, String contrasena) {
        // Verificar si el correo ya existe
        if (usuarioDAO.buscarPorEmail(email) != null) {
            System.out.println("El correo ya está registrado.");
            return false;
        }
        // Encriptar la contraseña y generar codigo
        String contrasenaHash = BCrypt.hashpw(contrasena, BCrypt.gensalt());
        String codigoVerificacion = generarCodigo();
        // Crear el usuario
        Usuario usuario = new Usuario();
        usuario.setNombreUsuario(nombre);
        usuario.setEmail(email);
        usuario.setContrasenaHash(contrasenaHash);
        usuario.setTokenVerificado(codigoVerificacion);
        usuario.setVerificado(false);
        //Envia correo para verificacion
        ServicioEmail.enviarCorreo(usuario.getEmail(),
                "Verificación de cuenta",
                "Tu cuenta ha sido registrada en el sistema de inventario de ElectroStock.\n" +
                        "Para poder acceder al sistema necesitamos que estés verificado.\n" +
                        "Tu código de verificación es el siguiente:\n" + codigoVerificacion);
        //Guarda el usuario en DB
        return usuarioDAO.registrarUsuario(usuario);
    }

    /**
     * Funcion para validar la verificacion de una cuanta registrada
     * @param token token o codigo que el usuario digita en el campo
     * @return true si se verifica con exito, y false si el codigo es invalido
     */
    public boolean verificarCuenta(String token){
        UsuarioDAO userDAO = new UsuarioDAO();
        Usuario user = usuarioDAO.buscarPorToken(token);

        if(user != null){
            user.setVerificado(true);
            user.setTokenVerificado(null);
            userDAO.actualizarVerificacion(user);
            System.out.println("Cuenta verificado correctamente");
            return true;
        }else{
            System.out.println("Token invalido");
            return false;
        }
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

        // Comparar contraseña ingresada con el hash y que este verificado
        if (BCrypt.checkpw(contrasena, usuario.getContrasenaHash()) && usuario.isVerificado()) {
            System.out.println("Inicio de sesión exitoso. Bienvenido " + usuario.getNombreUsuario());
            return true;
        } else {
            System.out.println("Contraseña incorrecta.");
            return false;
        }
    }
}

