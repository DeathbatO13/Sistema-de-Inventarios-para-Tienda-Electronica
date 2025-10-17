package com.sistema.servicios;

import com.sistema.dao.MovimientoDAO;
import com.sistema.dao.ProductoDAO;
import com.sistema.modelo.Movimiento;

public class GestorInventario {

    public boolean registroEntrada(int idProdu, int cant, String descripcion){
        int stockAct = new ProductoDAO().buscarPorId(idProdu).getStockActual();
        int stockNuevo = stockAct + cant;

        Movimiento mv = new Movimiento();

        mv.setIdProducto(idProdu);
        mv.setTipoMovimiento(Movimiento.TipoMovimiento.ENTRADA);
        mv.setCantidad(cant);
        mv.setDescripcion(descripcion);

        return new MovimientoDAO().registrarMovimiento(mv);
    }
}
