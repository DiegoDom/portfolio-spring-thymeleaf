package com.portfolio.my_portfolio.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.my_portfolio.dto.PersonalInfoMapper;
import com.portfolio.my_portfolio.service.IEducationService;
import com.portfolio.my_portfolio.service.IExperiencesService;
import com.portfolio.my_portfolio.service.IPersonalInfoService;
import com.portfolio.my_portfolio.service.IProjectService;
import com.portfolio.my_portfolio.service.ISkillService;

import lombok.RequiredArgsConstructor;

/**
 * Panel de administración. Todas las rutas viven bajo {@code /admin/**} para
 * poder protegerlas con Spring Security en el futuro.
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminHomeController {

  private final IPersonalInfoService personalInfoService;
  private final ISkillService skillService;
  private final IEducationService educationService;
  private final IExperiencesService experiencesService;
  private final IProjectService projectService;

  @GetMapping({ "", "/" })
  public String dashboard(Model model) {
    model.addAttribute("personalInfo",
        personalInfoService.find().map(PersonalInfoMapper::toDto).orElse(null));
    model.addAttribute("skillCount", skillService.findAll().size());
    model.addAttribute("educationCount", educationService.findAll().size());
    model.addAttribute("experienceCount", experiencesService.findAll().size());
    model.addAttribute("projectCount", projectService.findAll().size());
    return "admin/index";
  }
}
