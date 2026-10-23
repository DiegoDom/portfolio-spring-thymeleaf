package com.portfolio.my_portfolio.repository;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.portfolio.my_portfolio.model.Project;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ProjectRepositoryImpl implements IProjectRepository {
  private final String TABLE_NAME = "projects";
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Project> projectRowMapper = (rs, numRow) -> {
    Project project = new Project();
    project.setId(rs.getLong("id"));
    project.setTitle(rs.getString("title"));
    project.setDescription(rs.getString("description"));
    project.setImageUrl(rs.getString("image_url"));
    project.setProjectUrl(rs.getString("project_url"));
    project.setPersonalInfoId(rs.getLong("personal_info_id"));

    return project;
  };

  @Override
  public Project save(Project project) {
    if (project.getId() == null) {
      if (project.getTitle() == null || project.getTitle().trim().isEmpty()) {
        throw new com.portfolio.my_portfolio.exception.ValidationException(
            new org.springframework.validation.BeanPropertyBindingResult(project, "project") {{
              rejectValue("title", "", "The title cannot be empty");
            }});
      }
      String sqlString = "INSERT INTO " + TABLE_NAME
          + " (title, description, image_url, project_url, personal_info_id) VALUES (?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
        ps.setString(1, project.getTitle());
        ps.setString(2, project.getDescription());
        ps.setString(3, project.getImageUrl());
        ps.setString(4, project.getProjectUrl());
        ps.setLong(5, project.getPersonalInfoId());
        return ps;
      }, keyHolder);

      Number generatedId = Objects.requireNonNull(
          keyHolder.getKey(),
          "The database did not return a generated ID");

      project.setId(generatedId.longValue());
    } else {
      String sqlString = "UPDATE " + TABLE_NAME
          + " SET title = ?, description = ?, image_url = ?, project_url = ?, personal_info_id = ? WHERE id = ?";
      jdbcTemplate.update(sqlString, project.getTitle(), project.getDescription(), project.getImageUrl(),
          project.getProjectUrl(), project.getPersonalInfoId(), project.getId());
    }
    return project;
  }

  @Override
  public Optional<Project> findById(Long id) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, projectRowMapper, id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Project> findAll() {
    String sqlString = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sqlString, projectRowMapper);
  }

  @Override
  public void deleteById(Long id) {
    String sqlString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sqlString, id);
  }

}
