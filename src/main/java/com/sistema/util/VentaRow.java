package com.sistema.util;

import java.time.LocalDateTime;

public class VentaRow {
    private String productoVendido;
    private Integer cantidad;
    private Double precioTotal;
    private LocalDateTime fecha;


    public VentaRow(String productoVendido, Integer cantidad, Double precioTotal, LocalDateTime fecha) {
        this.productoVendido = productoVendido;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.fecha = fecha;
    }

    // Getters
    public String getProductoVendido() { return productoVendido; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioTotal() { return precioTotal; }
    public LocalDateTime getFecha() { return fecha; }

    // Setters
    public void setProductoVendido(String productoVendido) { this.productoVendido = productoVendido; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPrecioTotal(Double precioTotal) { this.precioTotal = precioTotal; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
