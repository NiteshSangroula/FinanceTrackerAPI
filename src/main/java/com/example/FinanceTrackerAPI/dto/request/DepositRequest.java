package com.example.FinanceTrackerAPI.dto.request;

import java.math.BigDecimal;

public record DepositRequest(
        long toAccountId,
        BigDecimal amount,
        String description) {
}
