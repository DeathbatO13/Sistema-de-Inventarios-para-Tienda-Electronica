package com.sistema.servicios;

import com.sistema.dao.MovimientoDAO;
import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Movimiento;
import com.sistema.modelo.Producto;

import java.util.List;

public class GestorInventario {

    /**
     * Funcion para registrar entrada de productos
     * @param idProdu id del producto
     * @param cant cantidad ingresada
     * @param descripcion descripcion del movimiento
     * @return true si se registro correctamente, false si no
     */
    public boolean registroEntrada(int idProdu, int cant, String descripcion){
        int stockAct = new ProductoDAO().buscarPorId(idProdu).getStockActual();
        int stockNuevo = stockAct + cant;

        Movimiento mv = new Movimiento();

        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.ENTRADA);
        mv.setCantidad(stockNuevo);
        mv.setDescripcion(descripcion);

        return new MovimientoDAO().registrarMovimiento(mv);
    }

    /**
     * Funcion para registrar una salida de productos
     * @param idProdu id del producto vendido
     * @param cant cantidad vendida
     * @param descripcion descripcion del movimiento
     * @return true si se registra correctamente, false si no
     */
    public boolean registroSalida(int idProdu, int cant, String descripcion){
        int stockAct = new ProductoDAO().buscarPorId(idProdu).getStockActual();
        int stockNuevo = stockAct - cant;

        Movimiento mv = new Movimiento();

        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.VENTA);
        mv.setCantidad(stockNuevo);
        mv.setDescripcion(descripcion);

        return new MovimientoDAO().registrarMovimiento(mv);
    }


    /**
     * Función para ajustar manualmente el stock de un producto.
     * @param idProdu id del producto
     * @param nuevoStock nuevo valor de stock
     * @param descripcion motivo del ajuste
     * @return true si el ajuste se registró correctamente, false si no
     */
    public boolean ajustarStock(int idProdu, int nuevoStock, String descripcion) {
        int stockAct = new ProductoDAO().buscarPorId(idProdu).getStockActual();
        int diferencia = nuevoStock - stockAct;

        Movimiento mv = new Movimiento();
        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.AJUSTE);
        mv.setCantidad(diferencia);
        mv.setDescripcion(descripcion);

        // Actualiza el stock del producto
        boolean actualizado = new ProductoDAO().actualizarCantidadProducto(idProdu, nuevoStock);
        if (!actualizado) return false;

        return new MovimientoDAO().registrarMovimiento(mv);
    }

    /**
     * Obtiene el historial de movimientos de un producto.
     * @param idProdu id del producto
     * @return lista de movimientos asociados al producto
     */
    public List<Movimiento> consultarHistorial(int idProdu) {
        return new MovimientoDAO().listaMovimientosPorProducto(idProdu);
    }


    /**
     * Obtiene el stock actual de un producto desde la base de datos.
     * @param idProducto id del producto
     * @return cantidad actual en stock, o -1 si no se encuentra
     */
    public int obtenerStockActual(int idProducto) {
        Producto producto = new ProductoDAO().buscarPorId(idProducto);
        return (producto != null) ? producto.getStockActual() : -1;
    }


}
