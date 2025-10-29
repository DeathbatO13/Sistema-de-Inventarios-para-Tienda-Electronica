package com.sistema.dao;

import com.sistema.modelo.DetalleVenta;
import com.sistema.modelo.Usuario;
import com.sistema.modelo.Venta;
import com.sistema.util.ConexionMySQL;
import com.sistema.util.VentaRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VentasDAO {

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

    /**
     * Registra una nueva venta y sus detalles asociados en la base de datos.
     * @param venta El objeto Venta con los datos de la venta.
     * @param detalles La lista de objetos DetalleVenta con los detalles de los productos vendidos.
     * @return true si la venta se registra correctamente, false si ocurre un error.
     */
    public boolean registrarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlVenta = "INSERT INTO ventas (fecha, total_venta, id_usuario) VALUES (?, ?, ?)";
        String sqlDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario_venta, subtotal) VALUES (?, ?, ?, ?, ?)";
        String sqlUpdateStock = "UPDATE productos SET stock_actual = stock_actual - ? WHERE id = ?";
        String sqlMovimiento = "INSERT INTO movimientos_inventario (id_producto, tipo_movimiento, cantidad, descripcion) VALUES (?, 'VENTA', ?, ?)";

        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;
        PreparedStatement psStock = null;
        PreparedStatement psMovimiento = null;

        try {
            con = ConexionMySQL.getConexion();
            con.setAutoCommit(false); // 🔒 Iniciar transacción

            // 1️⃣ Insertar la venta principal
            psVenta = con.prepareStatement(sqlVenta, Statement.RETURN_GENERATED_KEYS);
            psVenta.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            psVenta.setDouble(2, venta.getTotalVenta());
            psVenta.setInt(3, venta.getIdUsuario());
            psVenta.executeUpdate();

            // Obtener el ID generado de la venta
            ResultSet rs = psVenta.getGeneratedKeys();
            int idVenta = 0;
            if (rs.next()) {
                idVenta = rs.getInt(1);
            }

            // 2️⃣ Insertar los detalles
            psDetalle = con.prepareStatement(sqlDetalle);
            psStock = con.prepareStatement(sqlUpdateStock);
            psMovimiento = con.prepareStatement(sqlMovimiento);

            for (DetalleVenta d : detalles) {
                // Insertar en detalle_ventas
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, d.getIdProducto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnitarioVenta());
                psDetalle.setDouble(5, d.getSubtotal());
                psDetalle.addBatch();

                // Actualizar stock
                psStock.setInt(1, d.getCantidad());
                psStock.setInt(2, d.getIdProducto());
                psStock.addBatch();

                // Registrar movimiento de inventario
                psMovimiento.setInt(1, d.getIdProducto());
                psMovimiento.setInt(2, d.getCantidad());
                psMovimiento.setString(3, "Venta registrada (ID Venta: " + idVenta + ")");
                psMovimiento.addBatch();
            }

            // Ejecutar
            psDetalle.executeBatch();
            psStock.executeBatch();
            psMovimiento.executeBatch();

            con.commit();

            return true;

        } catch (SQLException e) {
            System.err.println("Error al registrar la venta: " + e.getMessage());
            if (con != null) {
                try {
                    con.rollback();
                } catch (SQLException ex) {
                    System.err.println("Error al hacer rollback: " + ex.getMessage());
                }
            }
            return false;
        } finally {
            try {
                if (psVenta != null) psVenta.close();
                if (psDetalle != null) psDetalle.close();
                if (psStock != null) psStock.close();
                if (psMovimiento != null) psMovimiento.close();
                if (con != null) con.setAutoCommit(true);
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }



}
