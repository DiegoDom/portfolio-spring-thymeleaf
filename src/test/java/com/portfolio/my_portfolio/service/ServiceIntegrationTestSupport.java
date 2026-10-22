package com.portfolio.my_portfolio.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.support.PostgresIntegrationTestSupport;

/**
 * Base para los tests de integración de la capa de servicio.
 */
abstract class ServiceIntegrationTestSupport extends PostgresIntegrationTestSupport {

  @Autowired
  protected IPersonalInfoService personalInfoService;

  /**
   * Crea un personal_info fresco por test en lugar de depender del id 1 del
   * seed. El rollback de @Transactional lo elimina al terminar el método.
   */
  protected PersonalInfo givenPersonalInfo() {
    return personalInfoService.save(new PersonalInfo(
        null,
        "Test",
        "User",
        "Developer",
        "Descripción de prueba",
        null,
        1,
        "test.user@example.com",
        "+525551234567",
        null,
        null));
  }
}
