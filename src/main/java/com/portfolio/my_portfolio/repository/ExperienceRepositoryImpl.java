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

import com.portfolio.my_portfolio.model.Experience;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ExperienceRepositoryImpl implements IExperienceRespository {

  private final String TABLE_NAME = "experiences";
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Experience> experienceRowMapper = (rs, numRow) -> {
    Experience experience = new Experience();
    experience.setId(rs.getLong("id"));
    experience.setJobTitle(rs.getString("job_title"));
    experience.setCompanyName(rs.getString("company_name"));
    experience.setStartDate(rs.getObject("start_date", java.time.LocalDate.class));
    experience.setEndDate(rs.getObject("end_date", java.time.LocalDate.class));
    experience.setDescription(rs.getString("description"));
    experience.setPersonalInfoId(rs.getLong("personal_info_id"));

    return experience;
  };

  @Override
  public Experience save(Experience experience) {
    if (experience.getId() == null) {
      String sqlString = "INSERT INTO " + TABLE_NAME
          + " (job_title, company_name, start_date, end_date, description, personal_info_id) VALUES (?, ?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
        ps.setString(1, experience.getJobTitle());
        ps.setString(2, experience.getCompanyName());
        ps.setObject(3, experience.getStartDate());
        ps.setObject(4, experience.getEndDate());
        ps.setString(5, experience.getDescription());
        ps.setLong(6, experience.getPersonalInfoId());
        return ps;
      }, keyHolder);

      Number generatedId = Objects.requireNonNull(
          keyHolder.getKey(),
          "The database did not return a generated ID");

      experience.setId(generatedId.longValue());
    } else {
      String sqlString = "UPDATE " + TABLE_NAME
          + " SET job_title = ?, company_name = ?, start_date = ?, end_date = ?, description = ?, personal_info_id = ? WHERE id = ?";
      jdbcTemplate.update(sqlString, experience.getJobTitle(), experience.getCompanyName(), experience.getStartDate(),
          experience.getEndDate(), experience.getDescription(), experience.getPersonalInfoId(), experience.getId());
    }
    return experience;
  }

  @Override
  public Optional<Experience> findById(Long id) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, experienceRowMapper, id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Experience> findAll() {
    String sqlString = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sqlString, experienceRowMapper);
  }

  @Override
  public void deleteById(Long id) {
    String sqlString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sqlString, id);
  }

}
