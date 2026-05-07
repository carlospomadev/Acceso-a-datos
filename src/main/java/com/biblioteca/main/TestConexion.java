package com.biblioteca.main;

import com.biblioteca.util.DBConnection;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class TestConexion {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            System.out.println("Conexión realizada correctamente con SQLite.");

            String sql = "SELECT id_usuario, nombre, email FROM usuarios";

            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(sql)) {

                System.out.println();
                System.out.println("Listado de usuarios:");
                System.out.println("-----------------------------");

                while (resultSet.next()) {
                    int id = resultSet.getInt("id_usuario");
                    String nombre = resultSet.getString("nombre");
                    String email = resultSet.getString("email");

                    System.out.println(id + " - " + nombre + " - " + email);
                }
            }

        } catch (SQLException e) {
            System.out.println("Error al conectar con la base de datos.");
            System.out.println("Detalle: " + e.getMessage());
        }
    }
}
