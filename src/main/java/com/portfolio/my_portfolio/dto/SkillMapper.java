package com.portfolio.my_portfolio.dto;

import com.portfolio.my_portfolio.model.Skill;

public class SkillMapper {
  public static SkillDto toDto(Skill skill) {
    if (skill == null) {
      return null;
    }

    SkillDto skillDto = new SkillDto();
    skillDto.setId(skill.getId());
    skillDto.setName(skill.getName());
    skillDto.setLevelPercentage(skill.getLevelPercentage());
    skillDto.setIconClass(skill.getIconClass());
    skillDto.setPersonalInfoId(skill.getPersonalInfoId());

    return skillDto;
  }

  public static Skill toSkill(SkillDto skillDto) {
    if (skillDto == null) {
      return null;
    }

    Skill skill = new Skill();
    skill.setId(skillDto.getId());
    skill.setName(skillDto.getName());
    skill.setLevelPercentage(skillDto.getLevelPercentage());
    skill.setIconClass(skillDto.getIconClass());
    skill.setPersonalInfoId(skillDto.getPersonalInfoId());

    return skill;
  }
}
