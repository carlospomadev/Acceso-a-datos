# 📚 Sistema de Gestión de Biblioteca – Acceso a Datos

Proyecto académico desarrollado en **Java** para la asignatura **Acceso a Datos (AAD)** del ciclo **Desarrollo de Aplicaciones Multiplataforma (DAM)**.

El objetivo del proyecto es implementar un sistema de gestión de una biblioteca utilizando el **patrón DAO**, **JDBC** y una base de datos **SQLite**, aplicando buenas prácticas de organización, persistencia de datos e integridad referencial.

---

## ✨ Funcionalidades principales

- Gestión de **Libros**
  - Alta, baja, modificación y consulta de libros
  - Búsqueda de libros por título
  - Listado de libros disponibles

- Gestión de **Autores**
  - Alta, baja, modificación y consulta de autores

- Gestión de **Categorías**
  - Alta, baja, modificación y consulta de categorías

- Gestión de **Préstamos**
  - Control del estado de disponibilidad de los libros

- Validaciones:
  - No se permite crear libros sin autor ni categoría
  - Control de claves foráneas (SQLite)
  - Evita eliminar autores o categorías con libros asociados

---

## 🛠️ Tecnologías utilizadas

- **Java 17** (o superior)
- **JDBC**
- **SQLite**
- **Maven**
- **Git & GitHub**

---

## 📁 Estructura del proyecto

```text
Acceso-a-datos/
│
├── database/
│   └── script_biblioteca_sqlite.sql
│
├── src/main/java/com/biblioteca/
│   ├── dao/
│   │   ├── AutorDAO.java
│   │   ├── CategoriaDAO.java
│   │   ├── LibroDAO.java
│   │   ├── PrestamoDAO.java
│   │   └── UsuarioDAO.java
│   │
│   ├── model/
│   │   ├── Autor.java
│   │   ├── Categoria.java
│   │   ├── Libro.java
│   │   ├── Prestamo.java
│   │   └── Usuario.java
│   │
│   ├── util/
│   │   └── ConexionBD.java
│   │
│   └── main/
│       └── Main.java
│
├── pom.xml
└── README.md
