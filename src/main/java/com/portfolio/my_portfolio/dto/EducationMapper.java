package com.portfolio.my_portfolio.dto;

import com.portfolio.my_portfolio.model.Education;

public class EducationMapper {
  public static EducationDto toDto(Education education) {
    if (education == null) {
      return null;
    }

    EducationDto educationDto = new EducationDto();
    educationDto.setId(education.getId());
    educationDto.setDegree(education.getDegree());
    educationDto.setInstitution(education.getInstitution());
    educationDto.setStartDate(education.getStartDate());
    educationDto.setEndDate(education.getEndDate());
    educationDto.setDescription(education.getDescription());
    educationDto.setPersonalInfoId(education.getPersonalInfoId());

    return educationDto;
  }

  public static Education toEducation(EducationDto educationDto) {
    if (educationDto == null) {
      return null;
    }

    Education education = new Education();
    education.setId(educationDto.getId());
    education.setDegree(educationDto.getDegree());
    education.setInstitution(educationDto.getInstitution());
    education.setStartDate(educationDto.getStartDate());
    education.setEndDate(educationDto.getEndDate());
    education.setDescription(educationDto.getDescription());
    education.setPersonalInfoId(educationDto.getPersonalInfoId());

    return education;
  }
}
