package com.biblioteca.dao;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrestamoDAO {

    public void listarPrestamosActivos() {
        String sql = """
                SELECT p.id,
                       l.titulo AS libro,
                       u.nombre AS usuario,
                       p.fecha_prestamo
                FROM prestamos p
                INNER JOIN libros l ON p.libro_id = l.id
                INNER JOIN usuarios u ON p.usuario_id = u.id
                WHERE p.estado = 'ACTIVO'
                ORDER BY p.fecha_prestamo DESC;
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== PRÉSTAMOS ACTIVOS ===");
            boolean hay = false;

            while (rs.next()) {
                hay = true;
                System.out.println("----------------------------------------");
                System.out.println("ID Préstamo: " + rs.getInt("id"));
                System.out.println("Libro: " + rs.getString("libro"));
                System.out.println("Usuario: " + rs.getString("usuario"));
                System.out.println("Fecha préstamo: " + rs.getString("fecha_prestamo"));
            }

            if (!hay) {
                System.out.println("No hay préstamos activos.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos activos: " + e.getMessage());
        }
    }

    public void registrarPrestamo(int libroId, int usuarioId) {
        String sqlDisponible = "SELECT disponible FROM libros WHERE id = ?;";
        String sqlInsert = "INSERT INTO prestamos (libro_id, usuario_id, estado) VALUES (?, ?, 'ACTIVO');";
        String sqlUpdateLibro = "UPDATE libros SET disponible = 0 WHERE id = ?;";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            // 1) Validar libro existe y está disponible
            try (PreparedStatement stmt = con.prepareStatement(sqlDisponible)) {
                stmt.setInt(1, libroId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No existe un libro con ese ID.");
                        con.rollback();
                        return;
                    }
                    int disponible = rs.getInt("disponible");
                    if (disponible != 1) {
                        System.out.println("El libro NO está disponible (ya está prestado).");
                        con.rollback();
                        return;
                    }
                }
            }

            // 2) Validar usuario existe
            if (!existeUsuario(con, usuarioId)) {
                System.out.println("No existe un usuario con ese ID.");
                con.rollback();
                return;
            }

            // 3) Insertar préstamo
            try (PreparedStatement stmt = con.prepareStatement(sqlInsert)) {
                stmt.setInt(1, libroId);
                stmt.setInt(2, usuarioId);
                stmt.executeUpdate();
            }

            // 4) Marcar libro como no disponible
            try (PreparedStatement stmt = con.prepareStatement(sqlUpdateLibro)) {
                stmt.setInt(1, libroId);
                stmt.executeUpdate();
            }

            con.commit();
            System.out.println("Préstamo registrado correctamente. El libro ahora NO está disponible.");

        } catch (SQLException e) {
            System.out.println("Error al registrar préstamo: " + e.getMessage());
        }
    }

    public void devolverPrestamo(int prestamoId) {
        String sqlGetLibro = "SELECT libro_id FROM prestamos WHERE id = ? AND estado = 'ACTIVO';";
        String sqlUpdatePrestamo = """
                UPDATE prestamos
                SET estado = 'DEVUELTO',
                    fecha_devolucion = CURRENT_TIMESTAMP
                WHERE id = ? AND estado = 'ACTIVO';
                """;
        String sqlUpdateLibro = "UPDATE libros SET disponible = 1 WHERE id = ?;";

        try (Connection con = DBConnection.getConnection()) {
            con.setAutoCommit(false);

            Integer libroId = null;

            // 1) Obtener libro_id del préstamo activo
            try (PreparedStatement stmt = con.prepareStatement(sqlGetLibro)) {
                stmt.setInt(1, prestamoId);
                try (ResultSet rs = stmt.executeQuery()) {
                    if (!rs.next()) {
                        System.out.println("No existe un préstamo ACTIVO con ese ID.");
                        con.rollback();
                        return;
                    }
                    libroId = rs.getInt("libro_id");
                }
            }

            // 2) Marcar préstamo como DEVUELTO
            int filas;
            try (PreparedStatement stmt = con.prepareStatement(sqlUpdatePrestamo)) {
                stmt.setInt(1, prestamoId);
                filas = stmt.executeUpdate();
            }

            if (filas == 0) {
                System.out.println("No se pudo devolver el préstamo (quizá ya estaba devuelto).");
                con.rollback();
                return;
            }

            // 3) Marcar libro como disponible
            try (PreparedStatement stmt = con.prepareStatement(sqlUpdateLibro)) {
                stmt.setInt(1, libroId);
                stmt.executeUpdate();
            }

            con.commit();
            System.out.println("Préstamo devuelto correctamente. El libro ahora está disponible.");

        } catch (SQLException e) {
            System.out.println("Error al devolver préstamo: " + e.getMessage());
        }
    }

    public void historialPrestamosPorUsuario(int usuarioId) {
        String sql = """
                SELECT p.id,
                       l.titulo AS libro,
                       p.fecha_prestamo,
                       p.fecha_devolucion,
                       p.estado
                FROM prestamos p
                INNER JOIN libros l ON p.libro_id = l.id
                WHERE p.usuario_id = ?
                ORDER BY p.fecha_prestamo DESC;
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql)) {

            stmt.setInt(1, usuarioId);

            try (ResultSet rs = stmt.executeQuery()) {
                System.out.println("\n=== HISTORIAL DE PRÉSTAMOS (USUARIO ID " + usuarioId + ") ===");
                boolean hay = false;

                while (rs.next()) {
                    hay = true;
                    System.out.println("----------------------------------------");
                    System.out.println("ID Préstamo: " + rs.getInt("id"));
                    System.out.println("Libro: " + rs.getString("libro"));
                    System.out.println("Fecha préstamo: " + rs.getString("fecha_prestamo"));
                    System.out.println("Fecha devolución: " + rs.getString("fecha_devolucion"));
                    System.out.println("Estado: " + rs.getString("estado"));
                }

                if (!hay) {
                    System.out.println("Este usuario no tiene préstamos registrados.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al consultar historial: " + e.getMessage());
        }
    }

    // ===== Helpers privados =====

    private boolean existeUsuario(Connection con, int usuarioId) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE id = ?;";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }
}