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
}
