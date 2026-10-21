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

import com.portfolio.my_portfolio.model.Education;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class EducationRepositoryImpl implements IEducationRepository {
  private final String TABLE_NAME = "educations";
  private final JdbcTemplate jdbcTemplate;

  private final RowMapper<Education> educationRowMapper = (rs, numRow) -> {
    Education education = new Education();
    education.setId(rs.getLong("id"));
    education.setInstitution(rs.getString("institution"));
    education.setDegree(rs.getString("degree"));
    education.setStartDate(rs.getObject("start_date", java.time.LocalDate.class));
    education.setEndDate(rs.getObject("end_date", java.time.LocalDate.class));
    education.setDescription(rs.getString("description"));
    education.setPersonalInfoId(rs.getLong("personal_info_id"));

    return education;
  };

  @Override
  public Education save(Education education) {
    if (education.getId() == null) {
      String sqlString = "INSERT INTO " + TABLE_NAME
          + " (institution, degree, start_date, end_date, description, personal_info_id) VALUES (?, ?, ?, ?, ?, ?)";
      KeyHolder keyHolder = new GeneratedKeyHolder();
      jdbcTemplate.update(connection -> {
        PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
        ps.setString(1, education.getInstitution());
        ps.setString(2, education.getDegree());
        ps.setObject(3, education.getStartDate());
        ps.setObject(4, education.getEndDate());
        ps.setString(5, education.getDescription());
        ps.setLong(6, education.getPersonalInfoId());
        return ps;
      }, keyHolder);

      Number generatedId = Objects.requireNonNull(
          keyHolder.getKey(),
          "The database did not return a generated ID");

      education.setId(generatedId.longValue());
    } else {
      String sqlString = "UPDATE " + TABLE_NAME
          + " SET institution = ?, degree = ?, start_date = ?, end_date = ?, description = ?, personal_info_id = ? WHERE id = ?";
      jdbcTemplate.update(sqlString, education.getInstitution(), education.getDegree(), education.getStartDate(),
          education.getEndDate(), education.getDescription(), education.getPersonalInfoId(), education.getId());
    }
    return education;
  }

  @Override
  public Optional<Education> findById(Long id) {
    String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    try {
      return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, educationRowMapper, id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<Education> findAll() {
    String sqlString = "SELECT * FROM " + TABLE_NAME;
    return jdbcTemplate.query(sqlString, educationRowMapper);
  }

  @Override
  public void deleteById(Long id) {
    String sqlString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
    jdbcTemplate.update(sqlString, id);
  }

}
