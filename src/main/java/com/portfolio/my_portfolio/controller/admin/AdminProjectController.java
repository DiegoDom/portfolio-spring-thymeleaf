package com.portfolio.my_portfolio.controller.admin;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.portfolio.my_portfolio.dto.ProjectDto;
import com.portfolio.my_portfolio.dto.ProjectMapper;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.service.FileStorageService;
import com.portfolio.my_portfolio.service.ProjectServiceImpl;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/projects")
public class AdminProjectController {

  private final ProjectServiceImpl projectService;
  private final FileStorageService fileStorageService;
  private final PersonalInfoIdResolver personalInfoIdResolver;

  @GetMapping({ "", "/" })
  public String list(Model model) {
    model.addAttribute("projects", projectService.findAll().stream().map(ProjectMapper::tDto).toList());
    return "admin/projects/index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    ProjectDto project = new ProjectDto();
    project.setPersonalInfoId(personalInfoIdResolver.requireId());
    model.addAttribute("project", project);
    model.addAttribute("editMode", false);
    return "admin/projects/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("project") ProjectDto projectDto,
      BindingResult bindingResult,
      @RequestParam(value = "file", required = false) MultipartFile file,
      Model model,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "admin/projects/form";
    }

    try {
      if (file != null && !file.isEmpty()) {
        projectDto.setImageUrl(fileStorageService.storeFile(file));
      }

      projectDto.setId(null);
      Project project = ProjectMapper.toProject(projectDto);
      project.setPersonalInfoId(personalInfoIdResolver.requireId());
      projectService.save(project);

      redirectAttributes.addFlashAttribute("success", "Proyecto creado correctamente.");
      return "redirect:/admin/projects";
    } catch (IOException e) {
      model.addAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "admin/projects/form";
    }
  }

  @GetMapping("/{id}/edit")
  public String showEditForm(@PathVariable Long id, Model model) {
    ProjectDto project = ProjectMapper.tDto(findOrThrow(id));
    model.addAttribute("project", project);
    model.addAttribute("editMode", true);
    return "admin/projects/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
      @Valid @ModelAttribute("project") ProjectDto projectDto,
      BindingResult bindingResult,
      @RequestParam(value = "file", required = false) MultipartFile file,
      Model model,
      RedirectAttributes redirectAttributes) {

    Project existing = findOrThrow(id);

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      if (projectDto.getImageUrl() == null || projectDto.getImageUrl().isBlank()) {
        projectDto.setImageUrl(existing.getImageUrl());
      }
      return "admin/projects/form";
    }

    try {
      if (file != null && !file.isEmpty()) {
        projectDto.setImageUrl(fileStorageService.storeFile(file));
      } else {
        projectDto.setImageUrl(existing.getImageUrl());
      }

      projectDto.setId(id);
      Project project = ProjectMapper.toProject(projectDto);
      project.setPersonalInfoId(personalInfoIdResolver.requireId());
      projectService.save(project);

      redirectAttributes.addFlashAttribute("success", "Proyecto actualizado correctamente.");
      return "redirect:/admin/projects";
    } catch (IOException e) {
      model.addAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      if (projectDto.getImageUrl() == null || projectDto.getImageUrl().isBlank()) {
        projectDto.setImageUrl(existing.getImageUrl());
      }
      return "admin/projects/form";
    }
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id, RedirectAttributes redirectAttributes) {
    findOrThrow(id);
    projectService.deleteById(id);
    redirectAttributes.addFlashAttribute("success", "Proyecto eliminado correctamente.");
    return "redirect:/admin/projects";
  }

  private Project findOrThrow(Long id) {
    Optional<Project> project = projectService.findById(id);
    if (project.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Proyecto no encontrado con id: " + id);
    }
    return project.get();
  }
}
