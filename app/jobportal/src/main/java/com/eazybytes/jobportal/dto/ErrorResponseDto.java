package com.eazybytes.jobportal.dto;

import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

public record ErrorResponseDto(String apiPath, HttpStatus errorStatus, String errorMessage, LocalDateTime timestamp) {
}
