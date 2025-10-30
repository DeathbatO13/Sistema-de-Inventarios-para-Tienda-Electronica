package com.sistema.util;

import java.time.LocalDate;

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