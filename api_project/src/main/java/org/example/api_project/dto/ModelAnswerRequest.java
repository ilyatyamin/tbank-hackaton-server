package org.example.api_project.dto;

public record ModelAnswerRequest(Long dbId, String brandId, String openDate,
                                 double cbPercent, String file) {
}
