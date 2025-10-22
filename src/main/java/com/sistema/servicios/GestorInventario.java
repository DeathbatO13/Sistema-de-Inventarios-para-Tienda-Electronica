package com.sistema.servicios;

import com.sistema.dao.MovimientoDAO;
import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Movimiento;
import com.sistema.modelo.Producto;

import java.time.LocalDateTime;
import java.util.List;

public class GestorInventario {

    /**
     * Registra una entrada de productos en el inventario.
     * @param idProdu El ID del producto.
     * @param cant La cantidad ingresada.
     * @param descripcion La descripción del movimiento.
     * @return true si el movimiento se registra correctamente, false si ocurre un error.
     */
    public boolean registroEntrada(int idProdu, int cant, String descripcion) {
        int stockAct = new ProductoDAO().buscarPorId(idProdu).getStockActual();
        int stockNuevo = stockAct + cant;

        Movimiento mv = new Movimiento();
        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.ENTRADA);
        mv.setCantidad(cant); // debe ser la cantidad ingresada, no el stock total
        mv.setDescripcion(descripcion);
        mv.setFecha(LocalDateTime.now());

        boolean exito = new MovimientoDAO().registrarMovimiento(mv);

        // Actualizar stock del producto
        if (exito) {
            new ProductoDAO().actualizarCantidadProducto(idProdu, stockNuevo);
        }

        return exito;
    }

    /**
     * Registra una salida de productos del inventario.
     * @param idProdu El ID del producto vendido.
     * @param cant La cantidad vendida.
     * @param descripcion La descripción del movimiento.
     * @return true si el movimiento se registra correctamente, false si ocurre un error.
     */
    public boolean registroSalida(int idProdu, int cant, String descripcion){
        ProductoDAO productoDAO = new ProductoDAO();
        int stockAct = productoDAO.buscarPorId(idProdu).getStockActual();
        int stockNuevo = stockAct - cant;

        // Actualiza el stock del producto
        boolean actualizado = productoDAO.actualizarCantidadProducto(idProdu, stockNuevo);
        if (!actualizado) return false;

        // Registrar movimiento
        Movimiento mv = new Movimiento();
        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.VENTA);
        mv.setCantidad(cant);
        mv.setDescripcion(descripcion);

        return new MovimientoDAO().registrarMovimiento(mv);
    }


    /**
     * Ajusta manualmente el stock de un producto en la base de datos y registra el movimiento.
     * @param idProdu El ID del producto.
     * @param nuevoStock El nuevo valor de stock.
     * @param descripcion El motivo del ajuste.
     * @return true si el ajuste y el movimiento se registran correctamente, false si ocurre un error.
     */
    public boolean ajustarStock(int idProdu, int nuevoStock, String descripcion) {
        ProductoDAO productoDAO = new ProductoDAO();
        int stockAct = productoDAO.buscarPorId(idProdu).getStockActual();
        int diferencia = nuevoStock - stockAct;

        // Si no hay diferencia, no hay nada que ajustar
        if (diferencia == 0) return false;

        // Actualiza el stock del producto
        boolean actualizado = productoDAO.actualizarCantidadProducto(idProdu, nuevoStock);
        if (!actualizado) return false;

        // Registrar movimiento
        Movimiento mv = new Movimiento();
        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.AJUSTE);
        mv.setCantidad(Math.abs(diferencia));
        mv.setDescripcion(descripcion + " (" + (diferencia > 0 ? "+" : "-") + Math.abs(diferencia) + ")");

        return new MovimientoDAO().registrarMovimiento(mv);
    }

    /**
     * Obtiene el historial de movimientos de un producto específico.
     * @param idProdu El ID del producto.
     * @return Una lista de movimientos asociados al producto.
     */
    public List<Movimiento> consultarHistorial(int idProdu) {
        return new MovimientoDAO().listaMovimientosPorProducto(idProdu);
    }


    /**
     * Obtiene el stock actual de un producto desde la base de datos.
     * @param idProducto El ID del producto.
     * @return La cantidad actual en stock, o -1 si el producto no se encuentra.
     */
    public int obtenerStockActual(int idProducto) {
        Producto producto = new ProductoDAO().buscarPorId(idProducto);
        return (producto != null) ? producto.getStockActual() : -1;
    }


}
