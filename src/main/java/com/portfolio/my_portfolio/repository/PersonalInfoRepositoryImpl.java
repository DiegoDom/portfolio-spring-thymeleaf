package com.portfolio.my_portfolio.repository;

import com.portfolio.my_portfolio.model.PersonalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonalInfoRepositoryImpl implements IPersonalInfoRepository {
    private final String TABLE_NAME = "personal_info";
    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<PersonalInfo> personalInfoRowMapper = (rs, numRow) -> {
        PersonalInfo info = new PersonalInfo();
        info.setId(rs.getLong("id"));
        info.setFirstName(rs.getString("first_name"));
        info.setLastName(rs.getString("last_name"));
        info.setTitle(rs.getString("title"));
        info.setProfileDescription(rs.getString("profile_description"));
        info.setProfileImageUrl(rs.getString("profile_image_url"));
        info.setYearsOfExperience(rs.getObject("years_of_experience", Integer.class));
        info.setEmail(rs.getString("email"));
        info.setPhone(rs.getString("phone"));
        info.setLinkedinUrl(rs.getString("linkedin_url"));
        info.setGithubUrl(rs.getString("github_url"));

        return info;
    };

    @Override
    public PersonalInfo save(PersonalInfo personalInfo) {
        if (personalInfo.getId() == null) {
            String sqlString = "INSERT INTO " + TABLE_NAME + " (first_name, last_name, title, profile_description, " +
                    "profile_image_url, years_of_experience, email, phone, linkedin_url, github_url) " +
                    "VALUES (?,?, ?, ?, ?, ?, ?, ?, ?, ?)";

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jdbcTemplate.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sqlString, new String[] { "id" });
                ps.setString(1, personalInfo.getFirstName());
                ps.setString(2, personalInfo.getLastName());
                ps.setString(3, personalInfo.getTitle());
                ps.setString(4, personalInfo.getProfileDescription());
                ps.setString(5, personalInfo.getProfileImageUrl());
                if (personalInfo.getYearsOfExperience() == null) {
                    ps.setNull(6, Types.INTEGER);
                } else {
                    ps.setObject(6, personalInfo.getYearsOfExperience(), Types.INTEGER);
                }
                ps.setString(7, personalInfo.getEmail());
                ps.setString(8, personalInfo.getPhone());
                ps.setString(9, personalInfo.getLinkedinUrl());
                ps.setString(10, personalInfo.getGithubUrl());
                return ps;
            }, keyHolder);

            Number generatedId = Objects.requireNonNull(
                    keyHolder.getKey(),
                    "The database did not return a generated ID");

            personalInfo.setId(generatedId.longValue());

        } else {
            String sqlString = "UPDATE " + TABLE_NAME
                    + " SET first_name = ?, last_name = ?, title = ?, profile_description = ?, " +
                    "profile_image_url = ?, years_of_experience = ?, email = ?, phone = ?, linkedin_url = ?, github_url = ? WHERE id = ?";
            jdbcTemplate.update(sqlString,
                    personalInfo.getFirstName(),
                    personalInfo.getLastName(),
                    personalInfo.getTitle(),
                    personalInfo.getProfileDescription(),
                    personalInfo.getProfileImageUrl(),
                    personalInfo.getYearsOfExperience(),
                    personalInfo.getEmail(),
                    personalInfo.getPhone(),
                    personalInfo.getLinkedinUrl(),
                    personalInfo.getGithubUrl(),
                    personalInfo.getId());
        }
        return personalInfo;
    }

    @Override
    public Optional<PersonalInfo> findById(Long id) {
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        /*
         * List<PersonalInfo> results = jdbcTemplate.query(queryString,
         * personalInfoRowMapper, id);
         * return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
         */
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, personalInfoRowMapper, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public List<PersonalInfo> findAll() {
        String queryString = "SELECT * FROM " + TABLE_NAME;
        return jdbcTemplate.query(queryString, personalInfoRowMapper);
    }

    @Override
    public void deleteById(Long id) {
        String queryString = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";
        jdbcTemplate.update(queryString, id);
    }
}
