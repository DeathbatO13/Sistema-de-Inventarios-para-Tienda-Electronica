package com.sistema.dao;

import com.sistema.modelo.Producto;
import com.sistema.util.ConexionMySQL;
import com.sistema.util.VentaEmpleadoRow;
import com.sistema.util.VentaProductoRow;

import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase de acceso a datos (DAO) especializada en consultas analíticas y reportes
 * del sistema de inventario. Proporciona métricas financieras, estadísticas de ventas
 * y análisis de rendimiento para diferentes periodos de tiempo.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Cálculo de totales de ventas, compras y ganancias en rangos de fechas específicos.</li>
 *   <li>Promedio diario de ventas calculado dinámicamente según el periodo.</li>
 *   <li>Desglose de ventas por producto y por empleado con agregación de cantidades y montos.</li>
 *   <li>Identificación de productos sin movimiento en el periodo especificado.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza consultas SQL optimizadas con JOINs, agregaciones (SUM, COUNT, GROUP BY)
 * y subconsultas. Todas las operaciones están parametrizadas para seguridad y manejan
 * rangos de fechas mediante {@link LocalDate} convertidos a {@link java.sql.Date}.</p>
 */
public class ReportesDAO {

    /**
     * Calcula el total de ventas (suma de subtotales de detalle_ventas) en el rango de fechas especificado.
     * Incluye todas las líneas de venta ejecutadas entre las fechas de inicio y fin.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return El total de ventas en el periodo, o 0.0 si no hay ventas o ocurre un error.
     */
    public double obtenerTotalVentas(LocalDate inicio, LocalDate fin) {
        double total = 0.0;

        String sql = """
        SELECT SUM(dv.subtotal) AS total
        FROM ventas v
        JOIN detalle_ventas dv ON v.id = dv.id_venta
        WHERE DATE(v.fecha) BETWEEN ? AND ?;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener total de ventas: " + e.getMessage());
        }

        return total;
    }

    /**
     * Calcula el costo total de compras (precio_compra × cantidad) para movimientos de tipo 'ENTRADA'
     * en el rango de fechas especificado. Representa el valor de inventario adquirido.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return El total de compras en el periodo, o 0.0 si no hay entradas o ocurre un error.
     */
    public double obtenerTotalCompras(LocalDate inicio, LocalDate fin) {
        double total = 0.0;

        String sql = """
        SELECT SUM(p.precio_compra * mi.cantidad) AS total_compras
        FROM movimientos_inventario mi
        JOIN productos p ON mi.id_producto = p.id
        WHERE mi.tipo_movimiento = 'ENTRADA'
          AND DATE(mi.fecha) BETWEEN ? AND ?;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    total = rs.getDouble("total_compras");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener total de compras: " + e.getMessage());
        }

        return total;
    }


    /**
     * Calcula las ganancias brutas totales (margen de ganancia) en el rango de fechas especificado.
     * La ganancia por línea es (precio_unitario_venta - precio_compra) × cantidad.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return El total de ganancias brutas, o 0.0 si no hay ventas o ocurre un error.
     */
    public double obtenerTotalGanancias(LocalDate inicio, LocalDate fin) {
        double totalGanancias = 0.0;

        String sql = """
        SELECT 
            SUM((dv.precio_unitario_venta - p.precio_compra) * dv.cantidad) AS total_ganancias
        FROM ventas v
        JOIN detalle_ventas dv ON v.id = dv.id_venta
        JOIN productos p ON dv.id_producto = p.id
        WHERE DATE(v.fecha) BETWEEN ? AND ?;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalGanancias = rs.getDouble("total_ganancias");
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener total de ganancias: " + e.getMessage());
        }

        return totalGanancias;
    }

    /**
     * Calcula el promedio diario de ventas totales en el periodo especificado.
     * Divide el total de ventas entre el número de días en el rango (inclusive ambos extremos).
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return El promedio diario de ventas, o 0.0 si no hay datos o el periodo es inválido.
     */
    public double obtenerPromedioDiarioVentas(LocalDate inicio, LocalDate fin) {
        double promedioDiario = 0.0;

        String sql = """
        SELECT 
            SUM(v.total_venta) AS total_ventas
        FROM ventas v
        WHERE DATE(v.fecha) BETWEEN ? AND ?;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    double totalVentas = rs.getDouble("total_ventas");
                    long diasPeriodo = ChronoUnit.DAYS.between(inicio, fin) + 1; // incluir ambos extremos
                    promedioDiario = diasPeriodo > 0 ? totalVentas / diasPeriodo : 0.0;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener promedio diario de ventas: " + e.getMessage());
        }

        return promedioDiario;
    }

    /**
     * Obtiene un desglose de ventas por producto en el periodo especificado.
     * Incluye ID, nombre, cantidad total vendida y ingresos generados por cada producto.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return Lista de {@link VentaProductoRow} ordenada por cantidad vendida descendente.
     */
    public List<VentaProductoRow> obtenerVentasPorProducto(LocalDate inicio, LocalDate fin) {
        List<VentaProductoRow> lista = new ArrayList<>();

        String sql = """
        SELECT 
            p.id AS id_producto,
            p.nombre AS producto,
            SUM(dv.cantidad) AS total_vendido,
            SUM(dv.subtotal) AS total_ingresos
        FROM detalle_ventas dv
        INNER JOIN productos p ON dv.id_producto = p.id
        INNER JOIN ventas v ON dv.id_venta = v.id
        WHERE DATE(v.fecha) BETWEEN ? AND ?
        GROUP BY p.id, p.nombre
        ORDER BY total_vendido DESC;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VentaProductoRow vp = new VentaProductoRow(
                            rs.getInt("id_producto"),
                            rs.getString("producto"),
                            rs.getInt("total_vendido"),
                            rs.getDouble("total_ingresos")
                    );
                    lista.add(vp);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por producto: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Obtiene un desglose de ventas por empleado en el periodo especificado.
     * Incluye ID, nombre del empleado, cantidad de ventas realizadas y total vendido.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return Lista de {@link VentaEmpleadoRow} ordenada por total vendido descendente.
     */
    public List<VentaEmpleadoRow> obtenerVentasPorEmpleado(LocalDate inicio, LocalDate fin) {
        List<VentaEmpleadoRow> lista = new ArrayList<>();

        String sql = """
        SELECT 
            u.id AS id_empleado,
            u.nombre_usuario AS empleado,
            COUNT(v.id) AS cantidad_ventas,
            SUM(v.total_venta) AS total_vendido
        FROM ventas v
        INNER JOIN usuarios u ON v.id_usuario = u.id
        WHERE DATE(v.fecha) BETWEEN ? AND ?
        GROUP BY u.id, u.nombre_usuario
        ORDER BY total_vendido DESC;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    VentaEmpleadoRow ve = new VentaEmpleadoRow(
                            rs.getInt("id_empleado"),
                            rs.getString("empleado"),
                            rs.getInt("cantidad_ventas"),
                            rs.getDouble("total_vendido")
                    );
                    lista.add(ve);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener ventas por empleado: " + e.getMessage());
        }

        return lista;
    }

    /**
     * Identifica los productos que no tuvieron movimientos (entradas o salidas) en el periodo especificado.
     * Utiliza una subconsulta NOT IN para excluir productos con actividad registrada.
     *
     * @param inicio Fecha de inicio del periodo (inclusive).
     * @param fin Fecha de fin del periodo (inclusive).
     * @return Lista de {@link Producto} sin movimientos, ordenados alfabéticamente por nombre.
     */
    public List<Producto> obtenerProductosSinMovimiento(LocalDate inicio, LocalDate fin) {
        List<Producto> lista = new ArrayList<>();

        String sql = """
        SELECT 
            p.id,
            p.codigo_sku,
            p.nombre,
            p.descripcion,
            p.precio_compra,
            p.precio_venta,
            p.stock_actual,
            p.stock_minimo,
            p.id_proveedor
        FROM productos p
        WHERE p.id NOT IN (
            SELECT DISTINCT m.id_producto
            FROM movimientos_inventario m
            WHERE DATE(m.fecha) BETWEEN ? AND ?
        )
        ORDER BY p.nombre ASC;
    """;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setDate(1, Date.valueOf(inicio));
            ps.setDate(2, Date.valueOf(fin));

            try (ResultSet rs = ps.executeQuery()) {
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

                    lista.add(producto);
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener productos sin movimiento: " + e.getMessage());
        }

        return lista;
    }

}
