package com.portfolio.my_portfolio.controller;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.my_portfolio.support.PostgresIntegrationTestSupport;

@AutoConfigureMockMvc
class LoginControllerTest extends PostgresIntegrationTestSupport {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void loginPageIsPublicAndRendersTheForm() throws Exception {
    mockMvc.perform(get("/auth/login"))
        .andExpect(status().isOk())
        .andExpect(view().name("auth/form-login"))
        .andExpect(content().string(containsString("Iniciar sesión")))
        .andExpect(content().string(containsString("name=\"_csrf\"")));
  }

  @Test
  void adminRouteRedirectsAnonymousUsersToLogin() throws Exception {
    mockMvc.perform(get("/admin"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login"));
  }

  @Test
  void validCredentialsLogInAndRedirectToAdmin() throws Exception {
    mockMvc.perform(post("/auth/login").with(csrf())
        .param("username", "admin")
        .param("password", "1Qwerty"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin"));
  }

  @Test
  void invalidCredentialsRedirectToLoginWithError() throws Exception {
    mockMvc.perform(post("/auth/login").with(csrf())
        .param("username", "admin")
        .param("password", "incorrecta"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login?error"));
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void logoutRedirectsToLoginWithLogoutMessage() throws Exception {
    mockMvc.perform(post("/logout").with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/auth/login?logout"));

    mockMvc.perform(get("/auth/login?logout"))
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Has cerrado sesión correctamente.")));
  }
}
