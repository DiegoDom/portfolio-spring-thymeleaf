package com.portfolio.my_portfolio.repository;

import com.portfolio.my_portfolio.model.PersonalInfo;

import java.util.Optional;

public interface IPersonalInfoRepository {
    Optional<PersonalInfo> find();

    PersonalInfo save(PersonalInfo personalInfo);
}
