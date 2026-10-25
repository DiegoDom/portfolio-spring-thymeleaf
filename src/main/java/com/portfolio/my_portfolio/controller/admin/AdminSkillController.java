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

import com.portfolio.my_portfolio.dto.SkillDto;
import com.portfolio.my_portfolio.dto.SkillMapper;
import com.portfolio.my_portfolio.model.Skill;
import com.portfolio.my_portfolio.service.SkillServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/skills")
public class AdminSkillController {

  private final SkillServiceImpl skillService;
  private final PersonalInfoIdResolver personalInfoIdResolver;

  @GetMapping({ "", "/" })
  public String list(Model model) {
    model.addAttribute("skills", skillService.findAll().stream().map(SkillMapper::toDto).toList());
    return "admin/skills/index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    SkillDto skill = new SkillDto();
    skill.setPersonalInfoId(personalInfoIdResolver.requireId());
    model.addAttribute("skill", skill);
    model.addAttribute("editMode", false);
    return "admin/skills/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("skill") SkillDto skillDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "admin/skills/form";
    }

    skillDto.setId(null);
    Skill skill = SkillMapper.toSkill(skillDto);
    skill.setPersonalInfoId(personalInfoIdResolver.requireId());
    skillService.save(skill);

    redirectAttributes.addFlashAttribute("success", "Habilidad creada correctamente.");
    return "redirect:/admin/skills";
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    SkillDto skill = SkillMapper.toDto(findOrThrow(id));
    model.addAttribute("skill", skill);
    model.addAttribute("editMode", true);
    return "admin/skills/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
      @Valid @ModelAttribute("skill") SkillDto skillDto,
      BindingResult bindingResult,
      Model model,
      RedirectAttributes redirectAttributes) {

    findOrThrow(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      return "admin/skills/form";
    }

    skillDto.setId(id);
    Skill skill = SkillMapper.toSkill(skillDto);
    skill.setPersonalInfoId(personalInfoIdResolver.requireId());
    skillService.save(skill);

    redirectAttributes.addFlashAttribute("success", "Habilidad actualizada correctamente.");
    return "redirect:/admin/skills";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    findOrThrow(id);
    skillService.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Habilidad eliminada correctamente.");
    return "redirect:/admin/skills";
  }

  private Skill findOrThrow(Long id) {
    Optional<Skill> skill = skillService.findById(id);
    if (skill.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Habilidad no encontrada con id: " + id);
    }
    return skill.get();
  }
}
