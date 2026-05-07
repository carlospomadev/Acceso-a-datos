package com.biblioteca.dao;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AutorDAO {

    public void listarAutores() {
        String sql = """
                SELECT id, nombre, nacionalidad, fecha_creacion
                FROM autores
                ORDER BY id;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== LISTADO DE AUTORES ===");

            while (rs.next()) {
                System.out.println("----------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Nacionalidad: " + rs.getString("nacionalidad"));
                System.out.println("Fecha de creación: " + rs.getString("fecha_creacion"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar autores: " + e.getMessage());
        }
    }

    public void agregarAutor(String nombre, String nacionalidad) {
        String sql = "INSERT INTO autores (nombre, nacionalidad) VALUES (?, ?);";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, nacionalidad);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Autor agregado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al agregar autor: " + e.getMessage());
        }
    }

    public void buscarAutorPorNombre(String nombreBuscado) {
        String sql = """
                SELECT id, nombre, nacionalidad, fecha_creacion
                FROM autores
                WHERE nombre LIKE ?
                ORDER BY id;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, "%" + nombreBuscado + "%");

            try (ResultSet rs = stmt.executeQuery()) {
                boolean encontrado = false;

                System.out.println("\n=== RESULTADO DE BÚSQUEDA DE AUTORES ===");

                while (rs.next()) {
                    encontrado = true;

                    System.out.println("----------------------------------------");
                    System.out.println("ID: " + rs.getInt("id"));
                    System.out.println("Nombre: " + rs.getString("nombre"));
                    System.out.println("Nacionalidad: " + rs.getString("nacionalidad"));
                    System.out.println("Fecha de creación: " + rs.getString("fecha_creacion"));
                }

                if (!encontrado) {
                    System.out.println("No se encontraron autores con ese nombre.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar autor: " + e.getMessage());
        }
    }

    public void actualizarAutor(int id, String nombre, String nacionalidad) {
        String sql = """
                UPDATE autores
                SET nombre = ?,
                    nacionalidad = ?
                WHERE id = ?;
                """;

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, nacionalidad);
            stmt.setInt(3, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Autor actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún autor con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar autor: " + e.getMessage());
        }
    }

    public void eliminarAutor(int id) {
        String sql = "DELETE FROM autores WHERE id = ?;";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Autor eliminado correctamente.");
            } else {
                System.out.println("No se encontró ningún autor con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al eliminar autor: " + e.getMessage());
            System.out.println("Nota: si el autor tiene libros asociados, no se podrá eliminar.");
        }
    }

    public boolean existeAutor(int id) {
        String sql = "SELECT id FROM autores WHERE id = ?;";

        try (Connection conexion = DBConnection.getConnection();
             PreparedStatement stmt = conexion.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar autor: " + e.getMessage());
            return false;
        }
    }
}
