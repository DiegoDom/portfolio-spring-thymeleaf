package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.repository.IExperienceRespository;

class ExperienceServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_EXPERIENCES = 2;

  @Autowired
  private IExperiencesService experienceService;

  @Autowired
  private IExperienceRespository experienceRepository;

  private Long personalInfoId;

  @BeforeEach
  void setUp() {
    personalInfoId = seededPersonalInfo().getId();
  }

  @Test
  void save_insertsNewExperienceAndReadsBackAllColumns() {
    Experience saved = experienceService.save(experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null));

    assertThat(saved.getId()).isNotNull();

    Experience reloaded = experienceRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getJobTitle()).isEqualTo("Desarrollador Backend");
    assertThat(reloaded.getCompanyName()).isEqualTo("Tech Solutions S.A.");
    assertThat(reloaded.getStartDate()).isEqualTo(LocalDate.of(2022, 1, 1));
    assertThat(reloaded.getEndDate()).isNull();
    assertThat(reloaded.getPersonalInfoId()).isEqualTo(personalInfoId);
  }

  @Test
  void save_withExistingId_updatesJobTitleAndCompanyName() {
    Experience created = experienceService.save(experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null));

    created.setJobTitle("Desarrollador Full Stack Senior");
    created.setCompanyName("Innovatech Labs");
    created.setEndDate(LocalDate.of(2023, 6, 30));
    experienceService.save(created);

    Experience reloaded = experienceRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getJobTitle()).isEqualTo("Desarrollador Full Stack Senior");
    assertThat(reloaded.getCompanyName()).isEqualTo("Innovatech Labs");
    assertThat(reloaded.getEndDate()).isEqualTo(LocalDate.of(2023, 6, 30));
    assertThat(experienceService.findAll()).hasSize(SEEDED_EXPERIENCES + 1);
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(experienceService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowsPlusTheNewOne() {
    experienceService.save(experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null));

    assertThat(experienceService.findAll()).hasSize(SEEDED_EXPERIENCES + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    Experience saved = experienceService.save(experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null));

    experienceService.deleteById(saved.getId());

    assertThat(experienceRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void deleteById_missingIdDoesNotThrow() {
    experienceService.deleteById(9_999_999L);

    assertThat(experienceService.findAll()).hasSize(SEEDED_EXPERIENCES);
  }

  @Test
  void save_blankJobTitleThrowsAndPersistsNothing() {
    Experience invalid = experience(" ", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null);

    ValidationException ex = assertThrows(ValidationException.class,
        () -> experienceService.save(invalid));

    assertThat(ex.getBindingResult().getFieldError("jobTitle")).isNotNull();
    assertThat(experienceRepository.findAll()).hasSize(SEEDED_EXPERIENCES);
  }

  @Test
  void save_futureStartDateThrows() {
    Experience invalid = experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.now().plusDays(1), null);

    ValidationException ex = assertThrows(ValidationException.class,
        () -> experienceService.save(invalid));

    assertThat(ex.getBindingResult().getFieldError("startDate")).isNotNull();
  }

  @Test
  void save_withUnknownPersonalInfoFailsAtDatabaseLevel() {
    Experience orphan = experience("Desarrollador Backend", "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1), null);
    orphan.setPersonalInfoId(9_999_999L);

    assertThatThrownBy(() -> experienceService.save(orphan))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  private Experience experience(String jobTitle, String companyName, LocalDate startDate, LocalDate endDate) {
    return new Experience(null, jobTitle, companyName, startDate, endDate,
        "Descripción de prueba", personalInfoId);
  }
}
