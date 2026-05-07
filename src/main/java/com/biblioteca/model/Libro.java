package com.biblioteca.model;

public class Libro {

    private int idLibro;
    private String titulo;
    private int anioPublicacion;
    private String isbn;
    private int stock;
    private int idAutor;
    private int idCategoria;

    public Libro() {
    }

    public Libro(int idLibro, String titulo, int anioPublicacion, String isbn, int stock, int idAutor, int idCategoria) {
        this.idLibro = idLibro;
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.isbn = isbn;
        this.stock = stock;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public Libro(String titulo, int anioPublicacion, String isbn, int stock, int idAutor, int idCategoria) {
        this.titulo = titulo;
        this.anioPublicacion = anioPublicacion;
        this.isbn = isbn;
        this.stock = stock;
        this.idAutor = idAutor;
        this.idCategoria = idCategoria;
    }

    public int getIdLibro() {
        return idLibro;
    }

    public void setIdLibro(int idLibro) {
        this.idLibro = idLibro;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getAnioPublicacion() {
        return anioPublicacion;
    }

    public void setAnioPublicacion(int anioPublicacion) {
        this.anioPublicacion = anioPublicacion;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public int getIdAutor() {
        return idAutor;
    }

    public void setIdAutor(int idAutor) {
        this.idAutor = idAutor;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "idLibro=" + idLibro +
                ", titulo='" + titulo + '\'' +
                ", anioPublicacion=" + anioPublicacion +
                ", isbn='" + isbn + '\'' +
                ", stock=" + stock +
                ", idAutor=" + idAutor +
                ", idCategoria=" + idCategoria +
                '}';
    }
}