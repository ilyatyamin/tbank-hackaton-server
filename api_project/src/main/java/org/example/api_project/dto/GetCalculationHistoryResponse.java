package org.example.api_project.dto;

import org.example.api_project.utils.CalcStatus;

public record GetCalculationHistoryResponse(
        String request_time,
        String brand_id,
        String offer_begin_date,
        Double cashback_percent,
        CalcStatus status,
        Double gmv,
        Long purchaseCount,
        Double totalCashback
) {

}
