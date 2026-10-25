package com.portfolio.my_portfolio.controller.admin;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;

class AdminViewsTest extends AdminMvcIntegrationTestSupport {

  @Test
  void publicIndexLoads() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"));
  }

  @Test
  void dashboardLoadsWithSeededCounts() throws Exception {
    mockMvc.perform(get("/admin"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/index"))
        .andExpect(model().attribute("skillCount", 7))
        .andExpect(model().attribute("educationCount", 2))
        .andExpect(model().attribute("experienceCount", 2))
        .andExpect(model().attribute("projectCount", 2));
  }

  @Test
  void listViewsLoad() throws Exception {
    mockMvc.perform(get("/admin/skills"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/skills/index"));

    mockMvc.perform(get("/admin/educations"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/educations/index"));

    mockMvc.perform(get("/admin/experiences"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/experiences/index"));

    mockMvc.perform(get("/admin/projects"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/projects/index"));
  }

  @Test
  void createFormsLoad() throws Exception {
    mockMvc.perform(get("/admin/skills/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/skills/form"))
        .andExpect(model().attribute("editMode", false));

    mockMvc.perform(get("/admin/educations/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/educations/form"))
        .andExpect(model().attribute("editMode", false));

    mockMvc.perform(get("/admin/experiences/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/experiences/form"))
        .andExpect(model().attribute("editMode", false));

    mockMvc.perform(get("/admin/projects/new"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/projects/form"))
        .andExpect(model().attribute("editMode", false));
  }

  @Test
  void editFormsLoadForSeededRows() throws Exception {
    mockMvc.perform(get("/admin/skills/{id}/edit", 1L))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/skills/form"))
        .andExpect(model().attribute("editMode", true));

    mockMvc.perform(get("/admin/educations/{id}/edit", 1L))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/educations/form"))
        .andExpect(model().attribute("editMode", true));

    mockMvc.perform(get("/admin/experiences/{id}/edit", 1L))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/experiences/form"))
        .andExpect(model().attribute("editMode", true));

    mockMvc.perform(get("/admin/projects/{id}/edit", 1L))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/projects/form"))
        .andExpect(model().attribute("editMode", true));
  }

  @Test
  void personalInfoFormLoads() throws Exception {
    mockMvc.perform(get("/admin/personal-info"))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/personal-info/form"));
  }

  @Test
  void missingRecordsReturnNotFound() throws Exception {
    mockMvc.perform(get("/admin/skills/{id}/edit", 9_999_999L))
        .andExpect(status().isNotFound());

    mockMvc.perform(get("/admin/educations/{id}/edit", 9_999_999L))
        .andExpect(status().isNotFound());

    mockMvc.perform(get("/admin/experiences/{id}/edit", 9_999_999L))
        .andExpect(status().isNotFound());

    mockMvc.perform(get("/admin/projects/{id}/edit", 9_999_999L))
        .andExpect(status().isNotFound());
  }
}
