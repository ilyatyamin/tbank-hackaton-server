package org.example.api_project.dao;

import jakarta.persistence.*;
import lombok.Data;
import org.example.api_project.dto.MakeCalcFileRequest;
import org.example.api_project.dto.MakeCalcRequest;
import org.example.api_project.utils.CalcStatus;

import java.time.LocalDate;
import java.time.ZonedDateTime;

@Data
@Entity(name = "requests")
@Table(name = "requests")
public class BudgetRequest {
    public static BudgetRequest newInstance(
            MakeCalcRequest dto
    ) {
        BudgetRequest request = new BudgetRequest();

        request.requestTime = ZonedDateTime.now();
        request.brandId = dto.getBrandId();
        request.offerBeginTime = dto.getOpenDate();
        request.cashbackPercent = dto.getCbPercent();
        request.calcStatus = CalcStatus.IN_PROCESS;

        return request;
    }

    public static BudgetRequest fromMakeCalcFileRequest(MakeCalcFileRequest request) {
        BudgetRequest req = new BudgetRequest();
        req.brandId = request.getBrandId();
        req.requestTime = ZonedDateTime.now();
        req.offerBeginTime = request.getOpenDate();
        req.cashbackPercent = request.getCbPercent();
        req.calcStatus = CalcStatus.IN_PROCESS;
        return req;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id = null;

    @Column(name = "request_time")
    private ZonedDateTime requestTime;

    @Column(name = "brand_id")
    private String brandId;

    @Column(name = "offer_begin_date")
    private LocalDate offerBeginTime;

    @Column(name = "cashback_percent")
    private double cashbackPercent;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private CalcStatus calcStatus;
}
