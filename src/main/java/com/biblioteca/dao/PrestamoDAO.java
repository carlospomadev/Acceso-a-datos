package com.biblioteca.dao;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PrestamoDAO {

    // =========================================================
    // LISTADO DE PRÉSTAMOS ACTIVOS (TABULADO Y LIMPIO)
    // =========================================================
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
                ORDER BY p.id;
                """;

        try (Connection con = DBConnection.getConnection();
             PreparedStatement stmt = con.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n=== PRÉSTAMOS ACTIVOS ===");
            System.out.printf(
                "%-4s | %-35s | %-22s | %-20s%n",
                "ID", "LIBRO", "USUARIO", "FECHA PRÉSTAMO"
            );
            System.out.println("-------------------------------------------------------------------------------------");

            boolean hay = false;

            while (rs.next()) {
                hay = true;

                int id = rs.getInt("id");
                String libro = recortar(rs.getString("libro"), 35);
                String usuario = recortar(rs.getString("usuario"), 22);
                String fecha = rs.getString("fecha_prestamo");

                System.out.printf(
                    "%-4d | %-35s | %-22s | %-20s%n",
                    id, libro, usuario, fecha
                );
            }

            if (!hay) {
                System.out.println("No hay préstamos activos actualmente.");
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos activos: " + e.getMessage());
        }
    }

    // =========================================================
    // REGISTRAR PRÉSTAMO
    // =========================================================
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
                    if (rs.getInt("disponible") != 1) {
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

    // =========================================================
    // DEVOLVER PRÉSTAMO
    // =========================================================
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

            Integer libroId;

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
            try (PreparedStatement stmt = con.prepareStatement(sqlUpdatePrestamo)) {
                stmt.setInt(1, prestamoId);
                if (stmt.executeUpdate() == 0) {
                    System.out.println("No se pudo devolver el préstamo.");
                    con.rollback();
                    return;
                }
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

    // =========================================================
    // HISTORIAL DE PRÉSTAMOS POR USUARIO
    // =========================================================
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

    // =========================================================
    // HELPERS
    // =========================================================
    private boolean existeUsuario(Connection con, int usuarioId) throws SQLException {
        String sql = "SELECT id FROM usuarios WHERE id = ?;";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Evita que textos largos rompan las tablas en consola
    private String recortar(String texto, int max) {
        if (texto == null) return "";
        if (texto.length() <= max) return texto;
        return texto.substring(0, Math.max(0, max - 3)) + "...";
    }
}