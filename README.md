# My Portfolio — API REST

API REST para gestionar la información de un portfolio personal: datos del perfil, habilidades, formación académica, experiencia laboral y proyectos.

Construida con **Spring Boot 4.1.1** y **Java 21**, con persistencia en **PostgreSQL** mediante **Spring Data JDBC** (`JdbcTemplate`), sin ORM.

---

## Características

- API REST para 5 recursos: información personal, habilidades, educación, experiencia y proyectos.
- Persistencia relacional con SQL explícito (`JdbcTemplate`) y mapeo manual de filas (`RowMapper`).
- Arquitectura por capas: `rest` → `service` → `repository` → PostgreSQL.
- Esquema de base de datos y datos de ejemplo inicializados automáticamente por Spring Boot.
- Base de datos reproducible en un solo comando con Docker Compose.
- Lombok para reducir el código boilerplate de los modelos.

---

## Stack tecnológico

| Componente | Versión / detalle |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Persistencia | `spring-boot-starter-data-jdbc` (`JdbcTemplate`) |
| Web | `spring-boot-starter-webmvc` |
| Base de datos | PostgreSQL 16 (Alpine) |
| Driver | `org.postgresql:postgresql` |
| Lombok | `org.projectlombok:lombok` (opcional) |
| Build | Maven (con wrapper `mvnw`) |
| DevTools | `spring-boot-devtools` (recarga en caliente) |

---

## Requisitos previos

- **Java 21** o superior.
- **Docker** con **Docker Compose** (para levantar PostgreSQL).
- **Maven** no es obligatorio: el proyecto incluye el wrapper `./mvnw`.

---

## Puesta en marcha

### 1. Levantar la base de datos

```bash
docker compose up -d
```

Esto crea el contenedor `db` con PostgreSQL 16 en el puerto `5432`, con la base de datos `portfolio`, el usuario `portfolio_user` y la contraseña `portfolio_secret`. El volumen `postgres_data` persiste los datos entre reinicios.

Verificar que la base de datos está lista:

```bash
docker compose ps
```

Para detenerla:

```bash
docker compose down
```

Para detenerla **y borrar el volumen** (empezar de cero):

```bash
docker compose down -v
```

### 2. Compilar y ejecutar la aplicación

```bash
./mvnw spring-boot:run
```

O bien, generar el JAR y ejecutarlo:

```bash
./mvnw clean package
java -jar target/my-portfolio-0.0.1-SNAPSHOT.jar
```

La aplicación queda disponible en:

```
http://localhost:8080
```

### 3. Probar la API

```bash
curl http://localhost:8080/api/test/skills/all
```

```bash
curl http://localhost:8080/api/test/personal-info/1
```

```bash
curl -X POST http://localhost:8080/api/test/projects \
  -H "Content-Type: application/json" \
  -d '{"title":"Mi proyecto","description":"Descripción del proyecto","imageUrl":"img/proyecto.jpg","projectUrl":"https://github.com/DiegoDom","personalInfoId":1}'
```

```bash
curl -X DELETE http://localhost:8080/api/test/projects/1
```

---

## Configuración

Los parámetros de conexión están en `src/main/resources/application.properties`:

```properties
spring.application.name=my-portfolio
spring.datasource.url=jdbc:postgresql://localhost:5432/portfolio
spring.datasource.username=portfolio_user
spring.datasource.password=portfolio_secret
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.connection-timeout=30000
spring.sql.init.mode=always
```

| Propiedad | Descripción |
|---|---|
| `spring.datasource.url` | URL de conexión a PostgreSQL. |
| `spring.datasource.username` / `password` | Credenciales de la base de datos. |
| `spring.datasource.hikari.maximum-pool-size` | Tamaño máximo del pool de conexiones (10). |
| `spring.datasource.hikari.connection-timeout` | Tiempo de espera para obtener una conexión (30 s). |
| `spring.sql.init.mode=always` | Ejecuta `schema.sql` y `data.sql` en cada arranque. |

> Las credenciales están en texto plano y son **solo para desarrollo**. En producción deben inyectarse por variables de entorno (`SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`) o un gestor de secretos.

---

## Estructura del proyecto

```
my-portfolio/
├── docker-compose.yaml              # PostgreSQL 16 para desarrollo
├── pom.xml
├── mvnw / mvnw.cmd / .mvn/          # Maven wrapper
└── src/
    ├── main/
    │   ├── java/com/portfolio/my_portfolio/
    │   │   ├── MyPortfolioApplication.java
    │   │   ├── model/               # Entidades (Lombok @Data)
    │   │   │   ├── PersonalInfo.java
    │   │   │   ├── Skill.java
    │   │   │   ├── Education.java
    │   │   │   ├── Experience.java
    │   │   │   └── Project.java
    │   │   ├── repository/          # Interfaces + implementaciones JDBC
    │   │   │   ├── ISkillRepository.java
    │   │   │   ├── SkillRepositoryImpl.java
    │   │   │   └── ...
    │   │   ├── service/             # Interfaces + implementaciones
    │   │   │   ├── ISkillService.java
    │   │   │   ├── SkillServiceImpl.java
    │   │   │   └── ...
    │   │   └── rest/                # Controladores REST
    │   │       ├── SkillTestController.java
    │   │       ├── ProjectTestController.java
    │   │       ├── EducationTestController.java
    │   │       ├── ExperiencesTestController.java
    │   │       └── PersonalInfoTestController.java
    │   └── resources/
    │       ├── application.properties
    │       ├── schema.sql           # DDL: DROP + CREATE de las tablas
    │       └── data.sql             # Datos de ejemplo
    └── test/
        └── java/com/portfolio/my_portfolio/
            └── MyPortfolioApplicationTests.java
```

### Capas

- **`model`** — Clases POJO anotadas con Lombok (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`). Las columnas snake_case de la BD se mapean a campos camelCase.
- **`repository`** — Interfaz (`I*Repository`) + implementación (`*RepositoryImpl`) con `JdbcTemplate`. Operaciones `save` (insert con `GeneratedKeyHolder` o update según exista `id`), `findById`, `findAll` y `deleteById`.
- **`service`** — Interfaz (`I*Service`) + implementación (`*ServiceImpl`). Delega en el repositorio correspondiente.
- **`rest`** — Controladores `@RestController` que exponen las operaciones HTTP sobre las implementaciones de servicio.

---

## Endpoints de la API

Todos los endpoints siguen el mismo patrón. `404` se devuelve cuando el recurso no existe.

| Método | Ruta | Descripción | Respuesta |
|---|---|---|---|
| `GET` | `/all` | Lista todos los registros | `200` + array JSON |
| `GET` | `/{id}` | Obtiene un registro por ID | `200` / `404` |
| `POST` | `/` | Crea un registro | `201` + objeto creado |
| `DELETE` | `/{id}` | Elimina un registro por ID | `204` / `404` |

### Rutas base por recurso

| Recurso | Ruta base |
|---|---|
| Información personal | `/api/test/personal-info` |
| Habilidades | `/api/test/skills` |
| Educación | `/api/test/educations` |
| Experiencia | `/api/test/experiences` |
| Proyectos | `/api/test/projects` |

### Ejemplo completo (habilidades)

```
GET    /api/test/skills/all
GET    /api/test/skills/{id}
POST   /api/test/skills
DELETE /api/test/skills/{id}
```

```json
{
  "id": 1,
  "name": "Java",
  "levelPercentage": 80,
  "iconClass": "fab fa-java",
  "personalInfoId": 1
}
```

---

## Modelo de datos

Las tablas están definidas en `src/main/resources/schema.sql`. Todas las tablas hijas referencian a `personal_info` con `ON DELETE CASCADE`.

```
personal_info (id, first_name, last_name, title, profile_description,
               profile_image_url, years_of_experience, email, phone,
               linkedin_url, github_url)
      │
      ├── skills     (id, name, level_percentage, icon_class, personal_info_id)
      ├── educations (id, degree, institution, start_date, end_date, description, personal_info_id)
      ├── experiences(id, job_title, company_name, start_date, end_date, description, personal_info_id)
      └── projects   (id, title, description, image_url, project_url, personal_info_id)
```

| Tabla | Campos | Notas |
|---|---|---|
| `personal_info` | `id`, `first_name`, `last_name`, `title`, `profile_description`, `profile_image_url`, `years_of_experience`, `email`, `phone`, `linkedin_url`, `github_url` | `id` y `first_name`/`last_name`/`title`/`profile_description` obligatorios. |
| `skills` | `id`, `name`, `level_percentage`, `icon_class`, `personal_info_id` | `level_percentage` representa el dominio del 0 al 100. |
| `educations` | `id`, `degree`, `institution`, `start_date`, `end_date`, `description`, `personal_info_id` | `end_date` puede ser `NULL` (formación en curso). |
| `experiences` | `id`, `job_title`, `company_name`, `start_date`, `end_date`, `description`, `personal_info_id` | `end_date` `NULL` indica que el puesto es actual. |
| `projects` | `id`, `title`, `description`, `image_url`, `project_url`, `personal_info_id` | — |

---

## Datos de ejemplo

`src/main/resources/data.sql` inserta datos de muestra: 1 registro de información personal, 7 habilidades, 2 educaciones, 2 experiencias y 2 proyectos.

---

## Pruebas

```bash
./mvnw test
```

La suite actual (`MyPortfolioApplicationTests`) verifica la carga del contexto de Spring. Requiere que PostgreSQL esté levantado, ya que la inicialización de `schema.sql`/`data.sql` ocurre al arrancar el contexto de pruebas.

---

## Notas importantes

- **Los datos se reinician en cada arranque.** `schema.sql` empieza con `DROP TABLE IF EXISTS` y `spring.sql.init.mode=always` ejecuta ambos scripts en cada inicio, por lo que las tablas se recrean y `data.sql` vuelve a poblarlas. Los registros creados por la API se pierden al reiniciar la aplicación. Para conserving datos reales, cambia `spring.sql.init.mode` a `never` y Migra el esquema con una herramienta como Flyway o Liquibase.
- **Propiedades JPA sin efecto.** `application.properties` contiene `spring.jpa.hibernate.ddl-auto` y `spring.jpa.show-sql`, pero el proyecto usa `spring-boot-starter-data-jdbc` y no incluye JPA/Hibernate, por lo que esas propiedades no tienen ningún efecto. Conviene eliminarlas para evitar confusión.
- **Los controladores son scaffolding temporal.** Están nombrados `*TestController` y expuestos bajo `/api/test/`. Si la API va a usarse en producción, renómbralos (p. ej. `SkillController`) y cambia el prefijo a `/api/`.
- **Sin autenticación.** La API es pública y sin control de acceso. Añade Spring Security antes de exponerla.
- **Sin CORS configurado.** Si el frontend React se sirve en otro origen, hay que añadir una configuración de CORS.
- **Sin validación de entrada.** Los modelos no tienen Bean Validation (`@NotNull`, `@Size`, etc.); la validación de NOT NULL la hace PostgreSQL.
- **Sin DTOs ni manejo global de errores.** Los modelos se exponen directamente como contrato de la API y los errores se propagan como `ResponseStatusException`.

---

## Licencia

Proyecto personal, sin licencia definida.
