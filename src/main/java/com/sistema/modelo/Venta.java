package com.sistema.modelo;

import java.time.LocalDateTime;

public class Venta {

    private int id;
    private LocalDateTime fecha;
    private double totalVenta;
    private int idUsuario;

    /**
     * Constructor completo
     */
    public Venta(LocalDateTime fecha, double totalVenta, int idUsuario) {
        this.fecha = fecha;
        this.totalVenta = totalVenta;
        this.idUsuario = idUsuario;
    }

    //Getters y Setters

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public double getTotalVenta() {
        return totalVenta;
    }

    public void setTotalVenta(double totalVenta) {
        this.totalVenta = totalVenta;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }
}
