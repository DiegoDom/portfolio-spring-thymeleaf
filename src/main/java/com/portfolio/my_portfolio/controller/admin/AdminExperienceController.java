package com.portfolio.my_portfolio.controller.admin;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.my_portfolio.dto.ExperienceDto;
import com.portfolio.my_portfolio.dto.ExperienceMapper;
import com.portfolio.my_portfolio.model.Experience;
import com.portfolio.my_portfolio.service.ExperienceServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/experiences")
public class AdminExperienceController {

  private final ExperienceServiceImpl experienceService;
  private final PersonalInfoIdResolver personalInfoIdResolver;

  @GetMapping({ "", "/" })
  public String list(Model model) {
    model.addAttribute("experiences",
        experienceService.findAll().stream().map(ExperienceMapper::toDto).toList());
    return "admin/experiences/index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    ExperienceDto experience = new ExperienceDto();
    experience.setPersonalInfoId(personalInfoIdResolver.requireId());
    model.addAttribute("experience", experience);
    model.addAttribute("editMode", false);
    return "admin/experiences/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("experience") ExperienceDto experienceDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "admin/experiences/form";
    }

    experienceDto.setId(null);
    Experience experience = ExperienceMapper.toExperience(experienceDto);
    experience.setPersonalInfoId(personalInfoIdResolver.requireId());
    experienceService.save(experience);

    redirectAttributes.addFlashAttribute("success", "Experiencia creada correctamente.");
    return "redirect:/admin/experiences";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    ExperienceDto experience = ExperienceMapper.toDto(findOrThrow(id));
    model.addAttribute("experience", experience);
    model.addAttribute("editMode", true);
    return "admin/experiences/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
      @Valid @ModelAttribute("experience") ExperienceDto experienceDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    findOrThrow(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      return "admin/experiences/form";
    }

    experienceDto.setId(id);
    Experience experience = ExperienceMapper.toExperience(experienceDto);
    experience.setPersonalInfoId(personalInfoIdResolver.requireId());
    experienceService.save(experience);

    redirectAttributes.addFlashAttribute("success", "Experiencia actualizada correctamente.");
    return "redirect:/admin/experiences";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    findOrThrow(id);
    experienceService.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Experiencia eliminada correctamente.");
    return "redirect:/admin/experiences";
  }

  private Experience findOrThrow(Long id) {
    Optional<Experience> experience = experienceService.findById(id);
    if (experience.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Experiencia no encontrada con id: " + id);
    }
    return experience.get();
  }
}
