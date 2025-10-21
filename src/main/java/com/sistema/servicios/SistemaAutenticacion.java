package com.sistema.servicios;

import com.sistema.dao.UsuarioDAO;
import com.sistema.modelo.Usuario;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;

/**
 * Clase encargada de gestionar el registro, autenticación y verificación de usuarios.
 * Utiliza BCrypt para el hashing de contraseñas y maneja el envío de códigos de verificación por correo.
 */
public class SistemaAutenticacion {

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    /**
     * Registra un nuevo usuario en la base de datos con una contraseña encriptada y envía un código de verificación por correo.
     * @param nombre El nombre del usuario.
     * @param email El correo electrónico del usuario.
     * @param contrasena La contraseña del usuario.
     * @return true si el usuario se registra correctamente, false si el correo ya está registrado o ocurre un error.
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
     * Verifica una cuenta de usuario mediante un código de verificación.
     * @param token El código de verificación proporcionado por el usuario.
     * @return true si la verificación es exitosa, false si el código es inválido.
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
     * Verifica las credenciales de inicio de sesión de un usuario.
     * @param email El correo electrónico del usuario.
     * @param contrasena La contraseña proporcionada por el usuario.
     * @return true si las credenciales son correctas y el usuario está verificado, false en caso contrario.
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


    /**
     * Genera un código alfanumérico de 8 caracteres para la verificación de correo.
     * @return El código de verificación generado.
     */
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
     * Verifica si un correo electrónico tiene un formato válido.
     * @param correo El correo electrónico a validar.
     * @return true si el correo tiene un formato válido, false si no.
     */
    public boolean correoCorrecto(String correo){
        String patron = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@"+
                "(?:[a-zA-Z0-9-]+\\.)+[a-z]{2,7}$";

        return correo.matches(patron);
    }

    /**
     * Verifica si una contraseña cumple con los requisitos de seguridad (mínimo 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial).
     * @param contrasena La contraseña a verificar.
     * @return true si la contraseña es segura, false si no cumple con los requisitos.
     */
    public boolean contrasenaSegura(String contrasena){
        boolean mayus = false;
        boolean minus = false;
        boolean numero = false;
        boolean especial = false;

        for(char c : contrasena.toCharArray()){
            if(Character.isUpperCase(c)) mayus = true;
            if(Character.isLowerCase(c)) minus = true;
            if(Character.isDigit(c)) numero = true;
            if(!Character.isLetterOrDigit(c)) especial = true;
        }
        return (mayus && minus && numero && especial && (contrasena.length() >= 8));
    }

    /**
     * Genera un código numérico de 6 dígitos para la recuperación de contraseña.
     * @return El código de recuperación generado.
     */
    public String codigoRecuperacion(){
        String carac = "1234567890";
        SecureRandom rd = new SecureRandom();
        StringBuilder cod = new StringBuilder(6);

        for(int i = 0; i < 6; i++){
            int index = rd.nextInt(carac.length());
            cod.append(index);
        }
        return cod.toString();
    }
}

