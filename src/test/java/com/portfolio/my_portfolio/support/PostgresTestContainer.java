package com.portfolio.my_portfolio.support;

import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Contenedor PostgreSQL compartido por toda la suite de tests.
 *
 * Se arranca una sola vez por JVM y Ryuk lo detiene al terminar, de modo que
 * la base de datos de desarrollo (docker compose) nunca se toca.
 */
public final class PostgresTestContainer {

  public static final PostgreSQLContainer INSTANCE =
      new PostgreSQLContainer("postgres:16-alpine");

  static {
    INSTANCE.start();
  }

  private PostgresTestContainer() {
  }
}
