package org.example.api_project.dto;

public record GetBudgetCalculationResponse(double gmv,
                                           long purchaseCount,
                                           double totalCashback) { }
