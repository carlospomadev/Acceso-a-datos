package com.biblioteca.dao;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CategoriaDAO {

    public void listarCategorias() {
        String sql = """
                SELECT id, nombre, descripcion, fecha_creacion
                FROM categorias
                ORDER BY id;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== LISTADO DE CATEGORÍAS ===");

            while (rs.next()) {
                System.out.println("----------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Descripción: " + rs.getString("descripcion"));
                System.out.println("Fecha de creación: " + rs.getString("fecha_creacion"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar categorías: " + e.getMessage());
        }
    }

    public void agregarCategoria(String nombre, String descripcion) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?);";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Categoría agregada correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al agregar categoría: " + e.getMessage());
            System.out.println("Nota: el nombre de la categoría no puede repetirse.");
        }
    }

    public void buscarCategoriaPorNombre(String nombreBuscado) {
        String sql = """
                SELECT id, nombre, descripcion, fecha_creacion
                FROM categorias
                WHERE nombre LIKE ?
                ORDER BY id;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                boolean encontrado = false;

                System.out.println("\n=== RESULTADO DE BÚSQUEDA DE CATEGORÍAS ===");

                while (rs.next()) {
                    encontrado = true;

                    System.out.println("----------------------------------------");
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nombre: " + rs.getString("nombre"));
                    System.out.println("Descripción: " + rs.getString("descripcion"));
                    System.out.println("Fecha de creación: " + rs.getString("fecha_creacion"));
                }

                if (!encontrado) {
                    System.out.println("No se encontraron categorías con ese nombre.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar categoría: " + e.getMessage());
        }
    }

    public void actualizarCategoria(int id, String nombre, String descripcion) {
        String sql = """
                UPDATE categorias
                SET nombre = ?,
                    descripcion = ?
                WHERE id = ?;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setInt(3, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Categoría actualizada correctamente.");
            } else {
                System.out.println("No se encontró ninguna categoría con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar categoría: " + e.getMessage());
            System.out.println("Nota: el nombre de la categoría no puede repetirse.");
        }
    }

    public void eliminarCategoria(int id) {
        String sql = "DELETE FROM categorias WHERE id = ?;";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Categoría eliminada correctamente.");
            } else {
                System.out.println("No se encontró ninguna categoría con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar categoría: " + e.getMessage());
            System.out.println("Nota: si la categoría tiene libros asociados, no se podrá eliminar.");
        }
    }

    public boolean existeCategoria(int id) {
        String sql = "SELECT id FROM categorias WHERE id = ?;";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar categoría: " + e.getMessage());
            return false;
        }
    }
}