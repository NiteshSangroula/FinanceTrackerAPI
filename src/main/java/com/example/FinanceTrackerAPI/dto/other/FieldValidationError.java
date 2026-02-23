package com.example.FinanceTrackerAPI.dto.other;

public record FieldValidationError(
    String field,
    String code,
    String message
) {
}
