package com.biblioteca.model;

public class Prestamo {

    private int id;
    private int libroId;
    private int usuarioId;
    private String fechaPrestamo;
    private String fechaDevolucion;
    private String estado; // ACTIVO / DEVUELTO

    public Prestamo() {}

    public Prestamo(int id, int libroId, int usuarioId, String fechaPrestamo, String fechaDevolucion, String estado) {
        this.id = id;
        this.libroId = libroId;
        this.usuarioId = usuarioId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaDevolucion = fechaDevolucion;
        this.estado = estado;
    }

    public int getId() { return id; }
    public int getLibroId() { return libroId; }
    public int getUsuarioId() { return usuarioId; }
    public String getFechaPrestamo() { return fechaPrestamo; }
    public String getFechaDevolucion() { return fechaDevolucion; }
    public String getEstado() { return estado; }

    public void setId(int id) { this.id = id; }
    public void setLibroId(int libroId) { this.libroId = libroId; }
    public void setUsuarioId(int usuarioId) { this.usuarioId = usuarioId; }
    public void setFechaPrestamo(String fechaPrestamo) { this.fechaPrestamo = fechaPrestamo; }
    public void setFechaDevolucion(String fechaDevolucion) { this.fechaDevolucion = fechaDevolucion; }
    public void setEstado(String estado) { this.estado = estado; }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id=" + id +
                ", libroId=" + libroId +
                ", usuarioId=" + usuarioId +
                ", fechaPrestamo='" + fechaPrestamo + '\'' +
                ", fechaDevolucion='" + fechaDevolucion + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}