package com.portfolio.my_portfolio.dto;

import com.portfolio.my_portfolio.model.PersonalInfo;

public class PersonalInfoMapper {
  public static PersonalInfoDto toDto(PersonalInfo personalInfo) {
    if (personalInfo == null) {
      return null;
    }

    PersonalInfoDto personalInfoDto = new PersonalInfoDto();
    personalInfoDto.setId(personalInfo.getId());
    personalInfoDto.setFirstName(personalInfo.getFirstName());
    personalInfoDto.setLastName(personalInfo.getLastName());
    personalInfoDto.setTitle(personalInfo.getTitle());
    personalInfoDto.setProfileDescription(personalInfo.getProfileDescription());
    personalInfoDto.setProfileImageUrl(personalInfo.getProfileImageUrl());
    personalInfoDto.setYearsOfExperience(personalInfo.getYearsOfExperience());
    personalInfoDto.setEmail(personalInfo.getEmail());
    personalInfoDto.setPhone(personalInfo.getPhone());
    personalInfoDto.setLinkedinUrl(personalInfo.getLinkedinUrl());
    personalInfoDto.setGithubUrl(personalInfo.getGithubUrl());

    return personalInfoDto;
  }

  public static PersonalInfo toPersonalInfo(PersonalInfoDto personalInfoDto) {
    if (personalInfoDto == null) {
      return null;
    }

    PersonalInfo personalInfo = new PersonalInfo();
    personalInfo.setId(personalInfoDto.getId());
    personalInfo.setFirstName(personalInfoDto.getFirstName());
    personalInfo.setLastName(personalInfoDto.getLastName());
    personalInfo.setTitle(personalInfoDto.getTitle());
    personalInfo.setProfileDescription(personalInfoDto.getProfileDescription());
    personalInfo.setProfileImageUrl(personalInfoDto.getProfileImageUrl());
    personalInfo.setYearsOfExperience(personalInfoDto.getYearsOfExperience());
    personalInfo.setEmail(personalInfoDto.getEmail());
    personalInfo.setPhone(personalInfoDto.getPhone());
    personalInfo.setLinkedinUrl(personalInfoDto.getLinkedinUrl());
    personalInfo.setGithubUrl(personalInfoDto.getGithubUrl());

    return personalInfo;
  }
}
