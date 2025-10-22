package com.sistema.dao;

import com.sistema.modelo.Proveedor;
import com.sistema.util.ConexionMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    /**
     * Consulta todos los proveedores registrados en la base de datos.
     * @return Una lista con todos los proveedores.
     */
    public List<Proveedor> listaProveedores(){
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setContacto(rs.getString("contacto"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));

                lista.add(proveedor);
            }

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }

        return lista;
    }

    /**
     * Inserta un nuevo proveedor en la base de datos
     * @param proveedor objeto Proveedor con los datos a registrar
     * @return true si se insertó correctamente, false si ocurrió un error
     */
    public boolean agregarProveedor(Proveedor proveedor) {
        String sql = "INSERT INTO proveedores (nombre, contacto, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getContacto());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al insertar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza los datos de un proveedor existente en la base de datos.
     * @param proveedor objeto Proveedor con el ID y los nuevos datos a actualizar
     * @return true si se actualizó correctamente (filasAfectadas > 0), false si ocurrió un error
     */
    public boolean editarProveedor(Proveedor proveedor) {
        // Se requiere el ID del proveedor para identificar qué registro modificar.
        String sql = "UPDATE proveedores SET nombre = ?, contacto = ?, telefono = ?, email = ? WHERE id = ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            // 1. Asignar los nuevos valores a las columnas
            ps.setString(1, proveedor.getNombre());
            ps.setString(2, proveedor.getContacto());
            ps.setString(3, proveedor.getTelefono());
            ps.setString(4, proveedor.getEmail());

            // 2. Asignar el ID para la cláusula WHERE (¡CLAVE para la edición!)
            ps.setInt(5, proveedor.getId());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (SQLException e) {
            System.err.println("Error al editar proveedor: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un único proveedor por su nombre exacto en la base de datos.
     * @param nombreBus El nombre exacto del proveedor a buscar.
     * @return El objeto Proveedor encontrado o un objeto Proveedor vacío si no se encuentra.
     */
    public Proveedor buscarPorNombre(String nombreBus){

        String sql = "SELECT * FROM proveedores WHERE nombre = ?";
        Proveedor pro = new Proveedor();

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, nombreBus);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    pro.setId(rs.getInt("id"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setContacto(rs.getString("contacto"));
                    pro.setTelefono(rs.getString("telefono"));
                    pro.setEmail(rs.getString("email"));
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return pro;
    }


    /**
     * Busca proveedores cuyo nombre coincida parcial o totalmente con el texto proporcionado.
     * @param nombreBus El nombre o parte del nombre del proveedor a buscar.
     * @return Una lista de proveedores que coinciden con el criterio de búsqueda.
     */
    public List<Proveedor> buscarListaPorNombre(String nombreBus){

        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores WHERE nombre LIKE ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreBus + "%");

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    Proveedor pro = new Proveedor();
                    pro.setId(rs.getInt("id"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setContacto(rs.getString("contacto"));
                    pro.setTelefono(rs.getString("telefono"));
                    pro.setEmail(rs.getString("email"));

                    lista.add(pro);
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene la cantidad total de proveedores registrados en la base de datos.
     * @return Un entero que representa la cantidad de proveedores.
     */
    public int cantidadProveedores(){
        String sql = "SELECT COUNT(*) AS total_proveedores FROM proveedores;";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return rs.getInt("total_proveedores");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Busca un único proveedor por su identificador único en la base de datos.
     * @param idProveedor El ID del proveedor a buscar.
     * @return El objeto Proveedor encontrado o un objeto Proveedor vacío si no se encuentra.
     */
    public Proveedor buscarPorId(int idProveedor){

        String sql = "SELECT * FROM proveedores WHERE id = ?";
        Proveedor pro = new Proveedor();

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setInt(1, idProveedor);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    pro.setId(rs.getInt("id"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setContacto(rs.getString("contacto"));
                    pro.setTelefono(rs.getString("telefono"));
                    pro.setEmail(rs.getString("email"));
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return pro;
    }
}
