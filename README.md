# Practica 2 - Registro Universitario - Proyecto Spring Boot

Este proyecto es una API RESTful desarrollada con Spring Boot para la gestión de un sistema de registro universitario. Permite la creación, lectura, actualización y eliminación de entidades como docentes, estudiantes, materias e inscripciones. Además, incluye funcionalidades de seguridad, validación y documentación con Swagger UI.

## Tecnologías Utilizadas

* Java
* Spring Boot
* Spring Web
* Spring Data JPA
* PostgreSQL
* Lombok
* Spring Security
* Spring Validation
* SpringDoc OpenAPI (para Swagger UI)
* Maven

## Requisitos Previos

* Java Development Kit (JDK) instalado (versión 21).
* Maven instalado.
* PostgreSQL instalado y en ejecución.

## Configuración

1.  **Clonar el Repositorio:**
    ```bash
    git clone [https://github.com/MikaelaCordova/Registro_Universitario_mbcv.git](https://github.com/MikaelaCordova/Registro_Universitario_mbcv.git)
    ```

2.  **Configuración de la Base de Datos:**
    Edita `src/main/resources/application.properties` con tus credenciales de PostgreSQL:
    ```properties
    spring.datasource.url=jdbc:postgresql://localhost:5432/tu_base_de_datos
    spring.datasource.username=tu_usuario
    spring.datasource.password=tu_contraseña
    spring.jpa.hibernate.ddl-auto=update
    ```

### Swagger UI

La documentación interactiva de la API está disponible en:

[http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)

![image](https://github.com/user-attachments/assets/f5478906-97e4-447f-973d-23279ddd0c36)

