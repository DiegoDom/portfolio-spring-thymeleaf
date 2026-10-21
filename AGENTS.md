# AGENTS.md

Single-module Spring Boot REST API for a personal portfolio. Java 21, Spring Boot 4.1.1, PostgreSQL.

## Layout

- Base package `com.portfolio.my_portfolio`, layers `model` -> `repository` -> `service` -> `rest`.
- Persistence is **hand-written SQL** with `JdbcTemplate` + per-entity `RowMapper`. The `spring-boot-starter-data-jdbc` dependency only brings in the datasource/JDBC infra — there is no ORM and no JPA/Hibernate on the classpath.
- Entrypoint: `src/main/java/com/portfolio/my_portfolio/MyPortfolioApplication.java`.
- Only one real test class: `src/test/java/com/portfolio/my_portfolio/MyPortfolioApplicationTests.java`.

## Commands

Use `./mvnw` (wrapper pins Maven 3.10.0); a system `mvn` also exists but is not the pinned version.

```bash
docker compose up -d      # start Postgres (service `db`, postgres:16-alpine, localhost:5432,
                          # db=portfolio user=portfolio_user pw=portfolio_secret)
docker compose ps         # wait for "healthy"
./mvnw spring-boot:run    # app on http://localhost:8080 (devtools is on the classpath -> auto-restart)
./mvnw test               # the only verification gate: no lint/format/checkstyle plugin, no CI config
./mvnw test -Dtest=MyPortfolioApplicationTests   # single test
./mvnw clean package && java -jar target/my-portfolio-0.0.1-SNAPSHOT.jar
```

## Gotchas

1. **The DB is wiped on every app start *and* on every test run.** `spring.sql.init.mode=always` plus `schema.sql` starting with `DROP TABLE IF EXISTS` means tables are recreated and `data.sql` reseeds (1 personal_info, 7 skills, 2 educations, 2 experiences, 2 projects). Data created through the API does not survive a restart.
2. **Tests need Postgres running** and hit the *same* dev datasource — there is no test profile, Testcontainers, or embedded DB. `./mvnw test` therefore resets the development database (verified: tables are recreated and reseeded by the test context).
3. **`spring.jpa.properties.hibernate.dialect` in `application.properties` is dead config** — no JPA/Hibernate dependency exists. `README.md`'s config table is stale about this (it still lists `spring.jpa.hibernate.ddl-auto` / `spring.jpa.show-sql`, already removed); trust `application.properties` over the README.
4. **Row mapping is manual.** Adding/renaming a column means editing `src/main/resources/schema.sql` *and* the `RowMapper` in the matching `*RepositoryImpl` (snake_case -> camelCase). Nothing is regenerated.
5. **`save()` is insert-or-update keyed on `id == null`.** A POST body carrying an `id` updates that row instead of inserting. Inserts use `GeneratedKeyHolder` with `new String[]{"id"}` to read the generated key.
6. **SQL parameters are positional** — the argument order of `jdbcTemplate.update(...)` / `ps.setX(n, ...)` must match the column order of the statement, and that order differs per entity (`educations` lists `institution, degree`; `data.sql` lists `degree, institution`). Do not reorder one side alone.
7. **Known live bug:** the working-tree `EducationRepositoryImpl.save()` UPDATE binds `(degree, institution)` while its `SET` clause is `institution = ?, degree = ?`, so an education update swaps the two columns. Verified by API round-trip. Fix by swapping the two arguments back.
8. **Repository `deleteById` silently succeeds for a missing id.** The 404 is produced in the controller by calling `findById` first — keep that check when adding endpoints.
9. **Lombok is required to compile** (`optional` dependency, but the compiler plugin pins the annotation processor path). If getters/setters are missing at compile time, annotation processing is not active (IDE: enable it).

## Conventions to match

- Interface/impl naming: `I<Name>Repository` / `I<Name>Service` + `<Name>RepositoryImpl` / `<Name>ServiceImpl`. Note the existing typo `IExperienceRespository` (missing "o") — keep the name unless renaming the whole pair.
- Controllers inject the **concrete** `*ServiceImpl`, not the `I*Service` interface (deliberate; see commit "FIx: implementations all from service").
- Repository impls use Lombok `@RequiredArgsConstructor` for constructor injection; services and controllers use explicit constructors.
- Models are Lombok `@Data @AllArgsConstructor @NoArgsConstructor` POJOs and are exposed directly as the API contract — no DTOs, no Bean Validation (NOT NULL is enforced by Postgres).
- Missing records are reported with `ResponseStatusException(HttpStatus.NOT_FOUND, ...)`.
- Controllers are named `*TestController` and mounted under `/api/test/...` — that "Test" is scaffolding, **not** JUnit. Base paths: `/api/test/{personal-info,skills,educations,experiences,projects}`, each exposing `GET /all`, `GET /{id}`, `POST /`, `DELETE /{id}`.
- Indentation is mixed (2-space in most files, 4-space in the `PersonalInfo*` files) and there is no formatter config — match the file you edit.

## Known gaps (do not assume they are implemented)

No authentication, no CORS config, no input validation, no DTOs/global error handling, no migrations (schema is `DROP`+`CREATE` on every boot). `README.md` (Spanish) documents the endpoints, data model, and these gaps in more detail.
