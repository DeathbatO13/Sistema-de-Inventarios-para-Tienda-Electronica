package com.sistema.util;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {

    public static void inicializar() {
        crearBaseDatos();
        crearTablas();
    }

    // Crea la base de datos si no existe
    private static void crearBaseDatos() {
        try (Connection conn = java.sql.DriverManager.getConnection("jdbc:mysql://localhost:3306/", "root", "Deathbat134580");
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS inventariotiendadb");
            System.out.println("✅ Base de datos verificada o creada correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al crear la base de datos: " + e.getMessage());
        }
    }

    // Crea las tablas dentro de la base
    private static void crearTablas() {
        try (Connection conn = ConexionMySQL.getConexion();
             Statement stmt = conn.createStatement()) {

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre_usuario VARCHAR(100) NOT NULL,
                    email VARCHAR(150) NOT NULL UNIQUE,
                    contrasena_hash VARCHAR(255) NOT NULL,
                    token_verificacion VARCHAR(255),
                    verificado TINYINT(1) NOT NULL DEFAULT 0
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS proveedores (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    nombre VARCHAR(150) NOT NULL,
                    contacto VARCHAR(100),
                    telefono VARCHAR(30),
                    email VARCHAR(150)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS productos (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    codigo_sku VARCHAR(50) NOT NULL UNIQUE,
                    nombre VARCHAR(150) NOT NULL,
                    descripcion TEXT,
                    precio_compra DECIMAL(10,2) NOT NULL,
                    precio_venta DECIMAL(10,2) NOT NULL,
                    stock_actual INT NOT NULL,
                    stock_minimo INT NOT NULL DEFAULT 5,
                    id_proveedor INT,
                    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS ventas (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
                    total_venta DECIMAL(10,2) NOT NULL,
                    id_usuario INT,
                    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS detalle_ventas (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    id_venta INT NOT NULL,
                    id_producto INT NOT NULL,
                    cantidad INT NOT NULL,
                    precio_unitario_venta DECIMAL(10,2) NOT NULL,
                    subtotal DECIMAL(10,2) NOT NULL,
                    FOREIGN KEY (id_venta) REFERENCES ventas(id),
                    FOREIGN KEY (id_producto) REFERENCES productos(id)
                )
            """);

            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS movimientos_inventario (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    id_producto INT NOT NULL,
                    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
                    tipo_movimiento ENUM('ENTRADA','VENTA','AJUSTE') NOT NULL,
                    cantidad INT NOT NULL,
                    descripcion VARCHAR(255),
                    FOREIGN KEY (id_producto) REFERENCES productos(id)
                )
            """);

            System.out.println("✅ Tablas verificadas o creadas correctamente.");

        } catch (SQLException e) {
            System.err.println("❌ Error al crear las tablas: " + e.getMessage());
        }
    }
}
