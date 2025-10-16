package com.sistema.modelo;

import java.time.LocalDateTime;

public class Movimiento {

    public enum TipoMovimiento {
        ENTRADA,
        VENTA,
        AJUSTE
    }

    private int id;
    private int idProducto;
    private LocalDateTime fecha;
    private TipoMovimiento tipoMovimiento; // "entrada", "venta" o "ajuste"
    private int cantidad;
    private String descripcion;

    /**
     * Constructor vacio
     */
    public Movimiento(){}

    /**
     *Constructor completo
     */
    public Movimiento(int id, int idProducto, LocalDateTime fecha, TipoMovimiento tipoMovimiento, int cantidad, String descripcion) {
        this.id = id;
        this.idProducto = idProducto;
        this.fecha = fecha;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.descripcion = descripcion;
    }

    //Setters y Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(int idProducto) {
        this.idProducto = idProducto;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
