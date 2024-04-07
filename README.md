## Laboratorio

Aprenderás a desarrollar una API REST utilizando los siguientes proyectos de Spring Framework y bibliotecas

- [Spring Boot](https://docs.spring.io/spring-boot/docs/current/reference/html/getting-started.html#getting-started.introducing-spring-boot)
- [Spring Data JPA](https://docs.spring.io/spring-data/jpa/reference/jpa.html)
- [Spring Security](https://docs.spring.io/spring-security/reference/index.html)
- [JSON Web Tokens](https://jwt.io/introduction)

## Índice

1. [Dependencias](#dependencias)
2. [Requisitos funcionales](#requisitos-funcionales)
3. [Modelo de base de datos](#modelo-de-base-de-datos)
4. [Diseño de API REST](#diseño-de-api-rest)
5. [Implementación de los endpoints para gestionar los libros](#implementación-01)
6. [Implementación de la validación de la entrada de usuario y Exception Handling](#implementación-02)
7. [Referencia](#referencias)


## Dependencias
[Spring initializr](https://start.spring.io/#!type=maven-project&language=java&platformVersion=3.2.4&packaging=jar&jvmVersion=17&groupId=com.hampcode&artifactId=bookstoreapi&name=bookstoreapi&description=Demo%20project%20for%20Spring%20Boot&packageName=com.hampcode.bookstoreapi&dependencies=web,data-jpa,postgresql,lombok,devtools)

![Spring initializr](springi.png)

[Volver al Índice](#índice)

## Requisitos funcionales

| N° | Descripción                                                                                                  | Detalles                                                                                                     |
|----|--------------------------------------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------------------------|
| 1  | Como administrador, quiero poder gestionar los libros de la tienda, para mantener actualizado el inventario. | El libro debe tener los siguientes datos: título, descripción, precio, portada, archivo PDF, fecha de creación y fecha de actualización. |
| 2  | Como administrador, quiero poder gestionar los usuarios de la tienda, para administrar los permisos y privilegios de acceso. | El usuario debe tener los siguientes datos: nombre, apellido, email, contraseña, rol, fecha de creación y fecha de actualización. El rol puede ser administrador o usuario normal. |
| 3  | Como visitante, quiero poder iniciar sesión o registrarme si no tengo una cuenta, para acceder a funciones exclusivas. | Para iniciar sesión debe requerirse el email y la contraseña. Para registrarse se debe requerir el nombre, apellido, email y la contraseña. |
| 4  | Como visitante, quiero poder visualizar un catálogo de los últimos libros agregados, para estar al tanto de las novedades. | Esta debe mostrarse en la página de inicio. Para ello se debe considerar los 6 últimos libros agregados y ordenados en base a la fecha de creación. |
| 5  | Como visitante, quiero poder visualizar el catálogo completo de los libros de la tienda, para explorar todas las opciones disponibles. | Esta debe mostrarse en una página con los libros ordenados en base al título. La carga debe ser similar a las noticias en Facebook (cuando se hace scroll hacia abajo). |
| 6  | Como visitante, quiero poder agregar los libros a un carrito de compras, para poder comprar varios productos a la vez. | Cada libro debe mostrar un botón para agregar o quitar del carrito de compras. |
| 7  | Como visitante, quiero poder pagar la compra usando PayPal, para realizar transacciones de forma segura y conveniente. | Si este proceso resulta correcto, redirigir al usuario a la página de detalles de compra. Si falla, se debe volver al carrito de compras. |
| 8  | Como sistema, quiero registrar la compra y permitir la descarga de los libros, para proporcionar una experiencia de compra completa y satisfactoria. | La venta debe tener los siguientes datos: el monto total, el cliente (si el usuario estaba autenticado), la fecha de creación y los items. Cada ítem debe guardar el libro y su precio con el que se compró. El sistema debe restringir el número de descargas de cada ítem a 3 como máximo. |

[Volver al Índice](#índice)

## Modelo de base de datos
![Modelo  base de datos](db.png)

[Volver al Índice](#índice)

## Diseño de API REST

[Descargar bookstoreapi.postman_collection.json](bookstoreapi.postman_collection.json)


| Ruta base: /api/v1 |
|---------------------|

**Administración:** 

| N° | Función       | Recurso       | Verbo | Ruta              | Respuesta    | Representación | Status | Usuario y Rol |
|----|---------------|---------------|-------|-------------------|--------------|----------------|--------|---------------|
| 1  | Paginar libros|               | GET   | /admin/books/page            | 5 resultados por defecto, ordenados en base al título ascendentemente. | JSON | 200 | ADMIN |
| 2  | Crear nuevo libro |           | POST  | /admin/books            | El nuevo libro creado | JSON | 201, 400 | ADMIN |
| 3  | Obtener libro por ID |         | GET   | /admin/books/{id}       | El libro encontrado | JSON | 200, 404 | ADMIN |
| 4  | Actualizar libro por ID |      | PUT   | /admin/books/{id}       | El libro actualizado | JSON | 200, 400, 404 | ADMIN |
| 5  | Eliminar libro por ID |        | DELETE| /admin/books/{id}       | Nada. | - | 204, 404 | ADMIN |

**Gestión de archivos:**  
| N° | Función       | Recurso       | Verbo | Ruta              | Respuesta    | Representación | Status | Usuario y Rol |
|----|---------------|---------------|-------|-------------------|--------------|----------------|--------|---------------|
| 1  | Subir un archivo |              | POST  | /upload           | La ruta donde se ha almacenado el archivo | JSON | 200 | ANY |
| 2  | Recuperar un archivo |          | GET   | /{filename}       | El archivo Media |  | 200, 404 | ANY |

**Parte pública de la API:**  
| N° | Función       | Recurso       | Verbo | Ruta              | Respuesta    | Representación | Status | Usuario y Rol |
|----|---------------|---------------|-------|-------------------|--------------|----------------|--------|---------------|
| 1  | Consultar los últimos 6 libros agregados | | GET | /books/last | Los últimos 6 libros agregados en base a la fecha de creación | JSON | 200 | ANY |
| 2  | Paginar libros | | GET | /books | Una página de libros ordenado por el título de 5 en 5 | JSON | 200 | ANY |
| 3  | Obtener un libro por slug | | GET | /books/{slug} | El libro encontrado | JSON | 200, 404 | ANY |
| 4 | Crear compra | /purchases | POST | La compra | JSON |
| 5 | Descargar archivo PDF de un item de compra | /purchases/{purchaseId}/items/{itemId}/book/file | GET | El archivo PDF del item de la compra | Medio |
| 6 | Obtener una compra | /purchases/{id} | GET | La compra | JSON |
| 7 | Crear orden de pago en PayPal | /checkout/paypal/create | POST | La URL del formulario de pago PayPal | JSON |
| 8 | Capturar el pago de la orden PayPal | /checkout/paypal/capture | POST | Si el pago ha sido completado y el id de la compra | JSON |
| 9 | Obtener un access token | /auth/token | POST | El access token en formato Bearer token | JSON |

[Volver al Índice](#índice)

## Implementación 01
- Descargar los cambios de la rama `feature/implement-book-endpoints`:
  ```bash
  git pull origin feature/implement-book-endpoints
  ```

- En esta implementación se abordaron las siguientes tareas:
  - Mapeo de la entidad Book.
  - Creación del repositorio.
  - Creación del controlador.

- La configuración del `src/resources/application.properties`. Debes colocar password de tu instalación de PostgreSQL

  ```bash
  # Configuración de la ruta base para la API v1
  server.servlet.context-path= /api/v1

  # Configuración de la base de datos PostgreSQL
  spring.datasource.url=jdbc:postgresql://localhost:5432/bookstoredb
  spring.datasource.username=postgres
  spring.datasource.password=admin

  # Configuración de JPA
  spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
  spring.jpa.hibernate.ddl-auto=update
  spring.jpa.show-sql=true
     ```

[Volver al Índice](#índice)  

## Implementación 02
- Descargar los cambios de la rama `feature/user-input-validation-exception-handling`:
  ```bash
  git pull origin feature/user-input-validation-exception-handling
  ```

- En esta implementación se abordaron las siguientes tareas:
  - Implementación de la validación de la entrada de usuario.
  - Exception Handling.
  - Endpoints de `api/v1/books` públicos para los visitantes.
  - Endpoints de `api/v1/admin/books` para los administradores.

- Para `Exception Handling` necesitamos agregar  la dependencia  `spring-boot-starter-validation` en el archivo `pom.xml`

  ```
  <dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
  </dependency>
  ``` 

[Volver al Índice](#índice)

## Referencias
- [Métodos HTTP en MDN](https://developer.mozilla.org/es/docs/Web/HTTP/Methods)
- [Estado de códigos HTTP en MDN](https://developer.mozilla.org/es/docs/Web/HTTP/Status)
- [REST y el principio de idempotencia](https://www.adictosaltrabajo.com/2015/06/29/rest-y-el-principio-de-idempotencia/)
- [JPA Query Methods](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html)
- [Manejo de excepciones para REST con Spring en Baeldung](https://www.baeldung.com/exception-handling-for-rest-with-spring)
- [Usando la especificación ProblemDetail para respuestas de error en Spring Boot](https://medium.com/@mandeepdhakal11/using-problemdetail-specification-for-error-response-in-spring-boot-3-5d25956ef421)
- [Patrón DTO en Java](https://www.baeldung.com/java-dto-pattern)


[Volver al Índice](#índice)