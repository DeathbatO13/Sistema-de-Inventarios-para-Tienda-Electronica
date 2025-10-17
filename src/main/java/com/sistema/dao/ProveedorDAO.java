package com.sistema.dao;

import com.sistema.modelo.Proveedor;
import com.sistema.util.ConexionMySQL;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProveedorDAO {

    /**
     * Funcion para consultar a la base de datos todos los proveedores registrados
     * @return lista de proveedores registrados
     */
    public List<Proveedor> listaProveedores(){
        List<Proveedor> lista = new ArrayList<>();
        String sql = "SELECT * FROM proveedores";

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()){

            while(rs.next()){
                Proveedor proveedor = new Proveedor();
                proveedor.setId(rs.getInt("id"));
                proveedor.setNombre(rs.getString("nombre"));
                proveedor.setContacto(rs.getString("contacto"));
                proveedor.setTelefono(rs.getString("telefono"));
                proveedor.setEmail(rs.getString("email"));

                lista.add(proveedor);
            }

        }catch (SQLException e){
            System.err.println(e.getMessage());
        }

        return lista;
    }

    public Proveedor buscarPorNombre(String nombreBus){

        String sql = "SELECT * FROM proveedores WHERE nombre = ?";
        Proveedor pro = new Proveedor();

        try(Connection con = ConexionMySQL.getConexion();
            PreparedStatement ps = con.prepareStatement(sql)){

            ps.setString(1, nombreBus);

            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    pro.setId(rs.getInt("id"));
                    pro.setNombre(rs.getString("nombre"));
                    pro.setContacto(rs.getString("contacto"));
                    pro.setTelefono(rs.getString("telefono"));
                    pro.setEmail(rs.getString("email"));
                }
            }

        }catch(SQLException e){
            System.err.println(e.getMessage());
        }
        return pro;
    }

    /**
     * Busca la cantidad de proveedores en la base de datos
     * @return entero con la cantidad de proveedores
     */
    public int cantidadProveedores(){
        String sql = "SELECT COUNT(*) AS total_proveedores FROM proveedores;";
        try (Connection con = ConexionMySQL.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()){

            if(rs.next()){
                return rs.getInt("total_proveedores");
            }
        }catch (SQLException e){
            System.err.println(e.getMessage());
        }
        return 0;
    }

}
