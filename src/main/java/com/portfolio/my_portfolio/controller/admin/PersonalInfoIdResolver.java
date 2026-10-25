package com.portfolio.my_portfolio.controller.admin;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.portfolio.my_portfolio.service.IPersonalInfoService;

import lombok.RequiredArgsConstructor;

/**
 * Resuelve el id del perfil personal, que es un recurso singleton.
 *
 * <p>Todos los recursos hijos (habilidades, formación, experiencia y proyectos)
 * necesitan una FK a {@code personal_info}. Este componente centraliza esa
 * resolución para que los controladores no dupliquen la lógica.</p>
 */
@Component
@RequiredArgsConstructor
public class PersonalInfoIdResolver {

  private final IPersonalInfoService personalInfoService;

  public Long requireId() {
    return personalInfoService.find()
        .map(personalInfo -> personalInfo.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
            "La información personal no está configurada"));
  }
}
