package com.biblioteca.dao;

import com.biblioteca.model.Prestamo;
import com.biblioteca.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PrestamoDAO {

    public void realizarPrestamo(int idUsuario, int idLibro, String fechaPrestamo) {
        String sqlConsultarStock = "SELECT stock FROM libros WHERE id_libro = ?";
        String sqlInsertarPrestamo = "INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion, estado) VALUES (?, ?, ?, NULL, 'ACTIVO')";
        String sqlActualizarStock = "UPDATE libros SET stock = stock - 1 WHERE id_libro = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement psStock = connection.prepareStatement(sqlConsultarStock);
                    PreparedStatement psPrestamo = connection.prepareStatement(sqlInsertarPrestamo);
                    PreparedStatement psStockUpdate = connection.prepareStatement(sqlActualizarStock)
            ) {
                psStock.setInt(1, idLibro);

                try (ResultSet resultSet = psStock.executeQuery()) {
                    if (!resultSet.next()) {
                        System.out.println("No existe ningún libro con ese ID.");
                        connection.rollback();
                        return;
                    }

                    int stock = resultSet.getInt("stock");

                    if (stock <= 0) {
                        System.out.println("No se puede realizar el préstamo. El libro no tiene stock disponible.");
                        connection.rollback();
                        return;
                    }
                }

                psPrestamo.setInt(1, idUsuario);
                psPrestamo.setInt(2, idLibro);
                psPrestamo.setString(3, fechaPrestamo);
                psPrestamo.executeUpdate();

                psStockUpdate.setInt(1, idLibro);
                psStockUpdate.executeUpdate();

                connection.commit();
                System.out.println("Préstamo realizado correctamente.");

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error al realizar préstamo.");
                System.out.println("Detalle: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión al realizar préstamo.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void devolverLibro(int idPrestamo, String fechaDevolucion) {
        String sqlBuscarPrestamo = "SELECT id_libro, estado FROM prestamos WHERE id_prestamo = ?";
        String sqlActualizarPrestamo = "UPDATE prestamos SET fecha_devolucion = ?, estado = 'DEVUELTO' WHERE id_prestamo = ?";
        String sqlActualizarStock = "UPDATE libros SET stock = stock + 1 WHERE id_libro = ?";

        try (Connection connection = DBConnection.getConnection()) {

            connection.setAutoCommit(false);

            try (
                    PreparedStatement psBuscar = connection.prepareStatement(sqlBuscarPrestamo);
                    PreparedStatement psActualizarPrestamo = connection.prepareStatement(sqlActualizarPrestamo);
                    PreparedStatement psActualizarStock = connection.prepareStatement(sqlActualizarStock)
            ) {
                psBuscar.setInt(1, idPrestamo);

                int idLibro;

                try (ResultSet resultSet = psBuscar.executeQuery()) {
                    if (!resultSet.next()) {
                        System.out.println("No existe ningún préstamo con ese ID.");
                        connection.rollback();
                        return;
                    }

                    String estado = resultSet.getString("estado");

                    if ("DEVUELTO".equalsIgnoreCase(estado)) {
                        System.out.println("Este préstamo ya fue devuelto anteriormente.");
                        connection.rollback();
                        return;
                    }

                    idLibro = resultSet.getInt("id_libro");
                }

                psActualizarPrestamo.setString(1, fechaDevolucion);
                psActualizarPrestamo.setInt(2, idPrestamo);
                psActualizarPrestamo.executeUpdate();

                psActualizarStock.setInt(1, idLibro);
                psActualizarStock.executeUpdate();

                connection.commit();
                System.out.println("Libro devuelto correctamente.");

            } catch (SQLException e) {
                connection.rollback();
                System.out.println("Error al devolver libro.");
                System.out.println("Detalle: " + e.getMessage());
            } finally {
                connection.setAutoCommit(true);
            }

        } catch (SQLException e) {
            System.out.println("Error de conexión al devolver libro.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public List<Prestamo> listarPrestamos() {
        List<Prestamo> prestamos = new ArrayList<>();

        String sql = "SELECT id_prestamo, id_usuario, id_libro, fecha_prestamo, fecha_devolucion, estado FROM prestamos";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Prestamo prestamo = new Prestamo(
                        resultSet.getInt("id_prestamo"),
                        resultSet.getInt("id_usuario"),
                        resultSet.getInt("id_libro"),
                        resultSet.getString("fecha_prestamo"),
                        resultSet.getString("fecha_devolucion"),
                        resultSet.getString("estado")
                );

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos.");
            System.out.println("Detalle: " + e.getMessage());
        }

        return prestamos;
    }

    public void listarPrestamosActivosConDetalle() {
        String sql = """
                SELECT 
                    p.id_prestamo,
                    u.nombre AS usuario,
                    l.titulo AS libro,
                    p.fecha_prestamo,
                    p.estado
                FROM prestamos p
                INNER JOIN usuarios u ON p.id_usuario = u.id_usuario
                INNER JOIN libros l ON p.id_libro = l.id_libro
                WHERE p.estado = 'ACTIVO'
                ORDER BY p.fecha_prestamo DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("=== PRÉSTAMOS ACTIVOS CON DETALLE ===");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getInt("id_prestamo") + " - Usuario: " +
                        resultSet.getString("usuario") + " - Libro: " +
                        resultSet.getString("libro") + " - Fecha préstamo: " +
                        resultSet.getString("fecha_prestamo") + " - Estado: " +
                        resultSet.getString("estado")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al listar préstamos activos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void informeLibrosMasPrestados() {
        String sql = """
                SELECT 
                    l.titulo,
                    COUNT(p.id_prestamo) AS total_prestamos
                FROM prestamos p
                INNER JOIN libros l ON p.id_libro = l.id_libro
                GROUP BY l.titulo
                ORDER BY total_prestamos DESC
                """;

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("=== INFORME: LIBROS MÁS PRESTADOS ===");

            while (resultSet.next()) {
                System.out.println(
                        resultSet.getString("titulo") +
                        " - Total préstamos: " +
                        resultSet.getInt("total_prestamos")
                );
            }

        } catch (SQLException e) {
            System.out.println("Error al generar informe.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}