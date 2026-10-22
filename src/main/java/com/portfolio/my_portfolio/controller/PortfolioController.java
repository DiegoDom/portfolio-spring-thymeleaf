package com.portfolio.my_portfolio.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.my_portfolio.model.PersonalInfo;
import com.portfolio.my_portfolio.service.IPersonalInfoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/personal-info")
public class PortfolioController {
  private final IPersonalInfoService personalInfoService;

  @GetMapping("/form")
  public String showForm(Model model) {
    model.addAttribute("personalInfo", new PersonalInfo());
    return "form";
  }

  @PostMapping("/save")
  public String save(@ModelAttribute("personalInfo") PersonalInfo personalInfo) {
    personalInfoService.save(personalInfo);
    return "redirect:/";
  }
}
