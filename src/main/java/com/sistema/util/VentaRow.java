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
    private int idVenta;
    private int idProducto;
    private String productoVendido;
    private Integer cantidad;
    private Double precioUnitario;
    private Double precioTotal;
    private String vendedor;
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

    public VentaRow(int idVenta, String productoVendido, int cantidad, double precioTotal, String vendedor, LocalDateTime fecha) {
        this.idVenta = idVenta;
        this.productoVendido = productoVendido;
        this.cantidad = cantidad;
        this.precioTotal = precioTotal;
        this.vendedor = vendedor;
        this.fecha = fecha;
    }

    // Getters
    public int getIdVenta() { return idVenta; }
    public int getIdProducto() { return idProducto; }
    public String getProductoVendido() { return productoVendido; }
    public Integer getCantidad() { return cantidad; }
    public Double getPrecioUnitario() { return precioUnitario; }
    public Double getPrecioTotal() { return precioTotal; }
    public String getVendedor() { return vendedor; }
    public LocalDateTime getFecha() { return fecha; }

    // Setters
    public void setIdVenta(int idVenta) { this.idVenta = idVenta; }
    public void setIdProducto(int idProducto) { this.idProducto = idProducto; }
    public void setProductoVendido(String productoVendido) { this.productoVendido = productoVendido; }
    public void setCantidad(Integer cantidad) { this.cantidad = cantidad; }
    public void setPrecioUnitario(Double precioUnitario) { this.precioUnitario = precioUnitario; }
    public void setPrecioTotal(Double precioTotal) { this.precioTotal = precioTotal; }
    public void setVendedor(String vendedor) { this.vendedor = vendedor; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }


}
