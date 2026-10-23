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
   * Devuelve el perfil único sembrado por data.sql. personal_info es un
   * singleton, así que los tests solo necesitan su id para la FK.
   */
  protected PersonalInfo seededPersonalInfo() {
    return personalInfoService.find()
        .orElseThrow(() -> new IllegalStateException("Seeded personal_info row is missing"));
  }
}
