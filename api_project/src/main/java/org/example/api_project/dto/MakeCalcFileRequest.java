package org.example.api_project.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

public record MakeCalcFileRequest(
    String brand_id,
    String open_date,
    double cb_percent,
    MultipartFile file) {
    public String getBrandId() {
        return brand_id;
    }
    public MultipartFile getFile() {
        return file;
    }
    public LocalDate getOpenDate() {
        return LocalDate.parse(open_date);
    }
    public double getCbPercent() {
        return cb_percent;
    }
}
