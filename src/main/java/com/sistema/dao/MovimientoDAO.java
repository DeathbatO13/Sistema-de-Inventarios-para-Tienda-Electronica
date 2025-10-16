package com.sistema.dao;

import com.sistema.modelo.Movimiento;
import com.sistema.util.ConexionMySQL;

import java.sql.*;

public class MovimientoDAO {

    /**
     * Funcion para registrar un mvimiento de preductos en el inventario
     * @param mv Movimiento registrado
     * @return true si registra correctamente, false si no
     */
    public boolean registrarMovimiento(Movimiento mv){
        String sql = "INSERT INTO movimientos_inventario (id_producto, fecha, tipo_movimiento, " +
                "cantidad, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, mv.getIdProducto());
            java.sql.Timestamp timestamp = java.sql.Timestamp.valueOf(mv.getFecha());
            ps.setTimestamp(2, timestamp);
            ps.setString(3, mv.getTipoMovimiento().toString()); // Asumiendo que ahora usas un Enum
            ps.setInt(4, mv.getCantidad());
            ps.setString(5, mv.getDescripcion());

            int filas = ps.executeUpdate();

            if (filas > 0) {
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) mv.setId(rs.getInt(1));
                }
                return true;
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return false;
    }

}
