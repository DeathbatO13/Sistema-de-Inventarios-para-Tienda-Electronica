package com.sistema.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Clase de utilidad para la gestión de conexiones a la base de datos MySQL.
 * Implementa el patrón Singleton básico para reutilizar una única conexión activa
 * durante la ejecución de la aplicación.
 *
 * <p>Funcionalidades principales:
 * <ul>
 *   <li>Establece conexión con MySQL usando JDBC y credenciales fijas.</li>
 *   <li>Reutiliza la conexión si ya está abierta y válida.</li>
 *   <li>Cierra la conexión de forma segura cuando ya no es necesaria.</li>
 *   <li>Registra eventos de conexión y errores en consola.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Advertencia:</strong> Las credenciales están hardcoded.
 * En producción, se recomienda usar variables de entorno, archivos de configuración
 * o un sistema de gestión de secretos.</p>
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
