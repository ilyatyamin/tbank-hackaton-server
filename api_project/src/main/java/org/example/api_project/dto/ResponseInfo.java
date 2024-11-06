package org.example.api_project.dto;

import org.springframework.http.HttpStatus;

import java.util.Optional;

public record ResponseInfo<T>(HttpStatus status, Optional<T> info, String message) {
}
