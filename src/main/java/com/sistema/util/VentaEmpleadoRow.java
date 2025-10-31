package com.sistema.util;

import com.sistema.dao.ReportesDAO;

/**
 * Clase de modelo que representa una fila de reporte de rendimiento por empleado.
 * Agrupa métricas de ventas realizadas por un usuario (empleado) en un periodo específico.
 *
 * <p>Campos principales:
 * <ul>
 *   <li>ID del empleado para referencias internas.</li>
 *   <li>Nombre del empleado para visualización clara.</li>
 *   <li>Cantidad total de ventas registradas.</li>
 *   <li>Total monetario vendido (suma de {@code total_venta}).</li>
 * </ul>
 * </p>
 *
 * <p>Se utiliza en reportes de desempeño como "Ventas por empleado",
 * obtenidos mediante {@code GROUP BY} por usuario en consultas SQL
 * desde {@link ReportesDAO#obtenerVentasPorEmpleado}.</p>
 */
public class VentaEmpleadoRow {
    private int idEmpleado;
    private String nombreEmpleado;
    private int cantidadVentas;
    private double totalVendido;

    public VentaEmpleadoRow(int idEmpleado, String nombreEmpleado, int cantidadVentas, double totalVendido) {
        this.idEmpleado = idEmpleado;
        this.nombreEmpleado = nombreEmpleado;
        this.cantidadVentas = cantidadVentas;
        this.totalVendido = totalVendido;
    }

    public int getIdEmpleado() { return idEmpleado; }
    public String getNombreEmpleado() { return nombreEmpleado; }
    public int getCantidadVentas() { return cantidadVentas; }
    public double getTotalVendido() { return totalVendido; }
}

