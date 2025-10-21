package com.sistema.dao;

import com.sistema.modelo.Usuario;
import com.sistema.util.ConexionMySQL;

import java.sql.*;

/**
 * DAO para operaciones CRUD sobre la tabla 'usuarios'.
 *
 */
public class UsuarioDAO {

    /**
     * Registra un nuevo usuario en la base de datos.
     * @param usuario El objeto Usuario con los datos a registrar.
     * @return true si el usuario se inserta correctamente, false si ocurre un error o el email ya está registrado.
     */
    public boolean registrarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre_usuario, email, contrasena_hash, token_verificacion, verificado) "
                + "VALUES (?, ?, ?, ?, ?)";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, usuario.getNombreUsuario());
            ps.setString(2, usuario.getEmail());
            ps.setString(3, usuario.getContrasenaHash());
            ps.setString(4, usuario.getTokenVerificado());
            ps.setBoolean(5, usuario.isVerificado());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) usuario.setId(rs.getInt(1));
                }
                return true;
            }

        } catch (SQLIntegrityConstraintViolationException e) {
            System.err.println(" El email ya está registrado: " + usuario.getEmail());
        } catch (SQLException e) {
            System.err.println(" Error al registrar usuario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Busca un usuario en la base de datos por su correo electrónico.
     * @param email El correo electrónico del usuario a buscar.
     * @return El objeto Usuario si se encuentra, null si no.
     */
    public Usuario buscarPorEmail(String email) {
        String sql = "SELECT * FROM usuarios WHERE email = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                    usuario.setTokenVerificado(rs.getString("token_verificacion"));
                    usuario.setVerificado(rs.getBoolean("verificado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Busca un usuario en la base de datos por su token de verificación.
     * @param token El token único de verificación.
     * @return El objeto Usuario con el token proporcionado, o null si no se encuentra.
     */
    public Usuario buscarPorToken(String token){
        String sql = "SELECT * FROM usuarios WHERE token_verificacion = ?";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, token);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Usuario usuario = new Usuario();
                    usuario.setId(rs.getInt("id"));
                    usuario.setNombreUsuario(rs.getString("nombre_usuario"));
                    usuario.setEmail(rs.getString("email"));
                    usuario.setContrasenaHash(rs.getString("contrasena_hash"));
                    usuario.setTokenVerificado(rs.getString("token_verificacion"));
                    usuario.setVerificado(rs.getBoolean("verificado"));
                    return usuario;
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar usuario: " + e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza el estado de verificación de un usuario en la base de datos, marcándolo como verificado y eliminando el token.
     * @param usuario El usuario cuya verificación se actualizará.
     * @return true si la actualización es exitosa, false si ocurre un error.
     */
    public boolean actualizarVerificacion(Usuario usuario) {
        String sql = "UPDATE usuarios SET verificado = ?, token_verificacion = NULL WHERE email = ?";
        try (Connection conn = ConexionMySQL.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, 1); // marcar como verificado
            stmt.setString(2, usuario.getEmail());

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar verificación: " + e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si un usuario está registrado en la base de datos según su correo electrónico.
     * @param usuario El usuario cuyo correo se verificará.
     * @return true si el usuario está registrado, false si no lo está o si ocurre un error.
     */
    public boolean estaRegistrado(Usuario usuario){
        String sql = "SELECT 1 FROM usuarios WHERE email = ? LIMIT 1";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement stm = con.prepareStatement(sql)){

            stm.setString(1, usuario.getEmail());
            ResultSet rs = stm.executeQuery();

            return rs.next();
        }catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Cambia la contraseña de un usuario en la base de datos.
     * @param user El usuario con el correo y la nueva contraseña hash.
     * @return true si el cambio de contraseña es exitoso, false si ocurre un error.
     */
    public boolean cambiarPassword(Usuario user){
        String sql = "UPDATE usuarios SET contrasena_hash = ? WHERE email = ?";
        try (Connection conn = ConexionMySQL.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getContrasenaHash());
            stmt.setString(2, user.getEmail());

            int filas = stmt.executeUpdate();
            return filas > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar verificación: " + e.getMessage());
            return false;
        }
    }
}
