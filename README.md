# Smart Library API

Backend REST API untuk fitur Manajemen Buku pada project Smart Library Management System menggunakan Java Spring Boot dan MySQL.

---

# Features

- Create Book
- Get All Books
- Get Book By ID
- Update Book
- Delete Book
- Search Book by:
  - Title
  - Author
  - ISBN
- Support Physical Book
- Support Digital Book
- Custom API Response
- Global Exception Handler

---

# Technologies

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL / Azure MySQL
- Maven
- VS Code
- Postman

---

# Project Structure

```text
src/main/java/com/smartlibrary/smart_library_api
│
├── controller
├── dto
│   ├── request
│   └── response
├── entity
├── exception
├── repository
├── service
└── SmartLibraryApiApplication.java
```

---

# Installation

## 1. Clone Repository

```bash
git clone <repository-url>
```

---

## 2. Open Project

If your using VS Code.

Recommended extensions:

- Extension Pack for Java
- Spring Boot Extension Pack

---

## 3. Database Configuration

This project uses a shared MySQL database server.

To run the project successfully, please contact the project owner or team members to obtain:

- Database host
- Database username
- Database password
- Database name

After obtaining the credentials, configure:

```text
src/main/resources/application.properties
```

Example configuration:

```properties
spring.application.name=smart-library-api

spring.datasource.url=YOUR_DATABASE_URL
spring.datasource.username=YOUR_DATABASE_USERNAME
spring.datasource.password=YOUR_DATABASE_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

---

# Run Project

## Using Maven

```bash
mvn spring-boot:run
```

---

## If Java Version Error Appears

Check Java version:

```bash
java -version
javac -version
```

If project uses Java 17:

Update `pom.xml`

```xml
<java.version>17</java.version>
```

Then run again:

```bash
mvn clean
mvn spring-boot:run
```

---

# Database Tables

Tables are generated automatically by Hibernate when the application runs.

Generated tables:

```text
tb_books
tb_physical_books
tb_digital_books
... (to be added)
```


---

# API Endpoints

Base URL:

```text
http://localhost:8080/api/
```

---

## Create Physical Book

### POST

```text
/api/books
```

### Body

```json
{
  "title": "Java Programming Basics",
  "author": "Budi Santoso",
  "isbn": "9781234567890",
  "bookType": "PHYSICAL",
  "available": true,
  "shelfLocation": "Rack A1",
  "fileUrl": null
}
```

---

## Create Digital Book

### POST

```json
{
  "title": "Spring Boot Guide",
  "author": "Andi Wijaya",
  "isbn": "9789876543210",
  "bookType": "DIGITAL",
  "available": true,
  "shelfLocation": null,
  "fileUrl": "https://example.com/springboot.pdf"
}
```

---

## Get All Books

### GET

```text
/api/books
```

---

## Get Book By ID

### GET

```text
/api/books/{id}
```

Example:

```text
/api/books/1
```

---

## Update Book

### PUT

```text
/api/books/{id}
```

---

## Delete Book

### DELETE

```text
/api/books/{id}
```

---

## Search Book

### Search All

```text
/api/books/search?keyword=java
```

### Search by Title

```text
/api/books/search/title?keyword=java
```

### Search by Author

```text
/api/books/search/author?keyword=budi
```

### Search by ISBN

```text
/api/books/search/isbn?keyword=978
```

---

# Success Response Example

```json
{
  "status": 200,
  "data": {
    "id": 1,
    "title": "Spring Boot Guide",
    "author": "Andi Wijaya",
    "isbn": "9789876543210",
    "available": true,
    "bookType": "DIGITAL",
    "info": "Spring Boot Guide - Andi Wijaya - 9789876543210",
    "shelfLocation": null,
    "fileUrl": "https://example.com/springboot.pdf"
  },
  "error": null
}
```

---

# Error Response Example

```json
{
  "status": 400,
  "data": [],
  "error": "isbn empty!"
}
```

---

# Notes

- This project uses inheritance between:
  - `Book`
  - `PhysicalBook`
  - `DigitalBook`

- Database tables are generated automatically using Hibernate.

- Do not commit `application.properties` containing real database credentials.

Recommended:

```text
application.properties        -> ignored
application-example.properties -> committed
```

---

# Contributors

- Muhammad Heiwa Anujesi Al Kahfi
  - Book Management
  - Book Entity
  - PhysicalBook
  - DigitalBook
  - REST API
  - Database Integration