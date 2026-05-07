package com.biblioteca.main;

import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.model.Usuario;

import java.util.List;

public class TestUsuarioDAO {

    public static void main(String[] args) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();

        System.out.println("=== LISTADO INICIAL DE USUARIOS ===");
        List<Usuario> usuarios = usuarioDAO.listarUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        System.out.println("\n=== INSERTAR NUEVO USUARIO ===");
        Usuario nuevoUsuario = new Usuario(
                "Pedro Sánchez",
                "pedro.sanchez@email.com",
                "600999888",
                "2026-05-06",
                true
        );

        usuarioDAO.insertarUsuario(nuevoUsuario);

        System.out.println("\n=== LISTADO DESPUÉS DE INSERTAR ===");
        usuarios = usuarioDAO.listarUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }

        System.out.println("\n=== BUSCAR USUARIO POR ID ===");
        Usuario usuarioEncontrado = usuarioDAO.buscarUsuarioPorId(1);

        if (usuarioEncontrado != null) {
            System.out.println(usuarioEncontrado);
        } else {
            System.out.println("Usuario no encontrado.");
        }

        System.out.println("\n=== ACTUALIZAR USUARIO ===");
        Usuario usuarioActualizado = new Usuario(
                1,
                "Carlos Pérez Actualizado",
                "carlos.actualizado@email.com",
                "611111111",
                "2026-05-01",
                true
        );

        usuarioDAO.actualizarUsuario(usuarioActualizado);

        System.out.println("\n=== ELIMINACIÓN LÓGICA DE USUARIO ===");
        usuarioDAO.eliminarUsuarioLogico(2);

        System.out.println("\n=== LISTADO FINAL DE USUARIOS ===");
        usuarios = usuarioDAO.listarUsuarios();

        for (Usuario usuario : usuarios) {
            System.out.println(usuario);
        }
    }
}