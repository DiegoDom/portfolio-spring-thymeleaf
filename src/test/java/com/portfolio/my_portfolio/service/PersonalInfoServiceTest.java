package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.repository.IPersonalInfoRepository;

class PersonalInfoServiceTest extends ServiceIntegrationTestSupport {

  private static final Long SINGLETON_ID = 1L;

  @Autowired
  private IPersonalInfoRepository personalInfoRepository;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  void find_returnsSeededPersonalInfo() {
    PersonalInfo personalInfo = personalInfoService.find().orElseThrow();

    assertThat(personalInfo.getId()).isEqualTo(SINGLETON_ID);
    assertThat(personalInfo.getFirstName()).isEqualTo("Diego Alberto");
    assertThat(personalInfo.getLastName()).isEqualTo("Dominguez");
    assertThat(personalInfo.getTitle()).isEqualTo("Full Stack Developer");
    assertThat(personalInfo.getProfileDescription()).isEqualTo(
        "Apasionado por el desarrollo web con experiencia en Java, Spring Boot y React. "
            + "Disfruto construyendo soluciones robustas y escalables.");
    assertThat(personalInfo.getProfileImageUrl()).isEqualTo("/img/profile-placeholder.jpg");
    assertThat(personalInfo.getYearsOfExperience()).isEqualTo(5);
    assertThat(personalInfo.getEmail()).isEqualTo("juan.perez@example.com");
    assertThat(personalInfo.getPhone()).isEqualTo("+523348385911");
    assertThat(personalInfo.getLinkedinUrl()).isEqualTo("https://www.linkedin.com/in/diegodom/");
    assertThat(personalInfo.getGithubUrl()).isEqualTo("https://github.com/DiegoDom");
  }

  @Test
  void save_updatesSingletonWithoutInsertingAnotherRow() {
    PersonalInfo current = personalInfoService.find().orElseThrow();
    current.setLastName("Dominguez Actualizado");
    current.setTitle("Senior Full Stack Developer");
    current.setYearsOfExperience(6);
    current.setGithubUrl("https://github.com/DiegoDom");

    PersonalInfo saved = personalInfoService.save(current);

    assertThat(saved.getId()).isEqualTo(SINGLETON_ID);

    PersonalInfo reloaded = personalInfoRepository.find().orElseThrow();
    assertThat(reloaded.getLastName()).isEqualTo("Dominguez Actualizado");
    assertThat(reloaded.getTitle()).isEqualTo("Senior Full Stack Developer");
    assertThat(reloaded.getYearsOfExperience()).isEqualTo(6);
    assertThat(countPersonalInfoRows()).isEqualTo(1L);
  }

  @Test
  void save_withClientProvidedId_stillUsesSingletonId() {
    PersonalInfo personalInfo = fullPersonalInfo();
    personalInfo.setId(9_999_999L);

    PersonalInfo saved = personalInfoService.save(personalInfo);

    assertThat(saved.getId()).isEqualTo(SINGLETON_ID);
    assertThat(personalInfoRepository.find().orElseThrow().getFirstName()).isEqualTo("Diego");
    assertThat(countPersonalInfoRows()).isEqualTo(1L);
  }

  @Test
  void save_whenNoRowExists_insertsSingleton() {
    jdbcTemplate.update("DELETE FROM personal_info WHERE id = ?", SINGLETON_ID);

    assertThat(personalInfoService.find()).isEmpty();

    PersonalInfo saved = personalInfoService.save(fullPersonalInfo());

    assertThat(saved.getId()).isEqualTo(SINGLETON_ID);
    assertThat(personalInfoRepository.find().orElseThrow().getFirstName()).isEqualTo("Diego");
    assertThat(countPersonalInfoRows()).isEqualTo(1L);
  }

  @Test
  void save_withNullOptionalFields_persistsNulls() {
    PersonalInfo saved = personalInfoService.save(new PersonalInfo(
        null, "Diego", "Dominguez", "Full Stack Developer", "Descripción",
        null, null, "diego@example.com", "+523348385911", null, null));

    PersonalInfo reloaded = personalInfoRepository.find().orElseThrow();
    assertThat(reloaded.getId()).isEqualTo(SINGLETON_ID);
    assertThat(reloaded.getId()).isEqualTo(saved.getId());
    assertThat(reloaded.getProfileImageUrl()).isNull();
    assertThat(reloaded.getYearsOfExperience()).isNull();
    assertThat(reloaded.getLinkedinUrl()).isNull();
    assertThat(reloaded.getGithubUrl()).isNull();
  }

  @Test
  void save_invalidEmailThrowsAndPersistsNothing() {
    PersonalInfo original = personalInfoService.find().orElseThrow();

    PersonalInfo invalid = fullPersonalInfo();
    invalid.setEmail("not-an-email");

    ValidationException ex = assertThrows(ValidationException.class,
        () -> personalInfoService.save(invalid));

    assertThat(ex.getBindingResult().getFieldError("email")).isNotNull();

    PersonalInfo reloaded = personalInfoRepository.find().orElseThrow();
    assertThat(reloaded.getFirstName()).isEqualTo(original.getFirstName());
    assertThat(reloaded.getEmail()).isEqualTo(original.getEmail());
  }

  private long countPersonalInfoRows() {
    Long count = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM personal_info", Long.class);
    return count == null ? 0L : count;
  }

  private PersonalInfo fullPersonalInfo() {
    return new PersonalInfo(
        null,
        "Diego",
        "Dominguez",
        "Full Stack Developer",
        "Apasionado por el desarrollo web.",
        "img/profile.png",
        8,
        "diego@example.com",
        "+523348385911",
        "https://www.linkedin.com/in/diegodom/",
        "https://github.com/DiegoDom");
  }
}
