package com.biblioteca.dao;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public void listarUsuarios() {
        String sql = """
                SELECT id, nombre, email, telefono, fecha_registro
                FROM usuarios
                ORDER BY id;
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== LISTADO DE USUARIOS ===");
            while (rs.next()) {
                System.out.println("----------------------------------------");
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("Nombre: " + rs.getString("nombre"));
                System.out.println("Email: " + rs.getString("email"));
                System.out.println("Teléfono: " + rs.getString("telefono"));
                System.out.println("Fecha registro: " + rs.getString("fecha_registro"));
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios: " + e.getMessage());
        }
    }

    public void agregarUsuario(String nombre, String email, String telefono) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono) VALUES (?, ?, ?);";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setString(1, nombre);
            stmt.setString(2, email);
            stmt.setString(3, telefono);

            if (stmt.executeUpdate() > 0) {
                System.out.println("Usuario agregado correctamente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al agregar usuario: " + e.getMessage());
            System.out.println("Nota: el email no puede repetirse.");
        }
    }

    public boolean existeUsuario(int id) {
        String sql = "SELECT id FROM usuarios WHERE id = ?;";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException e) {
            System.out.println("Error al validar usuario: " + e.getMessage());
            return false;
        }
    }
}