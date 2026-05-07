package com.biblioteca.main;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.model.Libro;

import java.util.List;

public class TestLibroDAO {

    public static void main(String[] args) {

        LibroDAO libroDAO = new LibroDAO();

        System.out.println("=== LISTADO INICIAL DE LIBROS ===");
        List<Libro> libros = libroDAO.listarLibros();

        for (Libro libro : libros) {
            System.out.println(libro);
        }

        System.out.println("\n=== INSERTAR NUEVO LIBRO ===");
        Libro nuevoLibro = new Libro(
                "El principito",
                1943,
                "9780156012195",
                6,
                1,
                1
        );

        libroDAO.insertarLibro(nuevoLibro);

        System.out.println("\n=== LISTADO CON JOIN ===");
        libroDAO.listarLibrosConAutorYCategoria();

        System.out.println("\n=== BUSCAR LIBRO POR ID ===");
        Libro libroEncontrado = libroDAO.buscarLibroPorId(1);

        if (libroEncontrado != null) {
            System.out.println(libroEncontrado);
        } else {
            System.out.println("Libro no encontrado.");
        }

        System.out.println("\n=== ACTUALIZAR LIBRO ===");
        Libro libroActualizado = new Libro(
                1,
                "Cien años de soledad - Edición actualizada",
                1967,
                "9780307474728",
                4,
                1,
                1
        );

        libroDAO.actualizarLibro(libroActualizado);

        System.out.println("\n=== BUSCAR LIBROS POR CATEGORÍA ===");
        libroDAO.buscarLibrosPorCategoria("Novela");

        System.out.println("\n=== LISTADO FINAL DE LIBROS ===");
        libroDAO.listarLibrosConAutorYCategoria();
    }
}