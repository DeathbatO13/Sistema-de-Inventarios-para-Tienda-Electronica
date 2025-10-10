package com.sistema.dao;


import com.sistema.modelo.Producto;
import com.sistema.util.ConexionMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO para operaciones CRUD sobre la tabla 'productos'.
 *
 */
public class ProductoDAO {

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
                producto.setId(rs.getInt("id"));
                producto.setCodigoSku(rs.getString("codigo_sku"));
                producto.setNombre(rs.getString("nombre"));
                producto.setDescripcion(rs.getString("descripcion"));
                producto.setPrecioCompra(rs.getDouble("precio_compra"));
                producto.setPrecioVenta(rs.getDouble("precio_venta"));
                producto.setStockActual(rs.getInt("stock_actual"));
                producto.setStockMinimo(rs.getInt("stock_minimo"));
                producto.setIdProveedor(rs.getInt("id_proveedor"));

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

}