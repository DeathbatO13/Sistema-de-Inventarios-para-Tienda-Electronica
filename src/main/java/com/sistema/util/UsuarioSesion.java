package com.sistema.util;

import com.sistema.modelo.Usuario;

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

