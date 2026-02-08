package com.example.FinanceTrackerAPI.dto.request;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateAccountRequest(
        @NotBlank String name,
        @NotNull @Positive BigDecimal initialAmount) {
}
