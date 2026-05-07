package com.biblioteca.model;

public class Usuario {

    private int idUsuario;
    private String nombre;
    private String email;
    private String telefono;
    private String fechaAlta;
    private boolean activo;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String email, String telefono, String fechaAlta, boolean activo) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    public Usuario(String nombre, String email, String telefono, String fechaAlta, boolean activo) {
        this.nombre = nombre;
        this.email = email;
        this.telefono = telefono;
        this.fechaAlta = fechaAlta;
        this.activo = activo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFechaAlta() {
        return fechaAlta;
    }

    public void setFechaAlta(String fechaAlta) {
        this.fechaAlta = fechaAlta;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario=" + idUsuario +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", telefono='" + telefono + '\'' +
                ", fechaAlta='" + fechaAlta + '\'' +
                ", activo=" + activo +
                '}';
    }
}