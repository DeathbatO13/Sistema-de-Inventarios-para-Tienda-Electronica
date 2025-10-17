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
     * Funcion para consultar todos los productos disponibles,
     * mostrados en el panel de productos
     * @return lista con todos los productos
     */
    public List<Producto> listaProductos(){
        List<Producto> lista = new ArrayList<>();
        String sql = "SELECT * FROM productos";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()) {
                Producto producto = new Producto();
                producto.setCodigoSku(rs.getString("codigo_sku"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setStockActual(rs.getInt("stock_actual"));

                lista.add(producto);
            }

        }catch (SQLException e){
            System.err.println("Error al consultar productos con bajo stock: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Funcion para agregar un nuevo producto a la base de datos
     * @param producto produco para agregar
     * @return true si se agrega correctamente, false si ocurre un error
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
     * Funcion para buscar los porductos por el nombre
     * @param nombreBuscado nombre digitado en el campo
     * @return lista de productos buscados
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
     * Consulta entre los productos cuales tienen un stock bajo
     * @return Lista de productos con bajo stock
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

                productosBajoStock.add(producto);
            }

        } catch (SQLException e) {
            System.err.println("Error al consultar productos con bajo stock: " + e.getMessage());
        }

        return productosBajoStock;
    }

    /**
     * Busca la cantidad de productos en la base de datos
     * @return entero con la cantidad de productos
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
     * Funcion para buscar un producto a partir de su id
     * @param id id del producto
     * @return producto encontrado
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
                    p.setPrecioVenta(rs.getDouble("precio_venta"));
                    p.setStockActual(rs.getInt("stock_actual"));
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
     * Funcion para actualizar la cantidad de productos en caso de algun movimiento
     * @param idProducto id del producto a actualizar
     * @param cantidadNueva cantidad nueva despues del movimieto
     * @return true si se realiza el update y false si no
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
     * Funcion para buscar los productos con mismo proveedor
     * @param idProveedor id del proveedor
     * @return lista de productos
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
}