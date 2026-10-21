package com.portfolio.my_portfolio.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Education {
    private Long id;
    private String degree;
    private String institution;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private Long personalInfoId;

}
