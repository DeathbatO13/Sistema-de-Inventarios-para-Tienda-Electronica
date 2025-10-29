package com.sistema.dao;

import com.sistema.modelo.Proveedor;
import com.sistema.util.ConexionMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) para la gestión de proveedores en la base de datos.
 * Proporciona métodos completos para operaciones CRUD (Crear, Leer, Actualizar, Eliminar)
 * sobre la entidad {@link Proveedor}, incluyendo búsquedas por nombre (exacta o parcial),
 * por ID, conteo total y eliminación transaccional con productos asociados.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Listar todos los proveedores.</li>
 *   <li>Agregar, editar y eliminar proveedores (con eliminación en cascada de productos).</li>
 *   <li>Buscar proveedor único por nombre exacto o ID.</li>
 *   <li>Buscar lista de proveedores por coincidencia parcial de nombre.</li>
 *   <li>Contar el total de proveedores registrados.</li>
 *   <li>Eliminación segura mediante transacciones con rollback en caso de error.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link ConexionMySQL} para conexiones y sentencias preparadas.
 * Los errores se registran en consola mediante {@code System.err}.</p>
 */
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
     * Elimina un proveedor y sus productos asociados de la base de datos.
     * Utiliza una transacción para garantizar que se eliminen primero los productos relacionados
     * y luego el proveedor. Si alguna operación falla, realiza un rollback.
     * @param idProveedor El ID del proveedor a eliminar.
     * @return true si el proveedor y sus productos se eliminan correctamente, false si ocurre un error o el proveedor no existe.
     */
    public boolean eliminarProveedor(int idProveedor) {
        String sqlDeleteProductos = "DELETE FROM productos WHERE id_proveedor = ?";
        String sqlDeleteProveedor = "DELETE FROM proveedores WHERE id = ?";


        Connection con = null;

        try {
            con = ConexionMySQL.getConexion();

            con.setAutoCommit(false); // Deshabilitamos el auto-commit


            try (PreparedStatement psProd = con.prepareStatement(sqlDeleteProductos)) {
                psProd.setInt(1, idProveedor);
                psProd.executeUpdate();
            }


            try (PreparedStatement psProv = con.prepareStatement(sqlDeleteProveedor)) {
                psProv.setInt(1, idProveedor);
                int filasAfectadas = psProv.executeUpdate();

                if (filasAfectadas > 0) {
                    // Si el proveedor se eliminó, confimar los cambios.
                    con.commit();
                    return true;
                } else {
                    // Si el proveedor no se encontró/eliminó, deshacemos la eliminación de productos (si hubo).
                    con.rollback();
                    return false;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error en transacción de eliminación: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al realizar rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            if (con != null) {
                try {
                    con.setAutoCommit(true);
                    con.close();
                } catch (SQLException e) {
                    System.err.println("Error al cerrar conexión: " + e.getMessage());
                }
            }
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
