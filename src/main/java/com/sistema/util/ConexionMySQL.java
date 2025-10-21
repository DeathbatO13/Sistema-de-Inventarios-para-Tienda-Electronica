package com.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Proporciona métodos para abrir y cerrar conexiones de forma centralizada,
 * reutilizando una conexión existente si está activa.
 *
 */
public class ConexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/inventariotiendadb";
    private static final String USER = "root";
    private static final String PASSWORD = "Deathbat134580";

    private static Connection conexion = null;

    /**
     * Obtiene una conexión activa a la base de datos MySQL.
     * Reutiliza la conexión existente si está abierta, o establece una nueva si está cerrada o no existe.
     *
     * @return Un objeto Connection activo.
     * @throws SQLException Si ocurre un error al intentar conectar con la base de datos.
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("Conexión establecida con MySQL");
            } catch (SQLException e) {
                System.err.println("Error al conectar con MySQL: " + e.getMessage());
                throw e;
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa a la base de datos MySQL si está abierta.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    }
}
