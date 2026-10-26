# AGENTS.md

Single-module Spring Boot app for a personal portfolio. Java 21, Spring Boot 4.1.1, PostgreSQL. Three surfaces:

- **Public site** at `/` (Thymeleaf).
- **Admin panel** at `/admin/**` (Thymeleaf + Bootstrap 5), protected by Spring Security form login at `/auth/login`.
- **REST API** at `/api/test/**`.

## Layout

- Base package `com.portfolio.my_portfolio`; layers are `model` -> `dto` -> `repository` -> `service` -> `controller` / `rest`.
- Persistence is **hand-written SQL** with `JdbcTemplate` + per-entity `RowMapper`. The `spring-boot-starter-data-jdbc` dependency only brings in the datasource/JDBC infra — there is no ORM and no JPA/Hibernate on the classpath.
- `controller/IndexController` serves the public `/`; `controller/LoginController` serves `GET /auth/login`; `controller/admin/*` serves the panel; `rest/*` serves the API.
- Auth: `config/WebSecurityConfig` (filter chain, BCrypt) + `service/UserServiceImpl`, the single DB-backed `UserDetailsService`.
- Templates live in `src/main/resources/templates`: `index.html`, `auth/form-login.html`, `admin/layout.html` + `admin/**`, `fragments/**`, `errors/**`.
- Entrypoint: `src/main/java/com/portfolio/my_portfolio/MyPortfolioApplication.java`.
- Tests: `src/test/java/com/portfolio/my_portfolio/` with `controller/admin/Admin*`, `controller/LoginControllerTest`, `service/*ServiceTest` (including `UserServiceTest`), `model/ModelValidationTest`, and `support/PostgresIntegrationTestSupport`.

## Commands

Use `./mvnw` (wrapper pins Maven 3.10.0); on Windows the shell equivalent is `.\mvnw.cmd`.

```bash
docker compose up -d      # start dev Postgres (service `db`, postgres:16-alpine, localhost:5432,
                          # db=portfolio user=portfolio_user pw=portfolio_secret)
docker compose ps         # wait for "healthy"
./mvnw spring-boot:run    # app on http://localhost:8080 (devtools is on the classpath -> auto-restart)
./mvnw test               # requires Docker: tests use Testcontainers PostgreSQL
./mvnw test -Dtest=AdminCrudTest                 # one test class
./mvnw test -Dtest=AdminCrudTest#skillCrudFlow  # one test method
./mvnw clean package && java -jar target/my-portfolio-0.0.1-SNAPSHOT.jar
```

There is no lint/format/checkstyle plugin and no CI config; `./mvnw test` is the verification gate.

## Gotchas

1. **The dev DB is wiped on every app start.** `spring.sql.init.mode=always` plus `schema.sql` starting with `DROP TABLE IF EXISTS` recreates tables and `data.sql` reseeds (1 personal_info, 7 skills, 2 educations, 2 experiences, 2 projects, 1 user). Data created through the admin panel or API does not survive a restart.
2. **Tests use Testcontainers, not the dev DB.** `PostgresIntegrationTestSupport` uses `@ServiceConnection` so the datasource points at a throwaway PostgreSQL container; each test runs in a transaction with rollback. Docker must be running, but `docker compose` is not needed for tests. `./mvnw test` does **not** reset the development database.
3. **`spring.jpa.properties.hibernate.dialect` is dead config** — no JPA/Hibernate dependency exists. Remove or ignore it; do not add JPA for new features.
4. **Row mapping is manual.** Adding/renaming a column means editing `src/main/resources/schema.sql` *and* the `RowMapper` in the matching `*RepositoryImpl` (snake_case -> camelCase). Nothing is regenerated.
5. **`save()` is insert-or-update keyed on `id == null`.** A body/form carrying an `id` updates that row instead of inserting. Admin create handlers explicitly clear the DTO id before saving so a crafted POST cannot update an existing row. Inserts use `GeneratedKeyHolder` with `new String[]{"id"}`. `personal_info` is a singleton: `PersonalInfoRepositoryImpl.save` always writes id 1.
6. **SQL parameters are positional** — the argument order of `jdbcTemplate.update(...)` / `ps.setX(n, ...)` must match the column order of the statement, and that order differs per entity (`educations` lists `institution, degree` in SQL; `data.sql` lists `degree, institution`). Do not reorder one side alone. Keep both the INSERT column list and the UPDATE `SET` clause in `institution, degree` order and bind them the same way.
7. **Repository `deleteById` silently succeeds for a missing id.** The 404 is produced in the controller by calling `findById` first (REST and admin both do this) — keep that check when adding endpoints.
8. **Lombok is required to compile** (`optional` dependency, but the compiler plugin pins the annotation processor path). If getters/setters are missing at compile time, annotation processing is not active (IDE: enable it).
9. **The admin panel is protected by Spring Security.** `/admin/**` requires authentication; form login is `GET /auth/login` (template `auth/form-login.html`) and logout is `POST /logout`. `UserServiceImpl` is the only `UserDetailsService` (table `users`, BCrypt hashes) — do not add a second one or an `AuthenticationManagerBuilder.configure(...)` hook: Spring auto-configures the provider, and injecting `IUserService` into `WebSecurityConfig` creates a circular dependency. Seed credentials are `admin` / `1Qwerty` (dev only). `/` stays public; CSRF is ignored only for `/api/test/**`. Admin MockMvc tests get `@WithMockUser` from `AdminMvcIntegrationTestSupport` and need `.with(csrf())` on POSTs.
10. **Child admin forms carry a hidden `personalInfoId`.** The form is pre-filled with the singleton profile id, but the controller overwrites it with `PersonalInfoIdResolver.requireId()` before saving. If the profile is missing, the resolver throws `409 Conflict`. Never trust the hidden value.
11. **`ProjectDto.imageUrl` is `@Size(max = 500)`, not `@URL`**, so relative paths like `/img/foo.jpg` are valid. `projectUrl` is `@URL`. `data.sql` image paths start with `/` so they resolve correctly from nested admin pages; keep the leading slash. `FileStorageService` returns `/img/{uuid}{ext}` and writes to `src/main/resources/static/img` — dev-only; don't upload files in tests (it pollutes the source tree).
12. **`POST` routes have no trailing slash.** `/admin/skills` works; `/admin/skills/` falls through to static-resource handling (Spring Boot 4) and 404s. Same for `/api/test/...`.
13. **MVC forms use `novalidate`** so server-side Bean Validation messages render next to fields via `th:errorclass` / `th:errors`. The controller adds `formErrors=true` to show the top alert and sets `editMode` for create/edit. DTO and model validation messages are in Spanish; keep both in sync when adding fields.
14. **Date inputs use ISO `yyyy-MM-dd`.** `EducationDto`/`ExperienceDto` annotate `startDate`/`endDate` with `@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)` for `<input type="date">`.
15. **The profile is edit-only.** There is no create/delete for `personal_info`; `GET/POST /admin/personal-info` only.

## Conventions to match

- Interface/impl naming: `I<Name>Repository` / `I<Name>Service` + `<Name>RepositoryImpl` / `<Name>ServiceImpl`. Note the existing repository typo `IExperienceRespository` (missing "o") — keep it unless renaming the whole pair.
- Admin CRUD controllers (skills, educations, experiences, projects) inject the concrete `*ServiceImpl`; the profile controller, `IndexController`, and `AdminHomeController` use `I*Service`. Services inject concrete `*RepositoryImpl`; constructors are Lombok `@RequiredArgsConstructor` (match the file you edit).
- Models are Lombok `@Data @AllArgsConstructor @NoArgsConstructor` POJOs with Bean Validation. DTOs are Lombok `@Data @NoArgsConstructor` with Bean Validation and static mappers. REST controllers validate DTOs with `@Valid`; services validate models again.
- Missing records are reported with `ResponseStatusException(HttpStatus.NOT_FOUND, ...)`; admin messages are in Spanish.
- Public routes stay in `controller/`; admin routes go in `controller/admin/` under `/admin/...`.
- Admin templates extend `admin/layout` via `th:replace="~{admin/layout :: layout(title, active, ~{::content})}"` and put the page body in a `th:fragment="content"` element. Use Bootstrap 5 classes and Spanish UI copy.
- `User` is the only model implementing `UserDetails`; passwords are BCrypt-hashed in `UserServiceImpl.save` (already-hashed values are not re-encoded) and never exposed by `UserMapper.toDto`.
- POST mappings omit the trailing slash.
- Indentation is mixed (2-space in most files, 4-space in some `PersonalInfo*`/model files) and there is no formatter config — match the file you edit.

## Known gaps (do not assume they are implemented)

No user-management UI or registration, and no `role` column (`User.getAuthorities()` hardcodes `ROLE_ADMIN`). No CORS config, no migrations (schema is `DROP`+`CREATE` on every boot), no production file storage. REST controllers are still named `*TestController`, mounted under `/api/test/` as public scaffolding, and exempt from CSRF. `README.md` (Spanish) documents the endpoints, data model, admin panel, authentication, and these gaps in more detail.
