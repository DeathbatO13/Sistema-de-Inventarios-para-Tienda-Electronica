package com.sistema.modelo;

public class Usuario {

    private int id;
    private String nombreUsuario;
    private String email;
    private String contrasenaHash;
    private String tokenVerificado;
    private boolean verificado;

    /**
     * Constructor vacio
     */
    public Usuario(){}

    /**
     * Constructor completo
     */
    public Usuario(int id, String nombreUsuario, boolean verificado, String tokenVerificado, String contrasenaHash, String email) {
        this.id = id;
        this.nombreUsuario = nombreUsuario;
        this.verificado = verificado;
        this.tokenVerificado = tokenVerificado;
        this.contrasenaHash = contrasenaHash;
        this.email = email;
    }

    /**
     * Constructor sin id
     */
    public Usuario(String nombreUsuario, String email, String contrasenaHash, String tokenVerificado, boolean verificado) {
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contrasenaHash = contrasenaHash;
        this.tokenVerificado = tokenVerificado;
        this.verificado = verificado;
    }


    //Setters y Getters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {
        this.nombreUsuario = nombreUsuario;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContrasenaHash() {
        return contrasenaHash;
    }

    public void setContrasenaHash(String contrasenaHash) {
        this.contrasenaHash = contrasenaHash;
    }

    public String getTokenVerificado() {
        return tokenVerificado;
    }

    public void setTokenVerificado(String tokenVerificado) {
        this.tokenVerificado = tokenVerificado;
    }

    public boolean isVerificado() {
        return verificado;
    }

    public void setVerificado(boolean verificado) {
        this.verificado = verificado;
    }
}
