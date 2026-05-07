package com.biblioteca.dao;

import com.biblioteca.model.Usuario;
import com.biblioteca.util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    public void insertarUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios (nombre, email, telefono, fecha_alta, activo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getFechaAlta());
            preparedStatement.setInt(5, usuario.isActivo() ? 1 : 0);

            preparedStatement.executeUpdate();
            System.out.println("Usuario insertado correctamente.");

        } catch (SQLException e) {
            System.out.println("Error al insertar usuario.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public List<Usuario> listarUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();

        String sql = "SELECT id_usuario, nombre, email, telefono, fecha_alta, activo FROM usuarios";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql);
             ResultSet resultSet = preparedStatement.executeQuery()) {

            while (resultSet.next()) {
                Usuario usuario = new Usuario(
                        resultSet.getInt("id_usuario"),
                        resultSet.getString("nombre"),
                        resultSet.getString("email"),
                        resultSet.getString("telefono"),
                        resultSet.getString("fecha_alta"),
                        resultSet.getInt("activo") == 1
                );

                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar usuarios.");
            System.out.println("Detalle: " + e.getMessage());
        }

        return usuarios;
    }

    public Usuario buscarUsuarioPorId(int idUsuario) {
        String sql = "SELECT id_usuario, nombre, email, telefono, fecha_alta, activo FROM usuarios WHERE id_usuario = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return new Usuario(
                            resultSet.getInt("id_usuario"),
                            resultSet.getString("nombre"),
                            resultSet.getString("email"),
                            resultSet.getString("telefono"),
                            resultSet.getString("fecha_alta"),
                            resultSet.getInt("activo") == 1
                    );
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al buscar usuario.");
            System.out.println("Detalle: " + e.getMessage());
        }

        return null;
    }

    public void actualizarUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nombre = ?, email = ?, telefono = ?, fecha_alta = ?, activo = ? WHERE id_usuario = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setString(2, usuario.getEmail());
            preparedStatement.setString(3, usuario.getTelefono());
            preparedStatement.setString(4, usuario.getFechaAlta());
            preparedStatement.setInt(5, usuario.isActivo() ? 1 : 0);
            preparedStatement.setInt(6, usuario.getIdUsuario());

            int filas = preparedStatement.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario actualizado correctamente.");
            } else {
                System.out.println("No se encontró ningún usuario con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al actualizar usuario.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }

    public void eliminarUsuarioLogico(int idUsuario) {
        String sql = "UPDATE usuarios SET activo = 0 WHERE id_usuario = ?";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, idUsuario);

            int filas = preparedStatement.executeUpdate();

            if (filas > 0) {
                System.out.println("Usuario desactivado correctamente.");
            } else {
                System.out.println("No se encontró ningún usuario con ese ID.");
            }

        } catch (SQLException e) {
            System.out.println("Error al desactivar usuario.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}