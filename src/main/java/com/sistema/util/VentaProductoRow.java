package com.sistema.util;

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
