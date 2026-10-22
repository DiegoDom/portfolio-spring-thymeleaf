package com.portfolio.my_portfolio.support;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.postgresql.PostgreSQLContainer;

/**
 * Base para tests de integración contra PostgreSQL real.
 *
 * <ul>
 * <li>@ServiceConnection reemplaza el datasource de application.properties por
 * el del contenedor: no hace falta docker compose ni tocar la BD de desarrollo.</li>
 * <li>@Transactional hace rollback después de cada método, así que no se
 * necesita @DirtiesContext ni reseed entre tests. schema.sql y data.sql se
 * ejecutan una sola vez por contexto, contra el contenedor.</li>
 * </ul>
 */
@SpringBootTest
@Transactional
public abstract class PostgresIntegrationTestSupport {

  @ServiceConnection
  static final PostgreSQLContainer POSTGRES = PostgresTestContainer.INSTANCE;
}
