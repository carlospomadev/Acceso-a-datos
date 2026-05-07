package com.biblioteca.main;

import com.biblioteca.dao.LibroDAO;
import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Libro;
import com.biblioteca.model.Usuario;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    private static final DateTimeFormatter FORMATO_ENTRADA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FORMATO_BD = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static final UsuarioDAO usuarioDAO = new UsuarioDAO();
    private static final LibroDAO libroDAO = new LibroDAO();
    private static final PrestamoDAO prestamoDAO = new PrestamoDAO();

    public static void main(String[] args) {

        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1:
                    listarUsuarios();
                    break;
                case 2:
                    insertarUsuario();
                    break;
                case 3:
                    listarLibros();
                    break;
                case 4:
                    listarLibrosConDetalle();
                    break;
                case 5:
                    buscarLibrosPorCategoria();
                    break;
                case 6:
                    realizarPrestamo();
                    break;
                case 7:
                    devolverLibro();
                    break;
                case 8:
                    listarPrestamosActivos();
                    break;
                case 9:
                    mostrarInformeLibrosMasPrestados();
                    break;
                case 0:
                    System.out.println("Saliendo de la aplicación...");
                    break;
                default:
                    System.out.println("Opción no válida. Intente nuevamente.");
                    break;
            }

            System.out.println();

        } while (opcion != 0);

        scanner.close();
    }

    private static void mostrarMenu() {
        System.out.println("=====================================");
        System.out.println(" SISTEMA DE GESTIÓN DE BIBLIOTECA");
        System.out.println("=====================================");
        System.out.println("1. Listar usuarios");
        System.out.println("2. Insertar usuario");
        System.out.println("3. Listar libros");
        System.out.println("4. Listar libros con autor y categoría");
        System.out.println("5. Buscar libros por categoría");
        System.out.println("6. Realizar préstamo");
        System.out.println("7. Devolver libro");
        System.out.println("8. Listar préstamos activos");
        System.out.println("9. Informe de libros más prestados");
        System.out.println("0. Salir");
        System.out.println("=====================================");
    }

    private static void listarUsuarios() {
        System.out.println("=== LISTADO DE USUARIOS ===");

        List<Usuario> usuarios = usuarioDAO.listarUsuarios();

        if (usuarios.isEmpty()) {
            System.out.println("No hay usuarios registrados.");
        } else {
            System.out.printf(
                    "%-5s %-25s %-30s %-15s %-12s %-10s%n",
                    "ID", "Nombre", "Email", "Teléfono", "Fecha Alta", "Activo"
            );

            System.out.println("-----------------------------------------------------------------------------------------------");

            for (Usuario usuario : usuarios) {
                System.out.printf(
                        "%-5d %-25s %-30s %-15s %-12s %-10s%n",
                        usuario.getIdUsuario(),
                        usuario.getNombre(),
                        usuario.getEmail(),
                        usuario.getTelefono(),
                        convertirFechaParaMostrar(usuario.getFechaAlta()),
                        usuario.isActivo() ? "Sí" : "No"
                );
            }
        }
    }

    private static void insertarUsuario() {
        System.out.println("=== INSERTAR USUARIO ===");

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        System.out.print("Teléfono: ");
        String telefono = scanner.nextLine();

        System.out.print("Fecha de alta (DD/MM/YYYY): ");
        String fechaAltaUsuario = scanner.nextLine();

        if (nombre.isBlank() || email.isBlank() || fechaAltaUsuario.isBlank()) {
            System.out.println("Error: nombre, email y fecha de alta son obligatorios.");
            return;
        }

        String fechaAltaBD = convertirFechaParaBD(fechaAltaUsuario);

        if (fechaAltaBD == null) {
            System.out.println("Error: la fecha debe tener el formato DD/MM/YYYY. Ejemplo: 06/05/2026");
            return;
        }

        Usuario usuario = new Usuario(nombre, email, telefono, fechaAltaBD, true);
        usuarioDAO.insertarUsuario(usuario);
    }

    private static void listarLibros() {
        System.out.println("=== LISTADO DE LIBROS ===");

        List<Libro> libros = libroDAO.listarLibros();

        if (libros.isEmpty()) {
            System.out.println("No hay libros registrados.");
        } else {
            System.out.printf(
                "%-5s %-45s %-8s %-15s %-8s %-10s %-12s%n",
                "ID", "Título", "Año", "ISBN", "Stock", "Autor ID", "Categoría ID"
            );

            System.out.println("----------------------------------------------------------------------------------------------------------------");

            for (Libro libro : libros) {
                System.out.printf(
                    "%-5d %-45s %-8d %-15s %-8d %-10d %-12d%n",
                    libro.getIdLibro(),
                    libro.getTitulo(),
                    libro.getAnioPublicacion(),
                    libro.getIsbn(),
                    libro.getStock(),
                    libro.getIdAutor(),
                    libro.getIdCategoria()
                );
            }
        }
    }

    private static void listarLibrosConDetalle() {
        libroDAO.listarLibrosConAutorYCategoria();
    }

    private static void buscarLibrosPorCategoria() {
        System.out.println("=== BUSCAR LIBROS POR CATEGORÍA ===");

        libroDAO.listarCategoriasDisponibles();

        int idCategoria = leerEntero("Ingrese el ID de la categoría: ");

        libroDAO.buscarLibrosPorCategoriaId(idCategoria);
    }

    private static void realizarPrestamo() {
        System.out.println("=== REALIZAR PRÉSTAMO ===");

        int idUsuario = leerEntero("ID del usuario: ");
        int idLibro = leerEntero("ID del libro: ");

        System.out.print("Fecha de préstamo (DD/MM/YYYY): ");
        String fechaPrestamoUsuario = scanner.nextLine();

        if (fechaPrestamoUsuario.isBlank()) {
            System.out.println("Error: la fecha de préstamo es obligatoria.");
            return;
        }

        String fechaPrestamoBD = convertirFechaParaBD(fechaPrestamoUsuario);

        if (fechaPrestamoBD == null) {
            System.out.println("Error: la fecha debe tener el formato DD/MM/YYYY. Ejemplo: 06/05/2026");
            return;
        }

        prestamoDAO.realizarPrestamo(idUsuario, idLibro, fechaPrestamoBD);
    }

    private static void devolverLibro() {
        System.out.println("=== DEVOLVER LIBRO ===");

        int idPrestamo = leerEntero("ID del préstamo: ");

        System.out.print("Fecha de devolución (DD/MM/YYYY): ");
        String fechaDevolucionUsuario = scanner.nextLine();

        if (fechaDevolucionUsuario.isBlank()) {
            System.out.println("Error: la fecha de devolución es obligatoria.");
            return;
        }

        String fechaDevolucionBD = convertirFechaParaBD(fechaDevolucionUsuario);

        if (fechaDevolucionBD == null) {
            System.out.println("Error: la fecha debe tener el formato DD/MM/YYYY. Ejemplo: 06/05/2026");
            return;
        }

        prestamoDAO.devolverLibro(idPrestamo, fechaDevolucionBD);
    }

    private static void listarPrestamosActivos() {
        prestamoDAO.listarPrestamosActivosConDetalle();
    }

    private static void mostrarInformeLibrosMasPrestados() {
        prestamoDAO.informeLibrosMasPrestados();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: debe introducir un número válido.");
            }
        }
    }

    private static String convertirFechaParaBD(String fechaUsuario) {
        try {
            LocalDate fecha = LocalDate.parse(fechaUsuario, FORMATO_ENTRADA);
            return fecha.format(FORMATO_BD);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static String convertirFechaParaMostrar(String fechaBD) {
        try {
            if (fechaBD == null || fechaBD.isBlank()) {
                return "";
            }

            LocalDate fecha = LocalDate.parse(fechaBD, FORMATO_BD);
            return fecha.format(FORMATO_ENTRADA);
        } catch (DateTimeParseException e) {
            return fechaBD;
        }
    }
}