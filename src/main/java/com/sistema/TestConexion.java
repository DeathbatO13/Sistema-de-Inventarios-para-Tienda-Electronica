package com.sistema;

import com.sistema.util.ConexionMySQL;
import java.sql.Connection;

/**
 * Clase de prueba para verificar la conexión a la base de datos MySQL.
 * Utiliza la clase ConexionMySQL para establecer y probar una conexión con la base de datos.
 */
public class TestConexion {

    /**
     * Punto de entrada principal para la prueba de conexión.
     * Intenta establecer una conexión con la base de datos MySQL y muestra un mensaje
     * de éxito o imprime el error en caso de fallo.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        try (Connection con = ConexionMySQL.getConexion()) {
            System.out.println("Conexión de prueba exitosa 🚀");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
