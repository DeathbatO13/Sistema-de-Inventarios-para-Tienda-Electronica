package com.sistema.dao;

import com.sistema.modelo.DetalleVenta;
import com.sistema.modelo.Venta;
import com.sistema.util.ConexionMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
}
