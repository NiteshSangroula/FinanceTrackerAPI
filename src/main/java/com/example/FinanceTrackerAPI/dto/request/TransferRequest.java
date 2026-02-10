package com.example.FinanceTrackerAPI.dto.request;

import java.math.BigDecimal;

public record TransferRequest(
        long fromAccountId,
        long toAccountId,
        BigDecimal amount,
        String description) {
}
