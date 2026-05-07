package com.biblioteca.dao;

import com.biblioteca.model.Libro;
import com.biblioteca.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LibroDAO {

    public void insertarLibro(Libro libro) {
        String sql = "INSERT INTO libros (titulo, anio_publicacion, isbn, stock, id_autor, id_categoria) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setInt(2, libro.getAnioPublicacion());
            preparedStatement.setString(3, libro.getIsbn());
            preparedStatement.setInt(4, libro.getStock());
            preparedStatement.setInt(5, libro.getIdAutor());
            preparedStatement.setInt(6, libro.getIdCategoria());

            preparedStatement.executeUpdate();
            System.out.println("Libro insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar libro.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public List<Libro> listarLibros() {
        List<Libro> libros = new ArrayList<>();

        String sql = "SELECT id_libro, titulo, anio_publicacion, isbn, stock, id_autor, id_categoria FROM libros";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Libro libro = new Libro(
                        resultSet.getInt("id_libro"),
                        resultSet.getString("titulo"),
                        resultSet.getInt("anio_publicacion"),
                        resultSet.getString("isbn"),
                        resultSet.getInt("stock"),
                        resultSet.getInt("id_autor"),
                        resultSet.getInt("id_categoria")
                );

                libros.add(libro);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros.");
            System.out.println("Detalle: " + e.getMessage());
        }

        return libros;
    }

    public Libro buscarLibroPorId(int idLibro) {
        String sql = "SELECT id_libro, titulo, anio_publicacion, isbn, stock, id_autor, id_categoria FROM libros WHERE id_libro = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idLibro);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Libro(
                            resultSet.getInt("id_libro"),
                            resultSet.getString("titulo"),
                            resultSet.getInt("anio_publicacion"),
                            resultSet.getString("isbn"),
                            resultSet.getInt("stock"),
                            resultSet.getInt("id_autor"),
                            resultSet.getInt("id_categoria")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libro.");
            System.out.println("Detalle: " + e.getMessage());
        }

        return null;
    }

    public void actualizarLibro(Libro libro) {
        String sql = "UPDATE libros SET titulo = ?, anio_publicacion = ?, isbn = ?, stock = ?, id_autor = ?, id_categoria = ? WHERE id_libro = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, libro.getTitulo());
            preparedStatement.setInt(2, libro.getAnioPublicacion());
            preparedStatement.setString(3, libro.getIsbn());
            preparedStatement.setInt(4, libro.getStock());
            preparedStatement.setInt(5, libro.getIdAutor());
            preparedStatement.setInt(6, libro.getIdCategoria());
            preparedStatement.setInt(7, libro.getIdLibro());

            int filas = preparedStatement.executeUpdate();

            if (filas > 0) {
                System.out.println("Libro actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún libro con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void eliminarLibro(int idLibro) {
        String sql = "DELETE FROM libros WHERE id_libro = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idLibro);

            int filas = preparedStatement.executeUpdate();

            if (filas > 0) {
                System.out.println("Libro eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún libro con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void listarLibrosConAutorYCategoria() {
        String sql = """
            SELECT 
                l.id_libro,
                l.titulo,
                l.anio_publicacion,
                l.isbn,
                l.stock,
                a.nombre AS autor,
                c.nombre AS categoria
            FROM libros l
            INNER JOIN autores a ON l.id_autor = a.id_autor
            INNER JOIN categorias c ON l.id_categoria = c.id_categoria
            ORDER BY l.titulo
            """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("=== LISTADO DE LIBROS CON AUTOR Y CATEGORÍA ===");

            System.out.printf(
                "%-5s %-40s %-8s %-15s %-8s %-28s %-15s%n",
                "ID", "Título", "Año", "ISBN", "Stock", "Autor", "Categoría"
            );

            System.out.println("-------------------------------------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                System.out.printf(
                    "%-5d %-40s %-8d %-15s %-8d %-28s %-15s%n",
                    resultSet.getInt("id_libro"),
                    resultSet.getString("titulo"),
                    resultSet.getInt("anio_publicacion"),
                    resultSet.getString("isbn"),
                    resultSet.getInt("stock"),
                    resultSet.getString("autor"),
                    resultSet.getString("categoria")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros con autor y categoría.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void buscarLibrosPorCategoria(String categoria) {
        String sql = """
                SELECT 
                    l.titulo,
                    a.nombre AS autor,
                    c.nombre AS categoria,
                    l.stock
                FROM libros l
                INNER JOIN autores a ON l.id_autor = a.id_autor
                INNER JOIN categorias c ON l.id_categoria = c.id_categoria
                WHERE c.nombre = ?
                ORDER BY l.titulo
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, categoria);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                System.out.println("=== LIBROS DE LA CATEGORÍA: " + categoria + " ===");

                while (resultSet.next()) {
                    System.out.println(
                            resultSet.getString("titulo") +
                            " - Autor: " + resultSet.getString("autor") +
                            " - Stock: " + resultSet.getInt("stock")
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libros por categoría.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
    public void listarCategoriasDisponibles() {
        String sql = "SELECT id_categoria, nombre FROM categorias ORDER BY id_categoria";

        try (Connection connection = DBConnection.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("=== CATEGORÍAS DISPONIBLES ===");

            System.out.printf("%-5s %-25s%n", "ID", "Categoría");
            System.out.println("--------------------------------");

            while (resultSet.next()) {
                System.out.printf(
                    "%-5d %-25s%n",
                    resultSet.getInt("id_categoria"),
                    resultSet.getString("nombre")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
    public void buscarLibrosPorCategoriaId(int idCategoria) {
        String sql = """
            SELECT 
                l.titulo,
                a.nombre AS autor,
                c.nombre AS categoria,
                l.stock
            FROM libros l
            INNER JOIN autores a ON l.id_autor = a.id_autor
            INNER JOIN categorias c ON l.id_categoria = c.id_categoria
            WHERE c.id_categoria = ?
            ORDER BY l.titulo
            """;

        try (Connection connection = DBConnection.getConnection();
         PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idCategoria);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
            System.out.println("=== LIBROS DE LA CATEGORÍA SELECCIONADA ===");

            boolean hayResultados = false;

            System.out.printf("%-40s %-30s %-15s %-8s%n",
                    "Título", "Autor", "Categoría", "Stock");

            System.out.println("------------------------------------------------------------------------------------------");

            while (resultSet.next()) {
                hayResultados = true;

                System.out.printf("%-40s %-30s %-15s %-8d%n",
                        resultSet.getString("titulo"),
                        resultSet.getString("autor"),
                        resultSet.getString("categoria"),
                        resultSet.getInt("stock"));
                    }

                if (!hayResultados) {
                System.out.println("No se encontraron libros para la categoría seleccionada.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libros por categoría.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}