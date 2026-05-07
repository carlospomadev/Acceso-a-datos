-- ============================================================
-- SCRIPT SQL COMPLETO - SISTEMA DE GESTIÓN DE BIBLIOTECA
-- Base de datos: SQLite
-- Archivo sugerido: biblioteca.sql
-- ============================================================

PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS autores;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE autores (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    nacionalidad TEXT,
    fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE categorias (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT,
    fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE usuarios (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT UNIQUE,
    telefono TEXT,
    fecha_registro TEXT DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE libros (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    isbn TEXT UNIQUE,
    anio_publicacion INTEGER,
    autor_id INTEGER NOT NULL,
    categoria_id INTEGER NOT NULL,
    disponible INTEGER NOT NULL DEFAULT 1,
    fecha_creacion TEXT DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (autor_id) REFERENCES autores(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    FOREIGN KEY (categoria_id) REFERENCES categorias(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT
);

CREATE TABLE prestamos (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    libro_id INTEGER NOT NULL,
    usuario_id INTEGER NOT NULL,
    fecha_prestamo TEXT NOT NULL DEFAULT CURRENT_TIMESTAMP,
    fecha_devolucion TEXT,
    estado TEXT NOT NULL DEFAULT 'ACTIVO',

    FOREIGN KEY (libro_id) REFERENCES libros(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    FOREIGN KEY (usuario_id) REFERENCES usuarios(id)
        ON UPDATE CASCADE
        ON DELETE RESTRICT,

    CHECK (estado IN ('ACTIVO', 'DEVUELTO'))
);

INSERT INTO autores (nombre, nacionalidad) VALUES
('Gabriel García Márquez', 'Colombiana'),
('Isabel Allende', 'Chilena'),
('Mario Vargas Llosa', 'Peruana'),
('George Orwell', 'Británica'),
('J.K. Rowling', 'Británica'),
('Miguel de Cervantes', 'Española');

INSERT INTO categorias (nombre, descripcion) VALUES
('Novela', 'Obras narrativas de ficción literaria'),
('Ciencia ficción', 'Libros relacionados con futuros imaginarios, tecnología o mundos alternativos'),
('Fantasía', 'Libros con elementos mágicos o sobrenaturales'),
('Historia', 'Libros relacionados con hechos históricos'),
('Tecnología', 'Libros sobre informática, programación y tecnología'),
('Clásicos', 'Obras literarias reconocidas por su valor histórico y cultural');

INSERT INTO usuarios (nombre, email, telefono) VALUES
('Carlos Poma', 'carlos.poma@email.com', '600111222'),
('Ana Martínez', 'ana.martinez@email.com', '600333444'),
('Luis García', 'luis.garcia@email.com', '600555666');

INSERT INTO libros (titulo, isbn, anio_publicacion, autor_id, categoria_id, disponible) VALUES
('Cien años de soledad', '9780307474728', 1967, 1, 1, 1),
('La casa de los espíritus', '9780553383805', 1982, 2, 1, 1),
('La ciudad y los perros', '9788466332462', 1963, 3, 1, 1),
('1984', '9780451524935', 1949, 4, 2, 1),
('Harry Potter y la piedra filosofal', '9788478884452', 1997, 5, 3, 1),
('Don Quijote de la Mancha', '9788491050297', 1605, 6, 6, 1);

INSERT INTO prestamos (libro_id, usuario_id, fecha_prestamo, estado) VALUES
(4, 2, CURRENT_TIMESTAMP, 'ACTIVO');

UPDATE libros
SET disponible = 0
WHERE id = 4;