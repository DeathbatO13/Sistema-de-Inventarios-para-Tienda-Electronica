package com.sistema.dao;

import com.sistema.modelo.DetalleVenta;
import com.sistema.modelo.Usuario;
import com.sistema.modelo.Venta;
import com.sistema.util.ConexionMySQL;
import com.sistema.util.VentaGraficaRow;
import com.sistema.util.VentaRow;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) para la gestión de ventas en la base de datos.
 * Proporciona métodos para registrar ventas completas con sus detalles, consultar
 * el total de ventas del mes actual y obtener una lista formateada de todas las ventas.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Calcular el total de ventas del mes actual usando funciones SQL de agregación.</li>
 *   <li>Listar todas las ventas con detalles (producto, cantidad, subtotal, fecha) mediante JOINs.</li>
 *   <li>Registrar una venta y sus detalles en una transacción atómica con rollback en caso de error.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link ConexionMySQL} para conexiones, sentencias preparadas y transacciones.
 * Los errores se manejan con rollback automático y se registran en consola mediante {@code System.err}.
 * Los recursos (conexiones, statements) se cierran correctamente en el bloque {@code finally}.</p>
 */
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

        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psDetalle = null;

        try {
            con = ConexionMySQL.getConexion();
            con.setAutoCommit(false); // Iniciar transacción

            //  Insertar la venta principal
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

            //  Insertar los detalles
            psDetalle = con.prepareStatement(sqlDetalle);

            for (DetalleVenta d : detalles) {
                // Insertar en detalle_ventas
                psDetalle.setInt(1, idVenta);
                psDetalle.setInt(2, d.getIdProducto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnitarioVenta());
                psDetalle.setDouble(5, d.getSubtotal());
                psDetalle.addBatch();

            }

            // Ejecutar
            psDetalle.executeBatch();

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
                if (con != null) con.setAutoCommit(true);
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Edita una venta existente y reemplaza sus detalles asociados en la base de datos.
     * @param venta El objeto Venta con los datos actualizados.
     * @param detalles La lista de objetos DetalleVenta con los nuevos detalles de los productos vendidos.
     * @return true si la venta se actualiza correctamente, false si ocurre un error.
     */
    public boolean editarVenta(Venta venta, List<DetalleVenta> detalles) {
        String sqlActualizarVenta = "UPDATE ventas SET fecha = ?, total_venta = ?, id_usuario = ? WHERE id = ?";
        String sqlEliminarDetalles = "DELETE FROM detalle_ventas WHERE id_venta = ?";
        String sqlInsertarDetalle = "INSERT INTO detalle_ventas (id_venta, id_producto, cantidad, precio_unitario_venta, subtotal) VALUES (?, ?, ?, ?, ?)";

        Connection con = null;
        PreparedStatement psVenta = null;
        PreparedStatement psEliminar = null;
        PreparedStatement psDetalle = null;

        try {
            con = ConexionMySQL.getConexion();
            con.setAutoCommit(false); // 🔒 Iniciar transacción

            //  Actualizar la venta principal
            psVenta = con.prepareStatement(sqlActualizarVenta);
            psVenta.setTimestamp(1, Timestamp.valueOf(venta.getFecha()));
            psVenta.setDouble(2, venta.getTotalVenta());
            psVenta.setInt(3, venta.getIdUsuario());
            psVenta.setInt(4, venta.getIdUsuario());
            psVenta.executeUpdate();

            //  Eliminar los detalles anteriores
            psEliminar = con.prepareStatement(sqlEliminarDetalles);
            psEliminar.setInt(1, venta.getIdUsuario());
            psEliminar.executeUpdate();

            // Insertar los nuevos detalles
            psDetalle = con.prepareStatement(sqlInsertarDetalle);
            for (DetalleVenta d : detalles) {
                psDetalle.setInt(1, venta.getIdUsuario());
                psDetalle.setInt(2, d.getIdProducto());
                psDetalle.setInt(3, d.getCantidad());
                psDetalle.setDouble(4, d.getPrecioUnitarioVenta());
                psDetalle.setDouble(5, d.getSubtotal());
                psDetalle.addBatch();
            }
            psDetalle.executeBatch();

            //  Confirmar cambios
            con.commit();
            return true;

        } catch (SQLException e) {
            System.err.println("Error al editar la venta: " + e.getMessage());
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
                if (psEliminar != null) psEliminar.close();
                if (psDetalle != null) psDetalle.close();
                if (con != null) con.setAutoCommit(true);
                if (con != null) con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar recursos: " + e.getMessage());
            }
        }
    }

    /**
     * Obtiene una lista de los 20 productos más vendidos en los últimos 3 meses,
     * ordenados por cantidad descendente. Cada entrada incluye el nombre del producto,
     * la cantidad total vendida y el ingreso bruto generado.
     *
     * <p>Utiliza una consulta SQL con agregación ({@code SUM}) y agrupación por producto.
     * La fecha de inicio se calcula dinámicamente con {@code DATE_SUB(CURDATE(), INTERVAL 3 MONTH)}.</p>
     *
     * @return Lista de {@link VentaRow} con los productos más vendidos.
     *         La fecha se establece como {@code null} ya que no es relevante para este reporte.
     */
    public List<VentaRow> productosMasVendidosUltimos3Meses() {
        List<VentaRow> lista = new ArrayList<>();

        String sql = """
        SELECT 
            p.nombre AS Producto_vendido,
            SUM(dv.cantidad) AS Cantidad,
            SUM(dv.subtotal) AS Precio_total
        FROM ventas v
        JOIN detalle_ventas dv ON v.id = dv.id_venta
        JOIN productos p ON dv.id_producto = p.id
        WHERE v.fecha >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH)
        GROUP BY p.nombre
        ORDER BY Cantidad DESC
        LIMIT 20;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                VentaRow ventaRow = new VentaRow(
                        rs.getString("Producto_vendido"),
                        rs.getInt("Cantidad"),
                        rs.getDouble("Precio_total"),
                        null // no necesitamos la fecha en este caso
                );
                lista.add(ventaRow);
            }
        } catch (SQLException e) {
            System.err.println("Error al consultar productos más vendidos: " + e.getMessage());
        }
        return lista;
    }

    /**
     * Obtiene las ventas diarias totales de los últimos 60 días (aproximadamente 2 meses),
     * incluyendo días sin ventas (valor 0). Utiliza una tabla recursiva ({@code WITH RECURSIVE})
     * para generar todas las fechas del rango y un {@code LEFT JOIN} para incluir días sin registro.
     *
     * <p>Los resultados están ordenados cronológicamente y son ideales para gráficos de línea o área.</p>
     *
     * @return Lista de {@link VentaGraficaRow} con fecha y total de ventas por día.
     */
    public List<VentaGraficaRow> obtenerVentasUltimosTresMeses() {
        List<VentaGraficaRow> lista = new ArrayList<>();
        String sql = """
        WITH RECURSIVE dias AS (
                        SELECT CURDATE() - INTERVAL 60 DAY AS fecha
                        UNION ALL
                        SELECT fecha + INTERVAL 1 DAY
                        FROM dias
                        WHERE fecha + INTERVAL 1 DAY <= CURDATE()
                    )
                    SELECT\s
                        d.fecha AS Fecha,
                        IFNULL(SUM(v.total_venta), 0) AS Total_Ventas
                    FROM dias d
                    LEFT JOIN ventas v ON DATE(v.fecha) = d.fecha
                    GROUP BY d.fecha
                    ORDER BY d.fecha;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new VentaGraficaRow(
                        rs.getDate("Fecha").toLocalDate(),
                        rs.getDouble("Total_Ventas")
                ));
            }
        } catch (SQLException e) {
            System.err.println("Error al obtener ventas para gráfica: " + e.getMessage());
        }
        return lista;
    }


}
