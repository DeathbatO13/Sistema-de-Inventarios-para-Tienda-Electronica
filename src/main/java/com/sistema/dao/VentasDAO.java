package com.sistema.dao;

import com.sistema.modelo.DetalleVenta;
import com.sistema.modelo.Venta;
import com.sistema.util.ConexionMySQL;
import com.sistema.util.VentaRow;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class VentasDAO {

    /**
     * Registra una nueva venta y sus detalles asociados en la base de datos.
     * @param v El objeto Venta con los datos de la venta.
     * @param det La lista de objetos DetalleVenta con los detalles de los productos vendidos.
     * @return true si la venta se registra correctamente, false si ocurre un error.
     */
    public boolean registrarVenta(Venta v, List<DetalleVenta> det){
        return false;
    }

    /**
     * Calcula el total de ventas realizadas en el mes actual.
     * @return Un valor double que representa el total de ventas del mes actual, o 0 si no hay ventas o ocurre un error.
     */
    public double totalVentasMes() {
        String sql = "SELECT COALESCE(SUM(total_venta), 0) AS total_ventas_mes " +
                "FROM ventas " +
                "WHERE YEAR(fecha) = YEAR(CURRENT_DATE()) " +
                "AND MONTH(fecha) = MONTH(CURRENT_DATE())";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                return rs.getDouble("total_ventas_mes");
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar total de ventas: " + e.getMessage());
        }
        return 0;
    }

    /**
     * Consulta y devuelve una lista de todas las ventas registradas en la base de datos.
     * @return Una lista de objetos Venta.
     */
    public List<VentaRow> listaVentas() {
        List<VentaRow> lista = new ArrayList<>();
        String sql = "SELECT " +
                "    p.nombre AS Producto_vendido, " +
                "    dv.cantidad AS Cantidad, " +
                "    dv.subtotal AS Precio_total, " +
                "    v.fecha AS Fecha " +
                "FROM ventas v " +
                "JOIN detalle_ventas dv ON v.id = dv.id_venta " +
                "JOIN productos p ON dv.id_producto = p.id " +
                "ORDER BY v.fecha DESC;";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VentaRow ventaRow = new VentaRow(
                        rs.getString("Producto_vendido"),
                        rs.getInt("Cantidad"),
                        rs.getDouble("Precio_total"),
                        rs.getTimestamp("Fecha").toLocalDateTime()
                );
                lista.add(ventaRow);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar la lista de ventas: " + e.getMessage());
        }
        return lista;
    }



}
