package com.sistema.dao;

import com.sistema.modelo.Movimiento;
import com.sistema.util.ConexionMySQL;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimientoDAO {

    /**
     * Registra un nuevo movimiento de inventario en la base de datos.
     * @param mv El movimiento a registrar.
     * @return true si el movimiento se registra correctamente, false si ocurre un error.
     */
    public boolean registrarMovimiento(Movimiento mv){
        String sql = "INSERT INTO movimientos_inventario (id_producto, fecha, tipo_movimiento, " +
                "cantidad, descripcion) VALUES (?, ?, ?, ?, ?)";

        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){

            ps.setInt(1, mv.getIdProducto());
            LocalDateTime fecha = mv.getFecha() != null ? mv.getFecha() : LocalDateTime.now();
            ps.setTimestamp(2, Timestamp.valueOf(fecha));
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
     * Obtiene una lista con todos los movimientos de inventario registrados en la base de datos.
     * @return Una lista de movimientos.
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
     * Obtiene una lista de movimientos de inventario asociados a un producto específico.
     * @param idProd El ID del producto para buscar movimientos.
     * @return Una lista de movimientos correspondientes al producto.
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
     * Busca movimientos de inventario registrados en un rango de fechas específico.
     * @param desde La fecha de inicio del rango.
     * @param hasta La fecha de fin del rango.
     * @return Una lista de movimientos dentro del rango de fechas especificado.
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
     * Obtiene los últimos movimientos de inventario registrados, hasta un límite especificado.
     * @param limite El número máximo de movimientos a retornar.
     * @return Una lista con los movimientos más recientes, ordenados por fecha descendente.
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
     * Cuenta el número total de movimientos de inventario registrados en la base de datos.
     * @return La cantidad total de movimientos.
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
     * Busca movimientos de inventario filtrados por tipo de movimiento.
     * @param tipoMovimiento El tipo de movimiento a buscar (por ejemplo, "ENTRADA", "VENTA", "AJUSTE").
     * @return Una lista de movimientos que coinciden con el tipo especificado.
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
