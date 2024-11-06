package org.example.api_project.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public abstract class MakeCalcRequest {
    private String brandId;
    private LocalDate openDate;
    private double cbPercent;
}
