PRAGMA foreign_keys = ON;

DROP TABLE IF EXISTS prestamos;
DROP TABLE IF EXISTS libros;
DROP TABLE IF EXISTS categorias;
DROP TABLE IF EXISTS autores;
DROP TABLE IF EXISTS usuarios;

CREATE TABLE usuarios (
    id_usuario INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE,
    telefono TEXT,
    fecha_alta TEXT NOT NULL,
    activo INTEGER NOT NULL DEFAULT 1
);

CREATE TABLE autores (
    id_autor INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL,
    nacionalidad TEXT
);

CREATE TABLE categorias (
    id_categoria INTEGER PRIMARY KEY AUTOINCREMENT,
    nombre TEXT NOT NULL UNIQUE,
    descripcion TEXT
);

CREATE TABLE libros (
    id_libro INTEGER PRIMARY KEY AUTOINCREMENT,
    titulo TEXT NOT NULL,
    anio_publicacion INTEGER,
    isbn TEXT UNIQUE,
    stock INTEGER NOT NULL DEFAULT 0,
    id_autor INTEGER NOT NULL,
    id_categoria INTEGER NOT NULL,

    FOREIGN KEY (id_autor) REFERENCES autores(id_autor),
    FOREIGN KEY (id_categoria) REFERENCES categorias(id_categoria),

    CHECK (stock >= 0)
);

CREATE TABLE prestamos (
    id_prestamo INTEGER PRIMARY KEY AUTOINCREMENT,
    id_usuario INTEGER NOT NULL,
    id_libro INTEGER NOT NULL,
    fecha_prestamo TEXT NOT NULL,
    fecha_devolucion TEXT,
    estado TEXT NOT NULL DEFAULT 'ACTIVO',

    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_libro) REFERENCES libros(id_libro),

    CHECK (estado IN ('ACTIVO', 'DEVUELTO', 'CANCELADO'))
);

INSERT INTO usuarios (nombre, email, telefono, fecha_alta, activo)
VALUES
('Carlos Pérez', 'carlos.perez@email.com', '600111222', '2026-05-01', 1),
('Laura Gómez', 'laura.gomez@email.com', '600333444', '2026-05-02', 1),
('Miguel Torres', 'miguel.torres@email.com', '600555666', '2026-05-03', 1),
('Ana Martínez', 'ana.martinez@email.com', '600777888', '2026-05-04', 1);

INSERT INTO autores (nombre, nacionalidad)
VALUES
('Gabriel García Márquez', 'Colombiana'),
('Isabel Allende', 'Chilena'),
('George Orwell', 'Británica'),
('J.K. Rowling', 'Británica');

INSERT INTO categorias (nombre, descripcion)
VALUES
('Novela', 'Obras narrativas de ficción'),
('Ciencia ficción', 'Libros basados en mundos futuros o alternativos'),
('Fantasía', 'Libros con elementos mágicos o sobrenaturales'),
('Clásicos', 'Obras literarias reconocidas históricamente');

INSERT INTO libros (titulo, anio_publicacion, isbn, stock, id_autor, id_categoria)
VALUES
('Cien años de soledad', 1967, '9780307474728', 3, 1, 1),
('La casa de los espíritus', 1982, '9788401352898', 2, 2, 1),
('1984', 1949, '9780451524935', 4, 3, 4),
('Rebelión en la granja', 1945, '9780451526342', 2, 3, 4),
('Harry Potter y la piedra filosofal', 1997, '9788478884452', 5, 4, 3);

INSERT INTO prestamos (id_usuario, id_libro, fecha_prestamo, fecha_devolucion, estado)
VALUES
(1, 1, '2026-05-05', NULL, 'ACTIVO'),
(2, 3, '2026-05-05', NULL, 'ACTIVO'),
(3, 2, '2026-05-04', '2026-05-06', 'DEVUELTO'),
(4, 5, '2026-05-03', NULL, 'ACTIVO');