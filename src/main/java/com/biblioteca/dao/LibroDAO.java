package com.biblioteca.dao;

import com.biblioteca.util.ConexionBD;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LibroDAO {

    public void listarLibros() {
        String sql = """
                SELECT 
                    l.id,
                    l.titulo,
                    l.isbn,
                    l.anio_publicacion,
                    a.nombre AS autor,
                    c.nombre AS categoria,
                    l.disponible
                FROM libros l
                INNER JOIN autores a ON l.autor_id = a.id
                INNER JOIN categorias c ON l.categoria_id = c.id
                ORDER BY l.id;
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== LISTADO DE LIBROS ===");

            while (rs.next()) {
                boolean disponible = rs.getInt("disponible") == 1;

                System.out.println("----------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("ISBN: " + rs.getString("isbn"));
                System.out.println("Año de publicación: " + rs.getInt("anio_publicacion"));
                System.out.println("Autor: " + rs.getString("autor"));
                System.out.println("Categoría: " + rs.getString("categoria"));
                System.out.println("Estado: " + (disponible ? "Disponible" : "No disponible"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros: " + e.getMessage());
        }
    }

    public void agregarLibro(String titulo, String isbn, int anioPublicacion, int autorId, int categoriaId) {
        String sql = """
                INSERT INTO libros 
                (titulo, isbn, anio_publicacion, autor_id, categoria_id, disponible)
                VALUES (?, ?, ?, ?, ?, 1);
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, isbn);
            stmt.setInt(3, anioPublicacion);
            stmt.setInt(4, autorId);
            stmt.setInt(5, categoriaId);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Libro agregado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al agregar libro: " + e.getMessage());
        }
    }

    public void buscarLibroPorTitulo(String tituloBuscado) {
        String sql = """
                SELECT 
                    l.id,
                    l.titulo,
                    l.isbn,
                    l.anio_publicacion,
                    a.nombre AS autor,
                    c.nombre AS categoria,
                    l.disponible
                FROM libros l
                INNER JOIN autores a ON l.autor_id = a.id
                INNER JOIN categorias c ON l.categoria_id = c.id
                WHERE l.titulo LIKE ?
                ORDER BY l.id;
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, "%" + tituloBuscado + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                boolean encontrado = false;

                System.out.println("\n=== RESULTADO DE BÚSQUEDA ===");

                while (rs.next()) {
                    encontrado = true;
                    boolean disponible = rs.getInt("disponible") == 1;

                    System.out.println("----------------------------------------");
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Título: " + rs.getString("titulo"));
                    System.out.println("ISBN: " + rs.getString("isbn"));
                    System.out.println("Año de publicación: " + rs.getInt("anio_publicacion"));
                    System.out.println("Autor: " + rs.getString("autor"));
                    System.out.println("Categoría: " + rs.getString("categoria"));
                    System.out.println("Estado: " + (disponible ? "Disponible" : "No disponible"));
                }

                if (!encontrado) {
                    System.out.println("No se encontraron libros con ese título.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar libro: " + e.getMessage());
        }
    }

    public void actualizarLibro(int id, String titulo, String isbn, int anioPublicacion, int autorId, int categoriaId) {
        String sql = """
                UPDATE libros
                SET titulo = ?,
                    isbn = ?,
                    anio_publicacion = ?,
                    autor_id = ?,
                    categoria_id = ?
                WHERE id = ?;
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, titulo);
            stmt.setString(2, isbn);
            stmt.setInt(3, anioPublicacion);
            stmt.setInt(4, autorId);
            stmt.setInt(5, categoriaId);
            stmt.setInt(6, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Libro actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún libro con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar libro: " + e.getMessage());
        }
    }

    public void eliminarLibro(int id) {
        String sql = "DELETE FROM libros WHERE id = ?;";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Libro eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún libro con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar libro: " + e.getMessage());
            System.out.println("Nota: si el libro tiene préstamos asociados, no se podrá eliminar.");
        }
    }

    public void listarLibrosDisponibles() {
        String sql = """
                SELECT 
                    l.id,
                    l.titulo,
                    l.isbn,
                    l.anio_publicacion,
                    a.nombre AS autor,
                    c.nombre AS categoria
                FROM libros l
                INNER JOIN autores a ON l.autor_id = a.id
                INNER JOIN categorias c ON l.categoria_id = c.id
                WHERE l.disponible = 1
                ORDER BY l.id;
                """;

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== LIBROS DISPONIBLES ===");
            boolean hayLibros = false;

            while (rs.next()) {
                hayLibros = true;

                System.out.println("----------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Título: " + rs.getString("titulo"));
                System.out.println("ISBN: " + rs.getString("isbn"));
                System.out.println("Año de publicación: " + rs.getInt("anio_publicacion"));
                System.out.println("Autor: " + rs.getString("autor"));
                System.out.println("Categoría: " + rs.getString("categoria"));
            }

            if (!hayLibros) {
                System.out.println("No hay libros disponibles actualmente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar libros disponibles: " + e.getMessage());
        }
    }
}