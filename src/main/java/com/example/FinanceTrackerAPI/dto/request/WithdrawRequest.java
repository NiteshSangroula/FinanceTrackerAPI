package com.example.FinanceTrackerAPI.dto.request;

import java.math.BigDecimal;

public record WithdrawRequest(
        long fromAccountId,
        BigDecimal amount,
        String description) {
}
