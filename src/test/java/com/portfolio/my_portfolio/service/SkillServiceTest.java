package com.portfolio.my_portfolio.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import com.portfolio.my_portfolio.exception.ValidationException;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.repository.ISkillRepository;

class SkillServiceTest extends ServiceIntegrationTestSupport {

  private static final int SEEDED_SKILLS = 7;

  @Autowired
  private ISkillService skillService;

  @Autowired
  private ISkillRepository skillRepository;

  private Long personalInfoId;

  @BeforeEach
  void setUp() {
    personalInfoId = seededPersonalInfo().getId();
  }

  @Test
  void save_insertsNewSkillAndReadsBackAllColumns() {
    Skill saved = skillService.save(skill("TypeScript", 75, "fab fa-js"));

    assertThat(saved.getId()).isNotNull();

    Skill reloaded = skillRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getName()).isEqualTo("TypeScript");
    assertThat(reloaded.getLevelPercentage()).isEqualTo(75);
    assertThat(reloaded.getIconClass()).isEqualTo("fab fa-js");
    assertThat(reloaded.getPersonalInfoId()).isEqualTo(personalInfoId);
  }

  @Test
  void save_withNullLevelAndIcon_persistsNulls() {
    Skill saved = skillService.save(skill("Kotlin", null, null));

    Skill reloaded = skillRepository.findById(saved.getId()).orElseThrow();
    assertThat(reloaded.getLevelPercentage()).isNull();
    assertThat(reloaded.getIconClass()).isNull();
  }

  @Test
  void save_withExistingId_updatesWithoutInserting() {
    Skill created = skillService.save(skill("Java", 50, "fab fa-java"));

    created.setName("Java 21");
    created.setLevelPercentage(90);
    skillService.save(created);

    Skill reloaded = skillRepository.findById(created.getId()).orElseThrow();
    assertThat(reloaded.getName()).isEqualTo("Java 21");
    assertThat(reloaded.getLevelPercentage()).isEqualTo(90);
    assertThat(skillService.findAll()).hasSize(SEEDED_SKILLS + 1);
  }

  @Test
  void findById_returnsEmptyWhenNotExists() {
    assertThat(skillService.findById(9_999_999L)).isEmpty();
  }

  @Test
  void findAll_returnsSeededRowsPlusTheNewOne() {
    skillService.save(skill("TypeScript", 75, "fab fa-js"));

    assertThat(skillService.findAll()).hasSize(SEEDED_SKILLS + 1);
  }

  @Test
  void deleteById_removesTheRow() {
    Skill saved = skillService.save(skill("TypeScript", 75, "fab fa-js"));

    skillService.deleteById(saved.getId());

    assertThat(skillRepository.findById(saved.getId())).isEmpty();
  }

  @Test
  void deleteById_missingIdDoesNotThrow() {
    skillService.deleteById(9_999_999L);

    assertThat(skillService.findAll()).hasSize(SEEDED_SKILLS);
  }

  @Test
  void save_blankNameThrowsAndPersistsNothing() {
    ValidationException ex = assertThrows(ValidationException.class,
        () -> skillService.save(skill(" ", 50, null)));

    assertThat(ex.getBindingResult().getFieldError("name")).isNotNull();
    assertThat(skillRepository.findAll()).hasSize(SEEDED_SKILLS);
  }

  @Test
  void save_levelOutsideRangeThrows() {
    ValidationException ex = assertThrows(ValidationException.class,
        () -> skillService.save(skill("Java", 101, null)));

    assertThat(ex.getBindingResult().getFieldError("levelPercentage")).isNotNull();
  }

  @Test
  void save_withUnknownPersonalInfoFailsAtDatabaseLevel() {
    Skill orphan = new Skill(null, "Kotlin", 50, null, 9_999_999L);

    assertThatThrownBy(() -> skillService.save(orphan))
        .isInstanceOf(DataIntegrityViolationException.class);
  }

  private Skill skill(String name, Integer levelPercentage, String iconClass) {
    return new Skill(null, name, levelPercentage, iconClass, personalInfoId);
  }
}
