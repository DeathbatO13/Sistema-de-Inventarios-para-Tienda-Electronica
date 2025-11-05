package com.sistema.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Clase de utilidad para la gestión de conexiones a la base de datos MySQL.
 * Carga los datos de conexión desde un archivo externo de configuración
 * ubicado en src/main/resources/config/db.properties.
 *
 * Mejora la seguridad al evitar credenciales embebidas en el código fuente.
 */
public class ConexionMySQL {

    private static Connection conexion = null;
    private static final String CONFIG_PATH = "/config/db.properties";

    /**
     * Obtiene una conexión activa a la base de datos MySQL utilizando los datos del archivo de configuración.
     *
     * @return Objeto Connection activo y reutilizable.
     * @throws SQLException si ocurre un error de conexión.
     */
    public static Connection getConexion() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try (InputStream input = ConexionMySQL.class.getResourceAsStream(CONFIG_PATH)) {

                if (input == null) {
                    throw new IOException("No se encontró el archivo de configuración: " + CONFIG_PATH);
                }

                Properties prop = new Properties();
                prop.load(input);

                String url = prop.getProperty("db.url");
                String user = prop.getProperty("db.user");
                String password = prop.getProperty("db.password");

                conexion = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Conexión establecida con MySQL");

            } catch (IOException e) {
                System.err.println("Error al cargar configuración de la base de datos: " + e.getMessage());
                throw new SQLException("No se pudo cargar la configuración de conexión.", e);
            } catch (SQLException e) {
                System.err.println("❌ Error al conectar con MySQL: " + e.getMessage());
                throw e;
            }
        }
        return conexion;
    }

    /**
     * Cierra la conexión activa si está abierta.
     */
    public static void cerrarConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("🔒 Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
