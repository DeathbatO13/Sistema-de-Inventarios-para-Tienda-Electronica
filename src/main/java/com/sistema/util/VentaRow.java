package com.sistema.util;

import com.sistema.dao.VentasDAO;
import javafx.scene.control.TableView;

import java.time.LocalDateTime;

/**
 * Clase de modelo que representa una fila de datos de venta para su visualización
 * en una {@link TableView} de JavaFX.
 *
 * <p>Encapsula información consolidada de una venta individual:
 * <ul>
 *   <li>Nombre del producto vendido.</li>
 *   <li>Cantidad de unidades vendidas.</li>
 *   <li>Precio total (subtotal) de la línea de venta.</li>
 *   <li>Fecha y hora exacta de la venta.</li>
 * </ul>
 * </p>
 *
 * <p>Se utiliza en conjunto con {@link VentasDAO#listaVentas()} para mostrar
 * el historial de ventas de forma clara y estructurada en la interfaz gráfica.</p>
 */
public class VentaRow {
    private String productoVendido;
    private Integer cantidad;
    private Double precioTotal;
    private LocalDateTime fecha;

    /**
     * Representa una fila de datos de ventas para su visualización en una tabla.
     * Esta clase encapsula información sobre un producto vendido, incluyendo su nombre,
     * cantidad, precio total y fecha de venta.
     */
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
