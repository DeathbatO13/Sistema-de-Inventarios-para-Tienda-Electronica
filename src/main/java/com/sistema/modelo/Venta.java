package com.sistema.modelo;

import java.time.LocalDateTime;

public class Venta {

    private int id;
    private LocalDateTime fecha;
    private double totalVenta;
    private int idUsuario;

    /**
     * Constructor vacio
     */
    public Venta(){}

    /**
     * Constructor completo
     */
    public Venta(int id, LocalDateTime fecha, double totalVenta, int idUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.totalVenta = totalVenta;
        this.idUsuario = idUsuario;
    }

    //Getters y Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
