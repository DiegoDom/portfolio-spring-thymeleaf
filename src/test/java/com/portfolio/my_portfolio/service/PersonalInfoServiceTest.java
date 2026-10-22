package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.repository.IEducationRepository;
import com.portfolio.my_portfolio.repository.IExperienceRespository;
import com.portfolio.my_portfolio.repository.IPersonalInfoRepository;
import com.portfolio.my_portfolio.repository.IProjectRepository;
import com.portfolio.my_portfolio.repository.ISkillRepository;

class PersonalInfoServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_PERSONAL_INFO = 1;

  @Autowired
  private IPersonalInfoRepository personalInfoRepository;

  @Autowired
  private ISkillService skillService;

  @Autowired
  private IEducationService educationService;

  @Autowired
  private IExperiencesService experienceService;

  @Autowired
  private IProjectService projectService;

  @Autowired
  private ISkillRepository skillRepository;

  @Autowired
  private IEducationRepository educationRepository;

  @Autowired
  private IExperienceRespository experienceRepository;

  @Autowired
  private IProjectRepository projectRepository;

  @Test
  void save_insertsNewPersonalInfoAndReadsBackAllColumns() {
    PersonalInfo saved = personalInfoService.save(fullPersonalInfo());

    assertThat(saved.getId()).isNotNull();

    PersonalInfo reloaded = personalInfoRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getFirstName()).isEqualTo("Diego");
    assertThat(reloaded.getLastName()).isEqualTo("Dominguez");
    assertThat(reloaded.getTitle()).isEqualTo("Full Stack Developer");
    assertThat(reloaded.getProfileDescription()).isEqualTo("Apasionado por el desarrollo web.");
    assertThat(reloaded.getProfileImageUrl()).isEqualTo("img/profile.png");
    assertThat(reloaded.getYearsOfExperience()).isEqualTo(8);
    assertThat(reloaded.getEmail()).isEqualTo("diego@example.com");
    assertThat(reloaded.getPhone()).isEqualTo("+523348385911");
    assertThat(reloaded.getLinkedinUrl()).isEqualTo("https://www.linkedin.com/in/diegodom/");
    assertThat(reloaded.getGithubUrl()).isEqualTo("https://github.com/DiegoDom");
  }

  @Test
  void save_withNullOptionalFields_persistsNulls() {
    PersonalInfo saved = personalInfoService.save(new PersonalInfo(
        null, "Diego", "Dominguez", "Full Stack Developer", "Descripción",
        null, null, "diego@example.com", "+523348385911", null, null));

    PersonalInfo reloaded = personalInfoRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getProfileImageUrl()).isNull();
    assertThat(reloaded.getYearsOfExperience()).isNull();
    assertThat(reloaded.getLinkedinUrl()).isNull();
    assertThat(reloaded.getGithubUrl()).isNull();
  }

  @Test
  void save_withExistingId_updatesWithoutInserting() {
    PersonalInfo created = givenPersonalInfo();

    created.setLastName("Dominguez Actualizado");
    created.setTitle("Senior Full Stack Developer");
    created.setYearsOfExperience(6);
    created.setGithubUrl("https://github.com/DiegoDom");
    personalInfoService.save(created);

    PersonalInfo reloaded = personalInfoRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getLastName()).isEqualTo("Dominguez Actualizado");
    assertThat(reloaded.getTitle()).isEqualTo("Senior Full Stack Developer");
    assertThat(reloaded.getYearsOfExperience()).isEqualTo(6);
    assertThat(reloaded.getGithubUrl()).isEqualTo("https://github.com/DiegoDom");
    assertThat(personalInfoService.findAll()).hasSize(SEEDED_PERSONAL_INFO + 1);
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(personalInfoService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowPlusTheNewOne() {
    givenPersonalInfo();

    assertThat(personalInfoService.findAll()).hasSize(SEEDED_PERSONAL_INFO + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    PersonalInfo saved = givenPersonalInfo();

    personalInfoService.deleteById(saved.getId());

    assertThat(personalInfoRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void deleteById_missingIdDoesNotThrow() {
    personalInfoService.deleteById(9_999_999L);

    assertThat(personalInfoService.findAll()).hasSize(SEEDED_PERSONAL_INFO);
  }

  @Test
  void save_invalidEmailThrowsAndPersistsNothing() {
    PersonalInfo invalid = fullPersonalInfo();
    invalid.setEmail("not-an-email");

    ValidationException ex = assertThrows(ValidationException.class,
        () -> personalInfoService.save(invalid));

    assertThat(ex.getBindingResult().getFieldError("email")).isNotNull();
    assertThat(personalInfoRepository.findAll()).hasSize(SEEDED_PERSONAL_INFO);
  }

  @Test
  void deleteById_withChildren_cascadesToAllChildTables() {
    PersonalInfo parent = givenPersonalInfo();

    Skill skill = skillService.save(new Skill(null, "Kotlin", 50, null, parent.getId()));
    Education education = educationService.save(new Education(null, "Ingeniería", "Universidad XYZ",
        LocalDate.of(2015, 3, 1), null, null, parent.getId()));
    Experience experience = experienceService.save(new Experience(null, "Desarrollador", "Tech Solutions",
        LocalDate.of(2022, 1, 1), null, null, parent.getId()));
    Project project = projectService.save(new Project(null, "Portfolio", null, null, null, parent.getId()));

    personalInfoService.deleteById(parent.getId());

    assertThat(personalInfoRepository.findById(parent.getId())).isEmpty();
    assertThat(skillRepository.findById(skill.getId())).isEmpty();
    assertThat(educationRepository.findById(education.getId())).isEmpty();
    assertThat(experienceRepository.findById(experience.getId())).isEmpty();
    assertThat(projectRepository.findById(project.getId())).isEmpty();
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
