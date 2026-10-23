package com.portfolio.my_portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.service.IEducationService;
import com.portfolio.my_portfolio.service.IExperiencesService;
import com.portfolio.my_portfolio.service.IPersonalInfoService;
import com.portfolio.my_portfolio.service.IProjectService;
import com.portfolio.my_portfolio.service.ISkillService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class IndexController {
  private final IPersonalInfoService personalInfoService;
  private final IEducationService educationService;
  private final ISkillService skillService;
  private final IExperiencesService experiencesService;
  private final IProjectService projectService;

  @GetMapping("/")
  public String showIndex(Model model) {
    PersonalInfo personalInfo = personalInfoService.find().orElse(null);

    if (personalInfo != null) {
      model.addAttribute("personalInfo", personalInfo);
    }
    model.addAttribute("initials", buildInitials(personalInfo));

    model.addAttribute("educations", educationService.findAll());
    model.addAttribute("skills", skillService.findAll());
    model.addAttribute("experiences", experiencesService.findAll());
    model.addAttribute("projects", projectService.findAll());
    return "index";
  }

  private String buildInitials(PersonalInfo personalInfo) {
    if (personalInfo == null) {
      return "DD";
    }
    String initials = firstLetter(personalInfo.getFirstName()) + firstLetter(personalInfo.getLastName());
    return initials.isBlank() ? "DD" : initials.toUpperCase();
  }

  private String firstLetter(String value) {
    return value == null || value.isBlank() ? "" : value.substring(0, 1);
  }
}
