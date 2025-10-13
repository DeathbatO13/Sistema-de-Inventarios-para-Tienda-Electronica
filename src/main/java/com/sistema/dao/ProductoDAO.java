package com.sistema.dao;


import com.sistema.modelo.Producto;
import com.sistema.util.ConexionMySQL;

import javax.crypto.CipherInputStream;
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
     * Funcion para buscar los porductos por el nombre
     * @param nombreBuscado nombre digitado en el campo
     * @return lista de productos buscados
     */
    public List<Producto> buscarPorNombre(String nombreBuscado) {
        List<Producto> productos = new ArrayList<>();
        String sql = "SELECT * FROM productos WHERE nombre LIKE ?";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            //Agregar los comodines para buscar coincidencias parciales
            ps.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Producto p = new Producto();
                    p.setId(rs.getInt("id"));
                    p.setCodigoSku(rs.getString("codigo_sku"));
                    p.setNombre(rs.getString("nombre"));
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

}