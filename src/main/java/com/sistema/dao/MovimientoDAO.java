package com.sistema.dao;

import com.sistema.modelo.Movimiento;
import com.sistema.util.ConexionMySQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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
            Timestamp timestamp = Timestamp.valueOf(mv.getFecha());
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

    /**
     * Funcion para pedir a la db una lista con todos los movimientos
     * @return lista de moviminetos
     */
    public List<Movimiento> listaMovimientos(){
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos_inventario";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mov = new Movimiento();
                    mov.setIdProducto(rs.getInt("id_producto"));
                    mov.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    mov.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                    mov.setCantidad(rs.getInt("cantidad"));
                    mov.setDescripcion(rs.getString("descricion"));
                    lista.add(mov);
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Consulta a la db por lista de movimientos de un solo producto
     * @param idProd id del producto para buscar movimientos
     * @return lista de movimientos del producto
     */
    public List<Movimiento> listaMovimientosPorProducto(int idProd){
        List<Movimiento> lista = new ArrayList<>();
        String sql = "SELECT * FROM movimientos_inventario WHERE id_producto = ?";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, idProd);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mov = new Movimiento();
                    mov.setIdProducto(rs.getInt("id_producto"));
                    mov.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    mov.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                    mov.setCantidad(rs.getInt("cantidad"));
                    mov.setDescripcion(rs.getString("descricion"));

                    lista.add(mov);
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Funcion para buscar movimientos en un rango de fechas indicado
     * @param desde fecha de inicio
     * @param hasta fecha fin
     * @return lista con mivimientos entre fechas
     */
    public List<Movimiento> listarMovimientosPorRangoFecha(LocalDateTime desde, LocalDateTime hasta) {
        String sql = "SELECT * FROM movimientos_inventario WHERE fecha BETWEEN ? AND ?";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setTimestamp(1, java.sql.Timestamp.valueOf(desde));
            ps.setTimestamp(2, java.sql.Timestamp.valueOf(hasta));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mv = new Movimiento();
                    mv.setId(rs.getInt("id"));
                    mv.setIdProducto(rs.getInt("id_producto"));
                    mv.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    mv.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                    mv.setCantidad(rs.getInt("cantidad"));
                    mv.setDescripcion(rs.getString("descripcion"));

                    lista.add(mv);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Funcion para buscar los ultimos N movimientos registrados.
     * @param limite el numero maximo de movimientos a retornar.
     * @return lista con los N movimientos mas recientes.
     */
    public List<Movimiento> obtenerUltimosMovimientos(int limite) {
        String sql = "SELECT * FROM movimientos_inventario ORDER BY fecha DESC LIMIT ?";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, limite);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mv = new Movimiento();
                    mv.setId(rs.getInt("id"));
                    mv.setIdProducto(rs.getInt("id_producto"));
                    mv.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    mv.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                    mv.setCantidad(rs.getInt("cantidad"));
                    mv.setDescripcion(rs.getString("descripcion"));

                    lista.add(mv);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }

    /**
     * Funcion para contar el numero total de movimientos de inventario registrados.
     * @return La cantidad total de filas en la tabla movimientos_inventario.
     */
    public int contarMovimientos() {
        String sql = "SELECT COUNT(*) FROM movimientos_inventario";
        int totalRegistros = 0;

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                totalRegistros = rs.getInt(1);
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return totalRegistros;
    }

    /**
     * Funcion para buscar movimientos filtrando por el tipo de movimiento.
     * @param tipoMovimiento el tipo de movimiento a buscar ("ENTRADA", "VENTA" o "AJUSTE").
     * @return lista de movimientos que coinciden con el tipo especificado.
     */
    public List<Movimiento> buscarPorTipo(String tipoMovimiento) {
        String sql = "SELECT * FROM movimientos_inventario WHERE tipo_movimiento = ?";
        List<Movimiento> lista = new ArrayList<>();

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, tipoMovimiento);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Movimiento mv = new Movimiento();
                    mv.setId(rs.getInt("id"));
                    mv.setIdProducto(rs.getInt("id_producto"));
                    mv.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                    mv.setTipoMovimiento(Movimiento.TipoMovimiento.valueOf(rs.getString("tipo_movimiento")));
                    mv.setCantidad(rs.getInt("cantidad"));
                    mv.setDescripcion(rs.getString("descripcion"));

                    lista.add(mv);
                }
            }

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return lista;
    }


}
