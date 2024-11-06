package org.example.api_project.dto;


import lombok.Getter;

import java.time.LocalDate;

@Getter
public class MakeCalcS3Request extends MakeCalcRequest {
    public MakeCalcS3Request(String brand_id,
                             String clientsFileKey,
                             String open_date,
                             double cb_percent) {
        this.brandId = brand_id;
        this.clientsFileKey = clientsFileKey;
        this.openDate = LocalDate.parse(open_date);
        this.cbPercent = cb_percent;
    }

    private final String brandId;
    private final String clientsFileKey;
    private final LocalDate openDate;
    private final double cbPercent;
}
