package com.portfolio.my_portfolio.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

class ModelValidationTest {

  private static ValidatorFactory validatorFactory;
  private static Validator validator;

  @BeforeAll
  static void setUp() {
    validatorFactory = Validation.buildDefaultValidatorFactory();
    validator = validatorFactory.getValidator();
  }

  @AfterAll
  static void tearDown() {
    validatorFactory.close();
  }

  @Test
  void validModelsHaveNoViolations() {
    assertThat(validator.validate(validPersonalInfo())).isEmpty();
    assertThat(validator.validate(validSkill())).isEmpty();
    assertThat(validator.validate(validEducation())).isEmpty();
    assertThat(validator.validate(validExperience())).isEmpty();
    assertThat(validator.validate(validProject())).isEmpty();
  }

  @Test
  void personalInfoAcceptsLowercaseEmail() {
    PersonalInfo personalInfo = validPersonalInfo();
    personalInfo.setEmail("diego.dominguez@example.com");

    assertThat(validator.validate(personalInfo)).isEmpty();
  }

  @Test
  void personalInfoRejectsBlankRequiredFields() {
    PersonalInfo personalInfo = validPersonalInfo();
    personalInfo.setFirstName(" ");
    personalInfo.setLastName(null);
    personalInfo.setTitle("");
    personalInfo.setProfileDescription("  ");
    personalInfo.setEmail(null);
    personalInfo.setPhone(null);

    assertThat(invalidProperties(personalInfo))
        .contains("firstName", "lastName", "title", "profileDescription", "email", "phone");
  }

  @Test
  void personalInfoRejectsInvalidValues() {
    PersonalInfo personalInfo = validPersonalInfo();
    personalInfo.setEmail("not-an-email");
    personalInfo.setYearsOfExperience(-1);
    personalInfo.setFirstName("a".repeat(101));
    personalInfo.setLinkedinUrl("not a url");

    assertThat(invalidProperties(personalInfo))
        .contains("email", "yearsOfExperience", "firstName", "linkedinUrl");
  }

  @Test
  void skillRejectsMissingRequiredFields() {
    assertThat(invalidProperties(new Skill())).contains("name", "personalInfoId");
  }

  @Test
  void skillRejectsLevelOutsideZeroToOneHundred() {
    Skill skill = validSkill();
    skill.setLevelPercentage(101);

    assertThat(invalidProperties(skill)).contains("levelPercentage");

    skill.setLevelPercentage(-1);
    assertThat(invalidProperties(skill)).contains("levelPercentage");
  }

  @Test
  void educationRejectsMissingRequiredFields() {
    assertThat(invalidProperties(new Education())).contains("degree", "institution", "startDate", "personalInfoId");
  }

  @Test
  void experienceRejectsMissingRequiredFields() {
    assertThat(invalidProperties(new Experience()))
        .contains("jobTitle", "companyName", "startDate", "personalInfoId");
  }

  @Test
  void projectRejectsMissingRequiredFields() {
    assertThat(invalidProperties(new Project())).contains("title", "personalInfoId");
  }

  private static Set<String> invalidProperties(Object model) {
    return validator.validate(model).stream()
        .map(violation -> violation.getPropertyPath().toString())
        .collect(Collectors.toSet());
  }

  private static PersonalInfo validPersonalInfo() {
    return new PersonalInfo(
        null,
        "Diego",
        "Dominguez",
        "Full Stack Developer",
        "Apasionado por el desarrollo web.",
        "img/profile-placeholder.jpg",
        5,
        "diego.dominguez@example.com",
        "+523348385911",
        "https://www.linkedin.com/in/diegodom/",
        "https://github.com/DiegoDom");
  }

  private static Skill validSkill() {
    return new Skill(null, "Java", 80, "fab fa-java", 1L);
  }

  private static Education validEducation() {
    return new Education(
        null,
        "Ingeniería en Sistemas",
        "Universidad XYZ",
        LocalDate.of(2015, 3, 1),
        null,
        null,
        1L);
  }

  private static Experience validExperience() {
    return new Experience(
        null,
        "Desarrollador Full Stack Senior",
        "Tech Solutions S.A.",
        LocalDate.of(2022, 1, 1),
        null,
        null,
        1L);
  }

  private static Project validProject() {
    return new Project(null, "Portfolio Personal", null, "img/portfolio-project.jpg", null, 1L);
  }
}
