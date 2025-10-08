-- Crear base de datos
CREATE DATABASE IF NOT EXISTS inventariotiendadb;
USE inventariotiendadb;

-- Tabla de usuarios
CREATE TABLE usuarios (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre_usuario VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    contrasena_hash VARCHAR(255) NOT NULL,
    token_verificacion VARCHAR(255),
    verificado TINYINT(1) NOT NULL DEFAULT 0  -- 0 = no verificado, 1 = verificado
);

-- Tabla de proveedores
CREATE TABLE proveedores (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(150) NOT NULL,
    contacto VARCHAR(100),
    telefono VARCHAR(30),
    email VARCHAR(150)
);

-- Tabla de productos
CREATE TABLE productos (
    id INT AUTO_INCREMENT PRIMARY KEY,
    codigo_sku VARCHAR(50) NOT NULL UNIQUE,  -- SKU único por producto
    nombre VARCHAR(150) NOT NULL,
    descripcion TEXT,
    precio_compra DECIMAL(10,2) NOT NULL,
    precio_venta DECIMAL(10,2) NOT NULL,
    stock_actual INT NOT NULL,
    stock_minimo INT NOT NULL DEFAULT 5,  -- Nivel de alerta de stock
    id_proveedor INT,
    FOREIGN KEY (id_proveedor) REFERENCES proveedores(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- Tabla de ventas
CREATE TABLE ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    total_venta DECIMAL(10,2) NOT NULL,
    id_usuario INT,
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id)
        ON UPDATE CASCADE
        ON DELETE SET NULL
);

-- Detalle de cada venta
CREATE TABLE detalle_ventas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_venta INT NOT NULL,
    id_producto INT NOT NULL,
    cantidad INT NOT NULL,
    precio_unitario_venta DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (id_venta) REFERENCES ventas(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE,
    FOREIGN KEY (id_producto) REFERENCES productos(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

-- Movimientos de inventario (entradas, ventas, ajustes, etc.)
CREATE TABLE movimientos_inventario (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    fecha DATETIME DEFAULT CURRENT_TIMESTAMP,
    tipo_movimiento ENUM('ENTRADA','VENTA','AJUSTE') NOT NULL,
    cantidad INT NOT NULL,
    descripcion VARCHAR(255),
    FOREIGN KEY (id_producto) REFERENCES productos(id)
        ON UPDATE CASCADE
        ON DELETE CASCADE
);
