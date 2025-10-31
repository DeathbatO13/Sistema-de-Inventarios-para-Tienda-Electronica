package com.sistema.servicios;

import com.sistema.dao.ReportesDAO;

import java.time.LocalDate;

/**
 * Clase de servicio para la generación de contenido de reportes en formato de texto plano.
 * Actúa como capa intermedia entre la interfaz y {@link ReportesDAO}, formateando
 * los resultados en cadenas legibles para exportar en archivos TXT, CSV o mostrar en consola.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Generación de encabezado con tipo de reporte y rango de fechas.</li>
 *   <li>Formato numérico con separador de miles y dos decimales ($,##0.00).</li>
 *   <li>Soporte para múltiples tipos de reporte con estructura tabular.</li>
 *   <li>Manejo de casos no implementados con mensaje claro.</li>
 * </ul>
 * </p>
 *
 * <p>Utiliza {@link StringBuilder} para construcción eficiente y {@code switch} con
 * expresiones para selección clara de lógica por tipo de reporte.</p>
 */
public class ServicioReportes {

    private final ReportesDAO dao = new ReportesDAO();

    /**
     * Genera el contenido completo de un reporte en formato de texto plano según el tipo
     * y periodo especificados. Incluye encabezado, datos formateados y manejo de errores.
     *
     * @param tipo Tipo de reporte (ej. "Total de ventas", "Productos más vendidos").
     * @param desde Fecha de inicio del periodo (inclusive).
     * @param hasta Fecha de fin del periodo (inclusive).
     * @return Cadena de texto con el reporte completo, lista para escritura en archivo o visualización.
     */
    public String obtenerContenidoReporteTXT(String tipo, LocalDate desde, LocalDate hasta) {
        StringBuilder sb = new StringBuilder();
        sb.append("REPORTE: ").append(tipo).append("\n")
                .append("Periodo: ").append(desde).append(" a ").append(hasta).append("\n\n");

        switch (tipo.toLowerCase()) {
            case "total de ventas" -> {
                double total = dao.obtenerTotalVentas(desde, hasta);
                sb.append("TOTAL DE VENTAS: $").append(String.format("%,.2f", total)).append("\n");
            }
            case "total de compras" -> {
                double total = dao.obtenerTotalCompras(desde, hasta);
                sb.append("TOTAL DE COMPRAS: $").append(String.format("%,.2f", total)).append("\n");
            }
            case "total de ganancias" -> {
                double total = dao.obtenerTotalGanancias(desde, hasta);
                sb.append("TOTAL DE GANANCIAS: $").append(String.format("%,.2f", total)).append("\n");
            }
            case "promedio diario de ventas" -> {
                double promedio = dao.obtenerPromedioDiarioVentas(desde, hasta);
                sb.append("PROMEDIO DIARIO DE VENTAS: $").append(String.format("%,.2f", promedio)).append("\n");
            }
            case "ventas por empleado" -> {
                var lista = dao.obtenerVentasPorEmpleado(desde, hasta);
                sb.append("VENTAS POR EMPLEADO:\n\n");
                for (var fila : lista) {
                    sb.append(String.format("%-25s | Ventas: $%,.2f%n", fila.getNombreEmpleado(), fila.getTotalVendido()));
                }
            }
            case "productos más vendidos" -> {
                var lista = dao.obtenerVentasPorProducto(desde, hasta);
                sb.append("PRODUCTOS MÁS VENDIDOS:\n\n");
                for (var fila : lista) {
                    sb.append(String.format("%-30s | Cantidad: %-5d | Total: $%,.2f%n",
                            fila.getNombreProducto(), fila.getCantidadVendida(), fila.getTotalIngresos()));
                }
            }
            case "productos sin movimiento" -> {
                var lista = dao.obtenerProductosSinMovimiento(desde, hasta);
                sb.append("PRODUCTOS SIN MOVIMIENTO:\n\n");
                for (var p : lista) {
                    sb.append(String.format("%-30s | Stock: %-5d | Precio: $%,.2f%n",
                            p.getNombre(), p.getStockActual(), p.getPrecioVenta()));
                }
            }
            default -> sb.append("⚠ Tipo de reporte no reconocido o no implementado aún.\n");
        }

        return sb.toString();
    }

    /**
     * Genera el contenido completo de un reporte en formato csv según el tipo
     * y periodo especificados. Incluye encabezado, datos formateados y manejo de errores.
     *
     * @param tipo Tipo de reporte (ej. "Total de ventas", "Productos más vendidos").
     * @param desde Fecha de inicio del periodo (inclusive).
     * @param hasta Fecha de fin del periodo (inclusive).
     * @return Cadena de texto con el reporte completo, lista para escritura en archivo o visualización.
     */
    public String obtenerContenidoReporteCSV(String tipo, LocalDate desde, LocalDate hasta) {
        StringBuilder sb = new StringBuilder();

        switch (tipo.toLowerCase()) {
            case "total de ventas" -> {
                sb.append("Tipo,Desde,Hasta,TotalVentas\n");
                double total = dao.obtenerTotalVentas(desde, hasta);
                sb.append("Total de Ventas,").append(desde).append(",").append(hasta).append(",")
                        .append(String.format("%.2f", total)).append("\n");
            }
            case "total de compras" -> {
                sb.append("Tipo,Desde,Hasta,TotalCompras\n");
                double total = dao.obtenerTotalCompras(desde, hasta);
                sb.append("Total de Compras,").append(desde).append(",").append(hasta).append(",")
                        .append(String.format("%.2f", total)).append("\n");
            }
            case "ventas por empleado" -> {
                sb.append("Empleado,TotalVentas\n");
                var lista = dao.obtenerVentasPorEmpleado(desde, hasta);
                for (var fila : lista) {
                    sb.append(fila.getNombreEmpleado()).append(",")
                            .append(String.format("%.2f", fila.getTotalVendido())).append("\n");
                }
            }
            case "productos más vendidos" -> {
                sb.append("Producto,Cantidad,Total\n");
                var lista = dao.obtenerVentasPorProducto(desde, hasta);
                for (var fila : lista) {
                    sb.append(fila.getNombreProducto()).append(",")
                            .append(fila.getCantidadVendida()).append(",")
                            .append(String.format("%.2f", fila.getTotalIngresos())).append("\n");
                }
            }
            case "productos sin movimiento" -> {
                sb.append("Producto,Stock,PrecioVenta\n");
                var lista = dao.obtenerProductosSinMovimiento(desde, hasta);
                for (var p : lista) {
                    sb.append(p.getNombre()).append(",")
                            .append(p.getStockActual()).append(",")
                            .append(String.format("%.2f", p.getPrecioVenta())).append("\n");
                }
            }
            default -> sb.append("Tipo de reporte no implementado para CSV.\n");
        }

        return sb.toString();
    }
}
