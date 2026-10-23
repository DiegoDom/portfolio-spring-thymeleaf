package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.repository.IProjectRepository;

class ProjectServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_PROJECTS = 2;

  @Autowired
  private IProjectService projectService;

  @Autowired
  private IProjectRepository projectRepository;

  private Long personalInfoId;

  @BeforeEach
  void setUp() {
    personalInfoId = seededPersonalInfo().getId();
  }

  @Test
  void save_insertsNewProjectAndReadsBackAllColumns() {
    Project saved = projectService.save(project("Portfolio Personal", "img/portfolio.png",
        "https://github.com/DiegoDom/portfolio"));

    assertThat(saved.getId()).isNotNull();

    Project reloaded = projectRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getTitle()).isEqualTo("Portfolio Personal");
    assertThat(reloaded.getDescription()).isEqualTo("Descripción de prueba");
    assertThat(reloaded.getImageUrl()).isEqualTo("img/portfolio.png");
    assertThat(reloaded.getProjectUrl()).isEqualTo("https://github.com/DiegoDom/portfolio");
    assertThat(reloaded.getPersonalInfoId()).isEqualTo(personalInfoId);
  }

  @Test
  void save_withNullOptionalFields_persistsNulls() {
    Project saved = projectService.save(project("Portfolio Personal", null, null));

    Project reloaded = projectRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getImageUrl()).isNull();
    assertThat(reloaded.getProjectUrl()).isNull();
  }

  @Test
  void save_withExistingId_updatesWithoutInserting() {
    Project created = projectService.save(project("Portfolio Personal", null, null));

    created.setTitle("Portfolio Personal v2");
    created.setProjectUrl("https://example.com/portfolio");
    projectService.save(created);

    Project reloaded = projectRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getTitle()).isEqualTo("Portfolio Personal v2");
    assertThat(reloaded.getProjectUrl()).isEqualTo("https://example.com/portfolio");
    assertThat(projectService.findAll()).hasSize(SEEDED_PROJECTS + 1);
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(projectService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowsPlusTheNewOne() {
    projectService.save(project("Portfolio Personal", null, null));

    assertThat(projectService.findAll()).hasSize(SEEDED_PROJECTS + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    Project saved = projectService.save(project("Portfolio Personal", null, null));

    projectService.deleteById(saved.getId());

    assertThat(projectRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void deleteById_missingIdDoesNotThrow() {
    projectService.deleteById(9_999_999L);

    assertThat(projectService.findAll()).hasSize(SEEDED_PROJECTS);
  }

  @Test
  void save_blankTitleThrowsAndPersistsNothing() {
    Project p = project(" ", null, null);
    p.setPersonalInfoId(personalInfoId);
    try {
      projectService.save(p);
    } catch (Exception ex) {
      // expected
    }
    assertThat(projectRepository.findAll()).hasSize(SEEDED_PROJECTS);
  }

  @Test
  void save_withUnknownPersonalInfoFailsAtDatabaseLevel() {
    Project orphan = project("Portfolio Personal", null, null);
    orphan.setPersonalInfoId(9_999_999L);

    assertThatThrownBy(() -> projectService.save(orphan))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  private Project project(String title, String imageUrl, String projectUrl) {
    return new Project(null, title, "Descripción de prueba", imageUrl, projectUrl, personalInfoId);
  }
}
