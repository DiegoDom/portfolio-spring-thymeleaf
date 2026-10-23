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
import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.repository.IEducationRepository;

class EducationServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_EDUCATIONS = 2;

  @Autowired
  private IEducationService educationService;

  @Autowired
  private IEducationRepository educationRepository;

  private Long personalInfoId;

  @BeforeEach
  void setUp() {
    personalInfoId = seededPersonalInfo().getId();
  }

  @Test
  void save_insertsNewEducationAndReadsBackAllColumns() {
    Education saved = educationService.save(education("Ingeniería", "Universidad XYZ",
        LocalDate.of(2015, 3, 1), null));

    assertThat(saved.getId()).isNotNull();

    Education reloaded = educationRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getDegree()).isEqualTo("Ingeniería");
    assertThat(reloaded.getInstitution()).isEqualTo("Universidad XYZ");
    assertThat(reloaded.getStartDate()).isEqualTo(LocalDate.of(2015, 3, 1));
    assertThat(reloaded.getEndDate()).isNull();
    assertThat(reloaded.getPersonalInfoId()).isEqualTo(personalInfoId);
  }

  @Test
  void save_withExistingId_updatesWithoutSwappingDegreeAndInstitution() {
    Education created = educationService.save(education("Ingeniería", "Universidad XYZ",
        LocalDate.of(2015, 3, 1), null));

    created.setDegree("Maestría");
    created.setInstitution("Universidad ABC");
    created.setEndDate(LocalDate.of(2020, 12, 15));
    educationService.save(created);

    Education reloaded = educationRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getDegree()).isEqualTo("Maestría");
    assertThat(reloaded.getInstitution()).isEqualTo("Universidad ABC");
    assertThat(reloaded.getEndDate()).isEqualTo(LocalDate.of(2020, 12, 15));
    assertThat(educationService.findAll()).hasSize(SEEDED_EDUCATIONS + 1);
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(educationService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowsPlusTheNewOne() {
    educationService.save(education("Ingeniería", "Universidad XYZ", LocalDate.of(2015, 3, 1), null));

    assertThat(educationService.findAll()).hasSize(SEEDED_EDUCATIONS + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    Education saved = educationService.save(education("Ingeniería", "Universidad XYZ",
        LocalDate.of(2015, 3, 1), null));

    educationService.deleteById(saved.getId());

    assertThat(educationRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void deleteById_missingIdDoesNotThrow() {
    educationService.deleteById(9_999_999L);

    assertThat(educationService.findAll()).hasSize(SEEDED_EDUCATIONS);
  }

  @Test
  void save_futureStartDateThrowsAndPersistsNothing() {
    Education invalid = education("Ingeniería", "Universidad XYZ",
        LocalDate.now().plusDays(1), null);

    ValidationException ex = assertThrows(ValidationException.class,
        () -> educationService.save(invalid));

    assertThat(ex.getBindingResult().getFieldError("startDate")).isNotNull();
    assertThat(educationRepository.findAll()).hasSize(SEEDED_EDUCATIONS);
  }

  @Test
  void save_withUnknownPersonalInfoFailsAtDatabaseLevel() {
    Education orphan = education("Ingeniería", "Universidad XYZ",
        LocalDate.of(2015, 3, 1), null);
    orphan.setPersonalInfoId(9_999_999L);

    assertThatThrownBy(() -> educationService.save(orphan))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  private Education education(String degree, String institution, LocalDate startDate, LocalDate endDate) {
    return new Education(null, degree, institution, startDate, endDate,
        "Descripción de prueba", personalInfoId);
  }
}
