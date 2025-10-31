package com.sistema.util;

import javafx.scene.chart.AreaChart;

import java.time.LocalDate;

/**
 * Clase de modelo que representa una entrada de datos para gráficos de ventas diarias.
 * Encapsula la fecha específica y el total de ventas realizadas en ese día.
 *
 * <p>Campos principales:
 * <ul>
 *   <li>{@link LocalDate} fecha: Día al que corresponden las ventas.</li>
 *   <li>{@code double} totalVentas: Suma de todos los {@code total_venta} de las ventas en esa fecha.</li>
 * </ul>
 * </p>
 *
 * <p>Ideal para alimentar gráficos de línea o área en JavaFX ({@link AreaChart}),
 * especialmente en reportes de evolución temporal. Incluye días sin ventas (valor 0)
 * gracias al uso de {@code LEFT JOIN} con una tabla recursiva de fechas en la consulta SQL.</p>
 */
public class VentaGraficaRow {

    private LocalDate fecha;
    private double totalVentas;

    public VentaGraficaRow(LocalDate fecha, double totalVentas) {
        this.fecha = fecha;
        this.totalVentas = totalVentas;
    }

    public LocalDate getFecha() { return fecha; }
    public double getTotalVentas() { return totalVentas; }
}