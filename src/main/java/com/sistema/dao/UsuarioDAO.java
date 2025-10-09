package com.sistema.dao;

import com.sistema.modelo.Usuario;
import com.sistema.util.ConexionMySQL;

import java.sql.*;

/**
 * DAO para operaciones CRUD sobre la tabla 'usuarios'.
 * Permite registrar y buscar usuarios por email.
 */
public class UsuarioDAO {

    /**
     * Inserta un nuevo usuario en la base de datos.
     * @param usuario objeto Usuario con los datos a registrar
     * @return true si se insertó correctamente, false si hubo error o el email ya existe
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
     * Busca un usuario por su email.
     * @param email correo del usuario
     * @return objeto Usuario si se encuentra, null si no
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
     * Buscar usuario por token para validacion
     * @param token token unico para validacion
     * @return objeto usuario con token ingresado
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
     * Actualiza el esto de la verificacion en la base de datos
     * @param usuario usuario verificado
     * @return true si actualiza correctamente, false si ocurre algun error
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

}
