package com.sistema;

import com.sistema.util.ConexionMySQL;
import java.sql.Connection;

public class TestConexion {
    public static void main(String[] args) {
        try (Connection con = ConexionMySQL.getConexion()) {
            System.out.println("Conexión de prueba exitosa 🚀");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
