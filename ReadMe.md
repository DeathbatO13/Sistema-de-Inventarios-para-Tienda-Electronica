# ⚙️ ElectroStock — Sistema de Gestión de Inventario Electrónico

**ElectroStock** es una aplicación de escritorio desarrollada en **JavaFX** con base de datos **MySQL**, diseñada para la **gestión integral de inventarios, ventas y proveedores** en tiendas del sector electrónico.  
Su objetivo es optimizar el control de existencias, agilizar los procesos de venta y mejorar la trazabilidad de los movimientos de productos.

---

## 🚀 Características Técnicas

| Módulo | Funcionalidad Principal |
|--------|--------------------------|
| **Autenticación** | Registro con verificación por correo (código de 6 dígitos), cifrado con **bcrypt** y bloqueo automático tras 5 intentos fallidos. |
| **Productos** | CRUD completo, control de stock, precio de compra/venta, cantidad mínima configurable y proveedor asociado. |
| **Proveedores** | Gestión de contactos, vinculación directa con productos, edición y eliminación segura. |
| **Ventas** | Registro multi-ítem, cálculo automático del total y actualización en tiempo real del inventario. |
| **Reportes** | Gráficos de tendencia, ranking de productos más vendidos y exportación en formatos **TXT** o **CSV**. |
| **Movimientos** | Auditoría completa mediante la tabla `movimientos_inventario` (INGRESO_INICIAL, SALIDA_VENTA, AJUSTE, etc.). |

---

## 🖥️ Requisitos del Sistema

```text
- Java Runtime Environment (JRE) 21 o superior
- MySQL Server 8.0 o superior
- Windows 10 / 11 (64 bits)
- 4 GB de memoria RAM mínima
- 200 MB de espacio libre en disco
```

## ⚡ Instalación Rápida

1. **Instalar MySQL** y crear un usuario con privilegios de lectura y escritura.
2. **Ejecutar** el archivo `ElectroStock.jar` o `ElectroStock.exe`.
3. En el primer inicio, el sistema **crea automáticamente** la base de datos `inventariotiendadb` si no existe.

**Conexión por defecto:**

    jdbc:mysql://localhost:3306/inventariotiendadb  
    Usuario: root  
    Contraseña: (configurada localmente)



---

## 🧭 Estructura de la Base de Datos (Tablas Principales)

| Tabla | Descripción |
|--------|--------------|
| `usuarios` | (id, nombre, correo, password_hash, verificado, intentos_fallidos) |
| `productos` | (id, codigo, nombre, descripcion, precio_compra, precio_venta, stock, cantidad_minima, proveedor_id) |
| `proveedores` | (id, nombre_empresa, contacto, telefono, correo, direccion) |
| `ventas` | (id, usuario_id, fecha, total) |
| `detalle_venta` | (venta_id, producto_id, cantidad, precio_unitario) |
| `movimientos_inventario` | (id, producto_id, tipo, cantidad, fecha, referencia) |

---

## 🔒 Seguridad y Control

- Contraseñas cifradas con **bcrypt**.
- **Verificación por correo electrónico** obligatoria antes del inicio de sesión.
- **Bloqueo temporal** tras múltiples intentos fallidos.
- **Auditoría completa** de movimientos de inventario.

---

## 📊 Capturas de Pantalla

### Inicio de Sesión con Verificación
<img src="./images/Login.png" alt="Login" width="550">

### Reportes Analíticos con Exportación
<img src="./images/reportes.png" alt="Reportes" width="550">

---

## 🧾 Soporte y Documentación

- Manual de Usuario: [`MANUAL.md`](./MANUAL.md)
- Registros de ejecución: `logs/electrostock.log`
- Contacto: 📧 soporte.electrostock@gmail.com

---

## 🛠️ Tecnologías Utilizadas

| Categoría | Tecnología |
|------------|-------------|
| **Lenguaje** | Java |
| **Framework** | JavaFX |
| **Base de Datos** | MySQL |
| **Conexión** | JDBC (Data Access Object Pattern) |
| **Diseño de Interfaz** | FXML + CSS personalizado |
| **Librerías Adicionales** | ControlsFX, FontAwesomeFX |

---

## 📦 Información de Versión

> **Versión:** 1.0.0  
> **Lanzamiento:** Noviembre 2025  
> **Desarrollado por:** Daniel Alejandro Torres Abella

---

## 🔮 Posibles Mejoras y Extensiones Futuras

El sistema está diseñado para ser **modular y escalable**, facilitando futuras ampliaciones.  
A continuación se listan las mejoras propuestas para próximas versiones:

| Área | Mejora Propuesta |
|------|------------------|
| **Interfaz de Usuario** | Implementar modo oscuro y rediseño adaptativo con FXML modular. |
| **Reportes** | Añadir exportación en formato PDF y gráficos comparativos por periodos. |
| **Ventas** | Soporte para devoluciones, descuentos personalizados y puntos de fidelidad. |
| **Usuarios** | Sistema de roles (Administrador, Vendedor, Supervisor) con permisos específicos. |
| **Seguridad** | Autenticación de dos factores (2FA) y registro de actividad detallado. |
| **Base de Datos** | Implementar índices, vistas y procedimientos almacenados para optimizar el rendimiento. |
| **Distribución** | Crear instaladores multiplataforma (.exe / .deb / .pkg) con actualizaciones automáticas. |
| **Conectividad Web** | Migrar a arquitectura cliente-servidor con API REST para versiones móviles o web. |

---

## ⚖️ Licencia

Este proyecto se distribuye bajo la **Licencia MIT**, que permite el uso, copia, modificación y distribución libre del software, siempre que se mantenga el aviso de derechos de autor y la licencia original.  


