package com.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase encargada de gestionar la conexión a la base de datos MySQL.
 * Permite abrir y cerrar conexiones desde cualquier parte del sistema.
 *
 * @author Daniel
 */
public class ConexionMySQL {

    private static final String URL = "jdbc:mysql://localhost:3306/inventariotiendadb";
    private static final String USER = "root";
    private static final String PASSWORD = "Deathbat134580";

    private static Connection conexion = null;

    /**
     * Obtiene una conexión activa a la base de datos MySQL.
     * Si ya existe una conexión abierta, la reutiliza.
     *
     * @return objeto Connection activo
     * @throws SQLException si ocurre un error de conexión
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                conexion = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Conexión establecida con MySQL");
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar con MySQL: " + e.getMessage());
                throw e;
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión actual si está abierta.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
