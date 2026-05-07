package com.biblioteca.main;

import com.biblioteca.dao.PrestamoDAO;
import com.biblioteca.model.Prestamo;

import java.util.List;

public class TestPrestamoDAO {

    public static void main(String[] args) {

        PrestamoDAO prestamoDAO = new PrestamoDAO();

        System.out.println("=== LISTADO INICIAL DE PRÉSTAMOS ===");
        List<Prestamo> prestamos = prestamoDAO.listarPrestamos();

        for (Prestamo prestamo : prestamos) {
            System.out.println(prestamo);
        }

        System.out.println("\n=== REALIZAR NUEVO PRÉSTAMO ===");
        prestamoDAO.realizarPrestamo(1, 3, "2026-05-06");

        System.out.println("\n=== PRÉSTAMOS ACTIVOS CON DETALLE ===");
        prestamoDAO.listarPrestamosActivosConDetalle();

        System.out.println("\n=== DEVOLVER LIBRO ===");
        prestamoDAO.devolverLibro(1, "2026-05-06");

        prestamoDAO.informeLibrosMasPrestados();

        System.out.println("\n=== LISTADO FINAL DE PRÉSTAMOS ===");
        prestamos = prestamoDAO.listarPrestamos();

        for (Prestamo prestamo : prestamos) {
            System.out.println(prestamo);
        }
    }
}