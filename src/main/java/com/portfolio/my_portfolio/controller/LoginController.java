package com.portfolio.my_portfolio.controller;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Página de inicio de sesión. El POST a /auth/login lo procesa Spring Security
 * (formLogin), no este controlador.
 */
@Controller
public class LoginController {

  @GetMapping("/auth/login")
  public String showLoginForm(Authentication authentication) {
    if (authentication != null
        && authentication.isAuthenticated()
        && !(authentication instanceof AnonymousAuthenticationToken)) {
      return "redirect:/admin";
    }
    return "auth/form-login";
  }
}
