package com.portfolio.my_portfolio.controller.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.portfolio.my_portfolio.support.PostgresIntegrationTestSupport;

@AutoConfigureMockMvc
@WithMockUser(roles = "ADMIN")
public abstract class AdminMvcIntegrationTestSupport extends PostgresIntegrationTestSupport {

  @Autowired
  protected MockMvc mockMvc;
}
