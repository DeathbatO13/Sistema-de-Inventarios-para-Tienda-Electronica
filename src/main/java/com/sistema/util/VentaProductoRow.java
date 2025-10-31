package com.sistema.util;

/**
 * Clase de modelo que representa una fila de reporte de ventas agregadas por producto.
 * Contiene información consolidada sobre las ventas de un producto específico
 * en un rango de fechas determinado.
 *
 * <p>Campos principales:
 * <ul>
 *   <li>ID del producto para referencias internas.</li>
 *   <li>Nombre del producto para visualización.</li>
 *   <li>Cantidad total vendida en el periodo.</li>
 *   <li>Ingresos totales generados (suma de subtotales).</li>
 * </ul>
 * </p>
 *
 * <p>Se utiliza en reportes como "Ventas por producto" y "Productos más vendidos",
 * obtenidos mediante consultas SQL con {@code GROUP BY} en {@link ReportesDAO}.</p>
 */
public class VentaProductoRow {

    private int idProducto;
    private String nombreProducto;
    private int cantidadVendida;
    private double totalIngresos;

    public VentaProductoRow(int idProducto, String nombreProducto, int cantidadVendida, double totalIngresos) {
        this.idProducto = idProducto;
        this.nombreProducto = nombreProducto;
        this.cantidadVendida = cantidadVendida;
        this.totalIngresos = totalIngresos;
    }

    public int getIdProducto() { return idProducto; }
    public String getNombreProducto() { return nombreProducto; }
    public int getCantidadVendida() { return cantidadVendida; }
    public double getTotalIngresos() { return totalIngresos; }
}
