package com.portfolio.my_portfolio.controller;

import java.io.IOException;
import java.util.List;
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

import com.portfolio.my_portfolio.dto.ProjectDto;
import com.portfolio.my_portfolio.dto.ProjectMapper;
import com.portfolio.my_portfolio.model.Project;
import com.portfolio.my_portfolio.service.FileStorageService;
import com.portfolio.my_portfolio.service.IPersonalInfoService;
import com.portfolio.my_portfolio.service.ProjectService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {
  private final ProjectService projectService;
  private final FileStorageService fileStorageService;
  private final IPersonalInfoService personalInfoService;

  @GetMapping({"/", ""})
  public String getAll(Model model) {
    List<ProjectDto> projects = projectService.findAll().stream().map(ProjectMapper::tDto).toList();
    model.addAttribute("projects", projects);
    return "projects/index";
  }

  @GetMapping("/new")
  public String showCreateForm(Model model) {
    model.addAttribute("project", new ProjectDto());
    model.addAttribute("editMode", false);
    return "projects/form";
  }

  @PostMapping("/add")
  public String addProject(@Valid @ModelAttribute("project") ProjectDto projectDto,
      BindingResult bindingResult,
      @RequestParam("file") MultipartFile file,
      Model model) {

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "projects/form";
    }

    try {
      if (file != null && !file.isEmpty()) {
        String imageUrl = fileStorageService.storeFile(file);
        projectDto.setImageUrl(imageUrl);
      }

      Project project = ProjectMapper.toProject(projectDto);
      project.setPersonalInfoId(currentPersonalInfoId());
      projectService.save(project);

      return "redirect:/projects/";
    } catch (IOException e) {
      model.addAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
      model.addAttribute("editMode", false);
      model.addAttribute("formErrors", true);
      return "projects/form";
    }
  }

  @GetMapping("/edit/{id}")
  public String showEditForm(@PathVariable Long id, Model model) {
    Optional<Project> projectOpt = projectService.findById(id);

    if (projectOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }

    ProjectDto projectDto = ProjectMapper.tDto(projectOpt.get());
    model.addAttribute("project", projectDto);
    model.addAttribute("editMode", true);
    return "projects/form";
  }

  @PostMapping("/update/{id}")
  public String updateProject(@PathVariable Long id,
      @Valid @ModelAttribute("project") ProjectDto projectDto,
      BindingResult bindingResult,
      @RequestParam(value = "file", required = false) MultipartFile file,
      Model model) {

    Optional<Project> existingOpt = projectService.findById(id);
    if (existingOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }

    if (bindingResult.hasErrors()) {
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      if (projectDto.getImageUrl() == null || projectDto.getImageUrl().isBlank()) {
        projectDto.setImageUrl(existingOpt.get().getImageUrl());
      }
      return "projects/form";
    }

    try {
      if (file != null && !file.isEmpty()) {
        String imageUrl = fileStorageService.storeFile(file);
        projectDto.setImageUrl(imageUrl);
      } else {
        projectDto.setImageUrl(existingOpt.get().getImageUrl());
      }

      projectDto.setId(id);
      Project project = ProjectMapper.toProject(projectDto);
      project.setPersonalInfoId(currentPersonalInfoId());
      projectService.save(project);

      return "redirect:/projects/";
    } catch (IOException e) {
      model.addAttribute("error", "Error al subir la imagen. Inténtalo de nuevo.");
      model.addAttribute("editMode", true);
      model.addAttribute("formErrors", true);
      if (projectDto.getImageUrl() == null || projectDto.getImageUrl().isBlank()) {
        projectDto.setImageUrl(existingOpt.get().getImageUrl());
      }
      return "projects/form";
    }
  }

  @PostMapping("/delete/{id}")
  public String deleteProject(@PathVariable Long id) {
    Optional<Project> projectOpt = projectService.findById(id);

    if (projectOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found with id: " + id);
    }

    projectService.deleteById(id);
    return "redirect:/projects/";
  }

  private Long currentPersonalInfoId() {
    return personalInfoService.find()
        .map(personalInfo -> personalInfo.getId())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT,
            "Personal info is not configured"));
  }
}
