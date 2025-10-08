package com.sistema.modelo;

public class Proveedor {

    private int id;
    private String nombre;
    private String contacto;
    private String telefono;
    private String email;

    /**
     * Constructor vacio
     */
    public Proveedor(){}

    /**
     * Constructor completo
     */
    public Proveedor(int id, String nombre, String contacto, String telefono, String email) {
        this.id = id;
        this.nombre = nombre;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
    }

    /**
     * Constructor sin id
     */
    public Proveedor(String nombre, String contacto, String telefono, String email) {
        this.nombre = nombre;
        this.contacto = contacto;
        this.telefono = telefono;
        this.email = email;
    }

    //Getters y Setters


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
