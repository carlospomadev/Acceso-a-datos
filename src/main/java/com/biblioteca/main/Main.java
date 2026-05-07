package com.biblioteca.main;

import com.biblioteca.dao.AutorDAO;
import com.biblioteca.dao.CategoriaDAO;
import com.biblioteca.dao.LibroDAO;

import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final LibroDAO libroDAO = new LibroDAO();
    private static final AutorDAO autorDAO = new AutorDAO();
    private static final CategoriaDAO categoriaDAO = new CategoriaDAO();

    public static void main(String[] args) {
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {

                case 1 -> libroDAO.listarLibros();

                case 2 -> agregarLibro();

                case 3 -> libroDAO.buscarLibroPorTitulo(leerTexto("Ingrese el título a buscar: "));

                case 4 -> libroDAO.listarLibrosDisponibles();

                case 5 -> autorDAO.listarAutores();

                case 6 -> agregarAutor();

                case 7 -> categoriaDAO.listarCategorias();

                case 8 -> agregarCategoria();

                case 0 -> System.out.println("Saliendo del sistema...");

                default -> System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // ================= MENÚ =================

    private static void mostrarMenu() {
        System.out.println("\n======================================");
        System.out.println(" SISTEMA DE GESTIÓN DE BIBLIOTECA ");
        System.out.println("======================================");
        System.out.println("1. Listar libros");
        System.out.println("2. Agregar libro");
        System.out.println("3. Buscar libro por título");
        System.out.println("4. Listar libros disponibles");
        System.out.println("5. Listar autores");
        System.out.println("6. Agregar autor");
        System.out.println("7. Listar categorías");
        System.out.println("8. Agregar categoría");
        System.out.println("0. Salir");
        System.out.println("--------------------------------------");
    }

    // ================= LIBROS =================

    private static void agregarLibro() {
        System.out.println("\n=== AGREGAR LIBRO ===");

        String titulo = leerTexto("Título: ");
        String isbn = leerTexto("ISBN: ");
        int anio = leerEntero("Año de publicación: ");

        autorDAO.listarAutores();
        int autorId = leerEntero("Seleccione el ID del autor: ");

        if (!autorDAO.existeAutor(autorId)) {
            System.out.println("Autor no válido.");
            return;
        }

        categoriaDAO.listarCategorias();
        int categoriaId = leerEntero("Seleccione el ID de la categoría: ");

        if (!categoriaDAO.existeCategoria(categoriaId)) {
            System.out.println("Categoría no válida.");
            return;
        }

        libroDAO.agregarLibro(titulo, isbn, anio, autorId, categoriaId);
    }

    // ================= AUTORES =================

    private static void agregarAutor() {
        System.out.println("\n=== AGREGAR AUTOR ===");
        String nombre = leerTexto("Nombre del autor: ");
        String nacionalidad = leerTexto("Nacionalidad: ");
        autorDAO.agregarAutor(nombre, nacionalidad);
    }

    // ================= CATEGORÍAS =================

    private static void agregarCategoria() {
        System.out.println("\n=== AGREGAR CATEGORÍA ===");
        String nombre = leerTexto("Nombre de la categoría: ");
        String descripcion = leerTexto("Descripción: ");
        categoriaDAO.agregarCategoria(nombre, descripcion);
    }

    // ================= UTILIDADES =================

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return scanner.nextLine();
    }

    private static int leerEntero(String mensaje) {
        System.out.print(mensaje);
        while (!scanner.hasNextInt()) {
            System.out.print("Ingrese un número válido: ");
            scanner.next();
        }
        int valor = scanner.nextInt();
        scanner.nextLine(); // limpiar buffer
        return valor;
    }
}
