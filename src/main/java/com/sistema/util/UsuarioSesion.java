package com.sistema.util;

import com.sistema.modelo.Usuario;

/**
 * Clase de utilidad para gestionar la sesión del usuario actual en la aplicación.
 * Utiliza un patrón Singleton implícito mediante un campo estático para mantener
 * una única referencia al {@link Usuario} que ha iniciado sesión.
 *
 * <p>Proporciona métodos estáticos para:
 * <ul>
 *   <li>Establecer el usuario actual.</li>
 *   <li>Obtener el usuario actual o su ID.</li>
 *   <li>Cerrar la sesión limpiando la referencia.</li>
 * </ul>
 * </p>
 *
 * <p><strong>Nota:</strong> No es thread-safe. Si la aplicación se ejecuta en un entorno
 * multihilo, se debe sincronizar el acceso a los métodos.</p>
 */
public class UsuarioSesion {


    private static Usuario usuarioActual;

    // Guarda el usuario que acaba de iniciar sesión
    public static void setUsuarioActual(Usuario usuario) {
        usuarioActual = usuario;
    }

    // Devuelve el usuario actual
    public static Usuario getUsuarioActual() {
        return usuarioActual;
    }

    // Devuelve el ID del usuario actual
    public static int getIdUsuarioActual() {
        if (usuarioActual == null) {
            throw new IllegalStateException("No hay un usuario en sesión.");
        }
        return usuarioActual.getId();
    }

    // Limpia la sesión (por ejemplo, al cerrar sesión)
    public static void cerrarSesion() {
        usuarioActual = null;
    }
}

