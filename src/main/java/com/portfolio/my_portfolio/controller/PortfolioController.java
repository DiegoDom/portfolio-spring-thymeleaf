package com.portfolio.my_portfolio.controller;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.portfolio.my_portfolio.dto.PersonalInfoDto;
import com.portfolio.my_portfolio.dto.PersonalInfoMapper;
import com.portfolio.my_portfolio.service.IPersonalInfoService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/personal-info")
public class PortfolioController {
  private final IPersonalInfoService personalInfoService;

  @GetMapping("/form")
  public String showForm(Model model) {
    PersonalInfoDto personalInfo = personalInfoService.find()
        .map(PersonalInfoMapper::toDto)
        .orElseGet(PersonalInfoDto::new);
    model.addAttribute("personalInfo", personalInfo);
    return "form";
  }

  @PostMapping("/save")
  public String save(@Valid @ModelAttribute("personalInfo") PersonalInfoDto personalInfoDto, BindingResult bindingResult) {
    if (bindingResult.hasErrors()) {
      return "form";
    }
    personalInfoService.save(PersonalInfoMapper.toPersonalInfo(personalInfoDto));
    return "redirect:/";
  }
}

