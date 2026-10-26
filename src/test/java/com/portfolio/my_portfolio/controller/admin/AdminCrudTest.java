package com.portfolio.my_portfolio.controller.admin;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.flash;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.service.IEducationService;
import com.portfolio.my_portfolio.service.IExperiencesService;
import com.portfolio.my_portfolio.service.IPersonalInfoService;
import com.portfolio.my_portfolio.service.IProjectService;
import com.portfolio.my_portfolio.service.ISkillService;

class AdminCrudTest extends AdminMvcIntegrationTestSupport {

  @Autowired
  private ISkillService skillService;

  @Autowired
  private IEducationService educationService;

  @Autowired
  private IExperiencesService experienceService;

  @Autowired
  private IProjectService projectService;

  @Autowired
  private IPersonalInfoService personalInfoService;

  @Test
  void skillCrudFlow() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(post("/admin/skills").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("name", "Kotlin Admin")
        .param("levelPercentage", "65")
        .param("iconClass", "fab fa-java")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/skills"))
        .andExpect(flash().attributeExists("success"));

    Skill created = skillService.findAll().stream()
        .filter(skill -> "Kotlin Admin".equals(skill.getName()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No se creó la habilidad"));
    assertThat(created.getId()).isNotNull();

    mockMvc.perform(post("/admin/skills/{id}", created.getId()).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("name", "Kotlin Admin 2")
        .param("levelPercentage", "90")
        .param("iconClass", "fab fa-kotlin")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/skills"));

    Skill updated = skillService.findById(created.getId()).orElseThrow();
    assertThat(updated.getName()).isEqualTo("Kotlin Admin 2");
    assertThat(updated.getLevelPercentage()).isEqualTo(90);

    mockMvc.perform(post("/admin/skills/{id}/delete", created.getId()).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/skills"));

    assertThat(skillService.findById(created.getId())).isEmpty();
  }

  @Test
  void educationCrudFlow() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(post("/admin/educations").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("degree", "Curso Admin")
        .param("institution", "Academia Admin")
        .param("startDate", "2021-01-01")
        .param("endDate", "2021-06-30")
        .param("description", "Formación de prueba")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/educations"))
        .andExpect(flash().attributeExists("success"));

    Education created = educationService.findAll().stream()
        .filter(education -> "Curso Admin".equals(education.getDegree()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No se creó la formación"));
    assertThat(created.getId()).isNotNull();

    mockMvc.perform(post("/admin/educations/{id}", created.getId()).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("degree", "Curso Admin 2")
        .param("institution", "Academia Admin 2")
        .param("startDate", "2021-02-01")
        .param("endDate", "2021-07-31")
        .param("description", "Formación actualizada")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/educations"));

    Education updated = educationService.findById(created.getId()).orElseThrow();
    assertThat(updated.getDegree()).isEqualTo("Curso Admin 2");
    assertThat(updated.getInstitution()).isEqualTo("Academia Admin 2");

    mockMvc.perform(post("/admin/educations/{id}/delete", created.getId()).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/educations"));

    assertThat(educationService.findById(created.getId())).isEmpty();
  }

  @Test
  void experienceCrudFlow() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(post("/admin/experiences").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("jobTitle", "Puesto Admin")
        .param("companyName", "Empresa Admin")
        .param("startDate", "2022-01-01")
        .param("description", "Experiencia de prueba")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/experiences"))
        .andExpect(flash().attributeExists("success"));

    Experience created = experienceService.findAll().stream()
        .filter(experience -> "Puesto Admin".equals(experience.getJobTitle()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No se creó la experiencia"));
    assertThat(created.getId()).isNotNull();

    mockMvc.perform(post("/admin/experiences/{id}", created.getId()).with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("jobTitle", "Puesto Admin 2")
        .param("companyName", "Empresa Admin 2")
        .param("startDate", "2022-02-01")
        .param("description", "Experiencia actualizada")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/experiences"));

    Experience updated = experienceService.findById(created.getId()).orElseThrow();
    assertThat(updated.getJobTitle()).isEqualTo("Puesto Admin 2");
    assertThat(updated.getCompanyName()).isEqualTo("Empresa Admin 2");

    mockMvc.perform(post("/admin/experiences/{id}/delete", created.getId()).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/experiences"));

    assertThat(experienceService.findById(created.getId())).isEmpty();
  }

  @Test
  void projectCrudFlow() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(multipart("/admin/projects").with(csrf())
        .param("title", "Proyecto Admin")
        .param("description", "Descripción de prueba")
        .param("projectUrl", "https://example.com/admin")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/projects"))
        .andExpect(flash().attributeExists("success"));

    Project created = projectService.findAll().stream()
        .filter(project -> "Proyecto Admin".equals(project.getTitle()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No se creó el proyecto"));
    assertThat(created.getId()).isNotNull();

    mockMvc.perform(multipart("/admin/projects/{id}", created.getId()).with(csrf())
        .param("title", "Proyecto Admin 2")
        .param("description", "Descripción actualizada")
        .param("projectUrl", "https://example.com/admin-2")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/projects"));

    Project updated = projectService.findById(created.getId()).orElseThrow();
    assertThat(updated.getTitle()).isEqualTo("Proyecto Admin 2");
    assertThat(updated.getProjectUrl()).isEqualTo("https://example.com/admin-2");

    mockMvc.perform(post("/admin/projects/{id}/delete", created.getId()).with(csrf()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/projects"));

    assertThat(projectService.findById(created.getId())).isEmpty();
  }

  @Test
  void projectCanBeCreatedWithoutImageOrProjectUrl() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(multipart("/admin/projects").with(csrf())
        .param("title", "Proyecto sin URL")
        .param("description", "Sin imagen ni URL de proyecto")
        .param("projectUrl", "")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/projects"));

    Project created = projectService.findAll().stream()
        .filter(project -> "Proyecto sin URL".equals(project.getTitle()))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No se creó el proyecto sin URL"));
    assertThat(created.getImageUrl()).isNull();
    assertThat(created.getProjectUrl()).isBlank();
  }

  @Test
  void editingSeededProjectWithoutNewImageKeepsItsRelativeImageUrl() throws Exception {
    Long personalInfoId = personalInfoId();
    Project seeded = projectService.findAll().stream()
        .filter(project -> project.getImageUrl() != null && project.getImageUrl().startsWith("/img/"))
        .findFirst()
        .orElseThrow(() -> new AssertionError("No hay proyectos sembrados con imagen local"));

    mockMvc.perform(multipart("/admin/projects/{id}", seeded.getId()).with(csrf())
        .param("title", seeded.getTitle())
        .param("description", seeded.getDescription())
        .param("projectUrl", "https://example.com/seeded")
        .param("imageUrl", seeded.getImageUrl())
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/projects"));

    Project updated = projectService.findById(seeded.getId()).orElseThrow();
    assertThat(updated.getImageUrl()).isEqualTo(seeded.getImageUrl());
    assertThat(updated.getProjectUrl()).isEqualTo("https://example.com/seeded");
  }

  @Test
  void personalInfoCanBeUpdated() throws Exception {
    mockMvc.perform(post("/admin/personal-info").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("firstName", "Admin")
        .param("lastName", "Portfolio")
        .param("title", "Tester de Portfolio")
        .param("profileDescription", "Descripción de perfil de prueba.")
        .param("profileImageUrl", "/img/profile-placeholder.jpg")
        .param("yearsOfExperience", "8")
        .param("email", "admin@example.com")
        .param("phone", "+34123456789")
        .param("linkedinUrl", "https://www.linkedin.com/in/admin")
        .param("githubUrl", "https://github.com/admin"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/personal-info"))
        .andExpect(flash().attributeExists("success"));

    var personalInfo = personalInfoService.find().orElseThrow();
    assertThat(personalInfo.getFirstName()).isEqualTo("Admin");
    assertThat(personalInfo.getEmail()).isEqualTo("admin@example.com");
    assertThat(personalInfo.getYearsOfExperience()).isEqualTo(8);
  }

  @Test
  void createIgnoresClientProvidedId() throws Exception {
    Long personalInfoId = personalInfoId();
    Skill original = skillService.findAll().get(0);
    String originalName = original.getName();

    mockMvc.perform(post("/admin/skills").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("id", original.getId().toString())
        .param("name", "Habilidad con id manipulado")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/admin/skills"));

    assertThat(skillService.findById(original.getId()).orElseThrow().getName()).isEqualTo(originalName);
    assertThat(skillService.findAll())
        .anyMatch(skill -> "Habilidad con id manipulado".equals(skill.getName()));
  }

  @Test
  void validationErrorsReturnTheForm() throws Exception {
    Long personalInfoId = personalInfoId();

    mockMvc.perform(post("/admin/skills").with(csrf())
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .param("name", " ")
        .param("personalInfoId", personalInfoId.toString()))
        .andExpect(status().isOk())
        .andExpect(view().name("admin/skills/form"))
        .andExpect(model().attribute("editMode", false))
        .andExpect(model().attribute("formErrors", true))
        .andExpect(model().attributeHasFieldErrors("skill", "name"));
  }

  @Test
  void deletingMissingRecordReturnsNotFound() throws Exception {
    mockMvc.perform(post("/admin/skills/{id}/delete", 9_999_999L).with(csrf()))
        .andExpect(status().isNotFound());
  }

  private Long personalInfoId() {
    return personalInfoService.find()
        .orElseThrow(() -> new IllegalStateException("Falta la fila de personal_info sembrada"))
        .getId();
  }
}
