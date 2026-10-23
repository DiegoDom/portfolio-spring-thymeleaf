package com.portfolio.my_portfolio.repository;

import com.portfolio.my_portfolio.model.PersonalInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PersonalInfoRepositoryImpl implements IPersonalInfoRepository {
    private static final Long SINGLETON_ID = 1L;

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
        String sqlString = "INSERT INTO " + TABLE_NAME
                + " (id, first_name, last_name, title, profile_description, profile_image_url, "
                + "years_of_experience, email, phone, linkedin_url, github_url) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) "
                + "ON CONFLICT (id) DO UPDATE SET "
                + "first_name = EXCLUDED.first_name, "
                + "last_name = EXCLUDED.last_name, "
                + "title = EXCLUDED.title, "
                + "profile_description = EXCLUDED.profile_description, "
                + "profile_image_url = EXCLUDED.profile_image_url, "
                + "years_of_experience = EXCLUDED.years_of_experience, "
                + "email = EXCLUDED.email, "
                + "phone = EXCLUDED.phone, "
                + "linkedin_url = EXCLUDED.linkedin_url, "
                + "github_url = EXCLUDED.github_url";

        jdbcTemplate.update(sqlString,
                SINGLETON_ID,
                personalInfo.getFirstName(),
                personalInfo.getLastName(),
                personalInfo.getTitle(),
                personalInfo.getProfileDescription(),
                personalInfo.getProfileImageUrl(),
                personalInfo.getYearsOfExperience(),
                personalInfo.getEmail(),
                personalInfo.getPhone(),
                personalInfo.getLinkedinUrl(),
                personalInfo.getGithubUrl());

        personalInfo.setId(SINGLETON_ID);
        return personalInfo;
    }

    @Override
    public Optional<PersonalInfo> find() {
        String queryString = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject(queryString, personalInfoRowMapper, SINGLETON_ID));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
