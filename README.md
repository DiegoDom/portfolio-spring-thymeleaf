# My Portfolio — Sitio público, panel de administración y API REST

Aplicación web para gestionar y mostrar un portfolio personal: datos del perfil, habilidades, formación académica, experiencia laboral y proyectos.

Incluye tres superficies:

- **Sitio público** en `/`, renderizado con Thymeleaf.
- **Panel de administración** en `/admin/**`, con vistas y formularios para el CRUD completo.
- **API REST** en `/api/test/**`, útil para pruebas y para el consumo desde otros clientes.

Construida con **Spring Boot 4.1.1** y **Java 21**, con persistencia en **PostgreSQL** mediante **Spring Data JDBC** (`JdbcTemplate`), sin ORM.

---

## Características

- Sitio público con perfil, habilidades, formación, experiencia y proyectos.
- Panel de administración en español, con Bootstrap 5 y Bootstrap Icons.
- CRUD de habilidades, formación, experiencia y proyectos; el perfil personal es un singleton editable.
- Validación con Bean Validation y mensajes de error en español.
- API REST para los cinco recursos.
- Persistencia relacional con SQL explícito (`JdbcTemplate`) y mapeo manual de filas (`RowMapper`).
- Esquema y datos de ejemplo inicializados automáticamente por Spring Boot.
- Tests de integración con PostgreSQL real mediante Testcontainers y MockMvc.
- Base de datos reproducible en un solo comando con Docker Compose.
- Lombok para reducir el código boilerplate de los modelos.

---

## Stack tecnológico

| Componente | Versión / detalle |
|---|---|
| Java | 21 |
| Spring Boot | 4.1.1 |
| Persistencia | `spring-boot-starter-data-jdbc` (`JdbcTemplate`) |
| Web | `spring-boot-starter-webmvc` + Thymeleaf |
| UI | Bootstrap 5 y Bootstrap Icons (CDN) |
| Validación | `spring-boot-starter-validation` (Bean Validation) |
| Base de datos | PostgreSQL 16 (Alpine) |
| Driver | `org.postgresql:postgresql` |
| Tests | `spring-boot-starter-test`, Testcontainers, MockMvc |
| Lombok | `org.projectlombok:lombok` (opcional) |
| Build | Maven (con wrapper `mvnw`) |
| DevTools | `spring-boot-devtools` (recarga en caliente) |

---

## Requisitos previos

- **Java 21** o superior.
- **Docker** con **Docker Compose** (para levantar PostgreSQL y para ejecutar los tests).
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
Sitio público:  http://localhost:8080/
Panel admin:    http://localhost:8080/admin
API REST:       http://localhost:8080/api/test
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
  -d '{"title":"Mi proyecto","description":"Descripción del proyecto","imageUrl":"/img/proyecto.jpg","projectUrl":"https://github.com/DiegoDom","personalInfoId":1}'
```

```bash
curl -X DELETE http://localhost:8080/api/test/projects/1
```

---

## Panel de administración

Todas las rutas del panel viven bajo `/admin/**`. **Todavía no hay Spring Security**: el panel es público y se agrupa bajo ese prefijo para poder protegerlo más adelante. El sitio público (`/`) no depende del panel.

| Método | Ruta | Descripción |
|---|---|---|
| `GET` | `/admin` | Dashboard con contadores y accesos rápidos |
| `GET` | `/admin/personal-info` | Formulario del perfil (singleton) |
| `POST` | `/admin/personal-info` | Guarda el perfil |
| `GET` | `/admin/skills` | Listado de habilidades |
| `GET` | `/admin/skills/new` | Formulario de creación |
| `POST` | `/admin/skills` | Crea una habilidad |
| `GET` | `/admin/skills/{id}/edit` | Formulario de edición |
| `POST` | `/admin/skills/{id}` | Actualiza una habilidad |
| `POST` | `/admin/skills/{id}/delete` | Elimina una habilidad |

Las mismas rutas se repiten para `educations`, `experiences` y `projects`.

Detalles de comportamiento:

- El perfil personal es un **singleton**: solo se edita, no se crea ni se elimina.
- Los recursos hijos envían `personalInfoId` en un campo oculto; el controlador lo reemplaza por el id real del perfil antes de guardar.
- Los formularios usan `novalidate` para que los mensajes en español de Bean Validation se muestren junto a cada campo.
- Los `POST` de creación se mapean **sin barra final**: `/admin/skills` funciona; `/admin/skills/` no.
- Las imágenes de proyectos se suben a `src/main/resources/static/img` (configurable con `file.upload.dir`) y se sirven como `/img/...`. Es una solución pensada para desarrollo; en producción conviene usar almacenamiento externo.
- `imageUrl` acepta tanto rutas relativas (`/img/...`) como URLs completas, con un máximo de 500 caracteres. `projectUrl` sí se valida como URL.

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
file.upload.dir=src/main/resources/static/img
```

| Propiedad | Descripción |
|---|---|
| `spring.datasource.url` | URL de conexión a PostgreSQL. |
| `spring.datasource.username` / `password` | Credenciales de la base de datos. |
| `spring.datasource.hikari.maximum-pool-size` | Tamaño máximo del pool de conexiones (10). |
| `spring.datasource.hikari.connection-timeout` | Tiempo de espera para obtener una conexión (30 s). |
| `spring.sql.init.mode=always` | Ejecuta `schema.sql` y `data.sql` en cada arranque. |
| `file.upload.dir` | Directorio donde se guardan las imágenes subidas desde el panel. |

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
    │   │   ├── model/               # Entidades (Lombok @Data + Bean Validation)
    │   │   │   ├── PersonalInfo.java
    │   │   │   ├── Skill.java
    │   │   │   ├── Education.java
    │   │   │   ├── Experience.java
    │   │   │   └── Project.java
    │   │   ├── dto/                 # DTOs y mappers para vistas/API
    │   │   ├── repository/          # Interfaces + implementaciones JDBC
    │   │   │   ├── ISkillRepository.java
    │   │   │   ├── SkillRepositoryImpl.java
    │   │   │   └── ...
    │   │   ├── service/             # Interfaces + implementaciones
    │   │   │   ├── ISkillService.java
    │   │   │   ├── SkillServiceImpl.java
    │   │   │   ├── FileStorageService.java
    │   │   │   └── ...
    │   │   ├── controller/          # Vistas públicas
    │   │   │   ├── IndexController.java
    │   │   │   └── admin/           # Panel bajo /admin/**
    │   │   │       ├── AdminHomeController.java
    │   │   │       ├── AdminPersonalInfoController.java
    │   │   │       ├── AdminSkillController.java
    │   │   │       ├── AdminEducationController.java
    │   │   │       ├── AdminExperienceController.java
    │   │   │       ├── AdminProjectController.java
    │   │   │       └── PersonalInfoIdResolver.java
    │   │   ├── rest/                # Controladores REST
    │   │   │   ├── SkillTestController.java
    │   │   │   ├── ProjectTestController.java
    │   │   │   ├── EducationTestController.java
    │   │   │   ├── ExperiencesTestController.java
    │   │   │   └── PersonalInfoTestController.java
    │   │   └── exception/           # ValidationException y handler global
    │   └── resources/
    │       ├── application.properties
    │       ├── schema.sql           # DDL: DROP + CREATE de las tablas
    │       ├── data.sql             # Datos de ejemplo
    │       ├── static/img/          # Imágenes subidas desde el panel
    │       └── templates/
    │           ├── index.html       # Sitio público
    │           ├── admin/           # Layout, dashboard y CRUDs del panel
    │           ├── fragments/       # Fragmentos compartidos del sitio público
    │           └── errors/          # Páginas de error
    └── test/
        └── java/com/portfolio/my_portfolio/
            ├── controller/admin/    # Tests MockMvc del panel
            ├── model/               # Validación de modelos
            ├── service/             # Tests de integración de servicios
            └── support/             # Testcontainers + PostgreSQL
```

### Capas

- **`model`** — Clases POJO anotadas con Lombok (`@Data`, `@AllArgsConstructor`, `@NoArgsConstructor`) y Bean Validation. Las columnas snake_case de la BD se mapean a campos camelCase.
- **`dto`** — DTOs y mappers estáticos entre modelos y formularios/vistas.
- **`repository`** — Interfaz (`I*Repository`) + implementación (`*RepositoryImpl`) con `JdbcTemplate`. Operaciones `save` (insert con `GeneratedKeyHolder` o update según exista `id`), `findById`, `findAll` y `deleteById`.
- **`service`** — Interfaz (`I*Service`) + implementación (`*ServiceImpl`). Valida con Bean Validation, delega en el repositorio y centraliza el almacenamiento de archivos.
- **`controller`** — Controladores MVC de Thymeleaf. El sitio público usa `IndexController`; el panel usa los controladores de `controller/admin`.
- **`rest`** — Controladores `@RestController` que exponen las operaciones HTTP sobre las implementaciones de servicio.

---

## API REST

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

> Los `POST` se mapean sin barra final: `/api/test/skills` funciona; `/api/test/skills/` no.

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
| `personal_info` | `id`, `first_name`, `last_name`, `title`, `profile_description`, `profile_image_url`, `years_of_experience`, `email`, `phone`, `linkedin_url`, `github_url` | `id` y `first_name`/`last_name`/`title`/`profile_description` obligatorios. Es un singleton. |
| `skills` | `id`, `name`, `level_percentage`, `icon_class`, `personal_info_id` | `level_percentage` representa el dominio del 0 al 100. |
| `educations` | `id`, `degree`, `institution`, `start_date`, `end_date`, `description`, `personal_info_id` | `end_date` puede ser `NULL` (formación en curso). |
| `experiences` | `id`, `job_title`, `company_name`, `start_date`, `end_date`, `description`, `personal_info_id` | `end_date` `NULL` indica que el puesto es actual. |
| `projects` | `id`, `title`, `description`, `image_url`, `project_url`, `personal_info_id` | — |

---

## Datos de ejemplo

`src/main/resources/data.sql` inserta datos de muestra: 1 registro de información personal, 7 habilidades, 2 educaciones, 2 experiencias y 2 proyectos.

Las rutas de las imágenes sembradas empiezan por `/` (por ejemplo, `/img/profile-placeholder.jpg`) para que funcionen tanto en el sitio público como en las vistas anidadas del panel.

---

## Pruebas

```bash
./mvnw test
```

La suite usa **Testcontainers** con PostgreSQL 16 (`PostgresIntegrationTestSupport` + `@ServiceConnection`), así que **no toca la base de datos de desarrollo**. Cada test de integración corre dentro de una transacción con rollback.

Incluye:

- `MyPortfolioApplicationTests` — carga del contexto.
- `ModelValidationTest` — anotaciones de Bean Validation de los modelos, sin base de datos.
- `Service*Test` — integración de los servicios contra PostgreSQL.
- `AdminViewsTest` — carga de `/` y de las vistas del panel con MockMvc.
- `AdminCrudTest` — flujos de creación, actualización y borrado de todos los recursos, validación y 404.

Docker debe estar en marcha para ejecutar los tests.

---

## Notas importantes

- **Los datos se reinician en cada arranque.** `schema.sql` empieza con `DROP TABLE IF EXISTS` y `spring.sql.init.mode=always` ejecuta ambos scripts en cada inicio, por lo que las tablas se recrean y `data.sql` vuelve a poblarlas. Los registros creados desde el panel o la API se pierden al reiniciar. Para conservar datos reales, cambia `spring.sql.init.mode` a `never` y migra el esquema con una herramienta como Flyway o Liquibase.
- **El panel no tiene autenticación todavía.** `/admin/**` está agrupado para poder protegerlo con Spring Security, pero actualmente cualquiera puede acceder. Añade seguridad antes de exponerlo.
- **`spring.jpa.properties.hibernate.dialect` no tiene efecto.** El proyecto usa `spring-boot-starter-data-jdbc` y no incluye JPA/Hibernate; esa propiedad es configuración muerta.
- **Sin CORS configurado.** Si un frontend se sirve en otro origen, hay que añadir una configuración de CORS.
- **Validación de entrada.** Los DTOs y modelos usan Bean Validation (`@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Min`, `@Max`, `@URL`, `@PastOrPresent`) con mensajes en español. Los controladores MVC re-renderizan el formulario con los errores; la API responde `400 Bad Request`. Las restricciones `NOT NULL` de PostgreSQL siguen actuando como última defensa.
- **Sin migraciones.** El esquema se recrea con `DROP` + `CREATE` en cada arranque.
- **Subida de imágenes para desarrollo.** `FileStorageService` escribe en `src/main/resources/static/img`; en un JAR empaquetado o en producción conviene sustituirlo por almacenamiento externo.

---

## Licencia

Proyecto personal, sin licencia definida.
