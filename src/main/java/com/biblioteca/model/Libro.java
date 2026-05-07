package com.biblioteca.model;

public class Libro {

    private int id;
    private String titulo;
    private int anioPublicacion;
    private String isbn;
    private int disponible; // 1 = disponible, 0 = no disponible
    private int idAutor;
    private int idCategoria;

    public Libro() {
    }

    public Libro(int id, String titulo, int anioPublicacion, String isbn,
                 int disponible, int idAutor, int idCategoria) {
        this.id = id;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.isbn = isbn;
        this.disponible = disponible;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public Libro(String titulo, int anioPublicacion, String isbn,
                 int disponible, int idAutor, int idCategoria) {
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.isbn = isbn;
        this.disponible = disponible;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public int getDisponible() {
        return disponible;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", anioPublicacion=" + anioPublicacion +
                ", isbn='" + isbn + '\'' +
                ", disponible=" + disponible +
                ", idAutor=" + idAutor +
                ", idCategoria=" + idCategoria +
                '}';
    }
}
