package org.example.api_project.dto;

public record InsertResultRequest(
        Long id,
        Double gmv,
        Long purchaseCount,
        Double totalCashback
) { }
