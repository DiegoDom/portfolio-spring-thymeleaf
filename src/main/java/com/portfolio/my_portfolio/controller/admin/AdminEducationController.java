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

import com.portfolio.my_portfolio.dto.EducationDto;
import com.portfolio.my_portfolio.dto.EducationMapper;
import com.portfolio.my_portfolio.model.Education;
import com.portfolio.my_portfolio.service.EducationServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/educations")
public class AdminEducationController {

  private final EducationServiceImpl educationService;
  private final PersonalInfoIdResolver personalInfoIdResolver;

  @GetMapping({ "", "/" })
  public String list(Model model) {
    model.addAttribute("educations",
        educationService.findAll().stream().map(EducationMapper::toDto).toList());
    return "admin/educations/index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    EducationDto education = new EducationDto();
    education.setPersonalInfoId(personalInfoIdResolver.requireId());
    model.addAttribute("education", education);
    model.addAttribute("editMode", false);
    return "admin/educations/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("education") EducationDto educationDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "admin/educations/form";
    }

    educationDto.setId(null);
    Education education = EducationMapper.toEducation(educationDto);
    education.setPersonalInfoId(personalInfoIdResolver.requireId());
    educationService.save(education);

    redirectAttributes.addFlashAttribute("success", "Formación creada correctamente.");
    return "redirect:/admin/educations";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    EducationDto education = EducationMapper.toDto(findOrThrow(id));
    model.addAttribute("education", education);
    model.addAttribute("editMode", true);
    return "admin/educations/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
      @Valid @ModelAttribute("education") EducationDto educationDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    findOrThrow(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      return "admin/educations/form";
    }

    educationDto.setId(id);
    Education education = EducationMapper.toEducation(educationDto);
    education.setPersonalInfoId(personalInfoIdResolver.requireId());
    educationService.save(education);

    redirectAttributes.addFlashAttribute("success", "Formación actualizada correctamente.");
    return "redirect:/admin/educations";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    findOrThrow(id);
    educationService.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Formación eliminada correctamente.");
    return "redirect:/admin/educations";
  }

  private Education findOrThrow(Long id) {
    Optional<Education> education = educationService.findById(id);
    if (education.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Formación no encontrada con id: " + id);
    }
    return education.get();
  }
}
