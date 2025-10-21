package com.sistema.dao;


import com.sistema.modelo.Producto;
import com.sistema.util.ConexionMySQL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la tabla 'productos'.
 *
 */
public class ProductoDAO {

    /**
     * Consulta todos los productos disponibles en la base de datos.
     * @return Una lista con todos los productos registrados.
     */
    public List<Producto> listaProductos(){
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()) {

                Producto producto = new Producto();
                producto.setId(rs.getInt("id"));
                producto.setCodigoSku(rs.getString("codigo_sku"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
                producto.setIdProveedor(rs.getInt("id_proveedor"));

                lista.add(producto);
            }

        }catch (SQLException e){
            System.err.println("Error al consultar productos con bajo stock: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Agrega un nuevo producto a la base de datos.
     * @param producto El producto a agregar.
     * @return true si el producto se agrega correctamente, false si ocurre un error.
     */
    public boolean agregarNuevoProducto(Producto producto){
        String sql = "INSERT INTO productos (codigo_sku, nombre, descripcion, precio_compra, precio_venta, " +
                "stock_actual, stock_minimo, id_proveedor) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setString(1, producto.getCodigoSku());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecioCompra());
            ps.setDouble(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getStockActual());
            ps.setInt(7, producto.getStockMinimo());
            ps.setInt(8, producto.getIdProveedor());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) producto.setId(rs.getInt(1));
                }
                return true;
            }

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }

    /**
     * Busca productos en la base de datos cuyo nombre coincida parcial o totalmente con el texto proporcionado.
     * @param nombreBuscado El nombre o parte del nombre del producto a buscar.
     * @return Una lista de productos que coinciden con el criterio de búsqueda.
     */
    public List<Producto> buscarPorNombre(String nombreBuscado) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigoSku(rs.getString("codigo_sku"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStockActual(rs.getInt("stock_actual"));

                    productos.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Consulta los productos cuyo stock actual es menor o igual al stock mínimo definido.
     * @return Una lista de productos con bajo stock.
     */
    public List<Producto> stockBajo() {
        List<Producto> productosBajoStock = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE stock_actual <= stock_minimo";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Producto producto = new Producto();
                producto.setCodigoSku(rs.getString("codigo_sku"));
                producto.setNombre(rs.getString("nombre"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setIdProveedor(rs.getInt("id_proveedor"));

                productosBajoStock.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar productos con bajo stock: " + e.getMessage());
        }

        return productosBajoStock;
    }

    /**
     * Obtiene la cantidad total de productos registrados en la base de datos.
     * @return Un entero que representa la cantidad de productos.
     */
    public int cantidadProductos(){
        String sql = "SELECT COUNT(*) AS total_productos FROM productos;";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return rs.getInt("total_productos");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return 0;
    }

    /**
     * Busca un producto en la base de datos por su identificador único.
     * @param id El ID del producto a buscar.
     * @return El producto encontrado o un objeto Producto vacío si no se encuentra.
     */
    public Producto buscarPorId(int id){
        String sql = "SELECT * FROM productos WHERE id = ?";
        Producto p = new Producto();

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, String.valueOf(id));

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    p.setId(rs.getInt("id"));
                    p.setCodigoSku(rs.getString("codigo_sku"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecioCompra(rs.getDouble("precio_compra"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStockActual(rs.getInt("stock_actual"));
                    p.setStockMinimo(rs.getInt("stock_minimo"));
                    p.setIdProveedor(rs.getInt("id_proveedor"));
                }
            }catch(SQLException e){
                System.err.println("No existe ese producto" + e.getMessage());
            }
        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return p;
    }

    /**
     * Actualiza los datos de un producto existente en la base de datos.
     * @param producto El producto con los datos actualizados.
     * @return true si la actualización es exitosa, false si ocurre un error.
     */
    public boolean actualizarProducto(Producto producto){
        String sql = "UPDATE productos SET codigo_sku = ?, nombre = ?, descripcion = ?, precio_compra = ?, precio_venta = ?, " +
                "stock_actual = ?, stock_minimo = ?, id_proveedor = ? WHERE id = ?";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, producto.getCodigoSku());
            ps.setString(2, producto.getNombre());
            ps.setString(3, producto.getDescripcion());
            ps.setDouble(4, producto.getPrecioCompra());
            ps.setDouble(5, producto.getPrecioVenta());
            ps.setInt(6, producto.getStockActual());
            ps.setInt(7, producto.getStockMinimo());
            ps.setInt(8, producto.getIdProveedor());
            ps.setInt(9, producto.getId());

            int filas = ps.executeUpdate();

            System.out.println("filas: "+filas);
            return filas > 0;

        }catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }

    }

    /**
     * Actualiza el stock actual de un producto en la base de datos.
     * @param idProducto El ID del producto a actualizar.
     * @param cantidadNueva La nueva cantidad de stock.
     * @return true si la actualización es exitosa, false si ocurre un error.
     */
    public boolean actualizarCantidadProducto(int idProducto, int cantidadNueva){
        String sql = "UPDATE productos SET stock_actual = ? WHERE id = ?";
        try(Connection conn = ConexionMySQL.getConexion();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, cantidadNueva); // marcar como verificado
            stmt.setInt(2, idProducto);

            int filas = stmt.executeUpdate();
            return filas > 0;

        }catch(SQLException e){
            System.err.println(e.getMessage());
            return false;
        }
    }

    /**
     * Busca productos en la base de datos asociados a un proveedor específico.
     * @param idProveedor El ID del proveedor.
     * @return Una lista de productos asociados al proveedor.
     */
    public List<Producto> buscarPorProveedor(int idProveedor) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE id_proveedor = ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProveedor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigoSku(rs.getString("codigo_sku"));
                    p.setNombre(rs.getString("nombre"));
                    p.setDescripcion(rs.getString("descripcion"));
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStockActual(rs.getInt("stock_actual"));

                    productos.add(p);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar productos: " + e.getMessage());
        }
        return productos;
    }

    /**
     * Elimina un producto de la base de datos según su ID.
     * @param id El ID del producto a eliminar.
     * @return true si el producto se elimina correctamente, false si ocurre un error.
     */
    public boolean eliminarProducto(int id) {
        String sql = "DELETE FROM productos WHERE id = ?";
        try (Connection conn = ConexionMySQL.getConexion();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            System.out.println("id: " + id);
            stmt.setInt(1, id);
            int filas = stmt.executeUpdate();
            System.out.println(filas);
            return filas > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar producto: " + e.getMessage());
            return false;
        }
    }

    /**
     * Busca un único producto en la base de datos cuyo nombre coincida parcial o totalmente con el texto proporcionado.
     * @param nombreBuscado El nombre o parte del nombre del producto a buscar.
     * @return El primer producto encontrado que coincide con el criterio o null si no se encuentra.
     */
    public Producto buscarUnicoPorNombre(String nombreBuscado) {
        Producto producto = null;
        String sql = "SELECT * FROM productos WHERE nombre LIKE ? LIMIT 1";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    producto = new Producto();
                    producto.setId(rs.getInt("id"));
                    producto.setCodigoSku(rs.getString("codigo_sku"));
                    producto.setNombre(rs.getString("nombre"));
                    producto.setDescripcion(rs.getString("descripcion"));
                    producto.setPrecioVenta(rs.getDouble("precio_venta"));
                    producto.setStockActual(rs.getInt("stock_actual"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error al buscar producto por nombre: " + e.getMessage());
        }
        return producto;
    }
}