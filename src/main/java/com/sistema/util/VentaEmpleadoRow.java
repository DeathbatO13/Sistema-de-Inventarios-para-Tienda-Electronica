package com.sistema.util;

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

