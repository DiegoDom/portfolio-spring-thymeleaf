package com.portfolio.my_portfolio.controller.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.my_portfolio.dto.PersonalInfoDto;
import com.portfolio.my_portfolio.dto.PersonalInfoMapper;
import com.portfolio.my_portfolio.service.IPersonalInfoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/personal-info")
public class AdminPersonalInfoController {

  private final IPersonalInfoService personalInfoService;

  @GetMapping({ "", "/" })
  public String showForm(Model model) {
    PersonalInfoDto personalInfo = personalInfoService.find()
        .map(PersonalInfoMapper::toDto)
        .orElseGet(PersonalInfoDto::new);
    model.addAttribute("personalInfo", personalInfo);
    return "admin/personal-info/form";
  }

  @PostMapping
  public String save(@Valid @ModelAttribute("personalInfo") PersonalInfoDto personalInfoDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("formErrors", true);
      return "admin/personal-info/form";
    }

    personalInfoService.save(PersonalInfoMapper.toPersonalInfo(personalInfoDto));
    redirectAttributes.addFlashAttribute("success", "Información personal actualizada correctamente.");
    return "redirect:/admin/personal-info";
  }
}
