package com.example.FinanceTrackerAPI.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.FinanceTrackerAPI.dto.request.DepositRequest;
import com.example.FinanceTrackerAPI.dto.request.TransferRequest;
import com.example.FinanceTrackerAPI.dto.request.WithdrawRequest;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.entity.TransactionType;
import com.example.FinanceTrackerAPI.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = TransactionController.class)
class TransactionControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllTransactions_returnsList() throws Exception {
        TransactionResponse t1 = new TransactionResponse(1L, new BigDecimal("10.00"), TransactionType.DEPOSIT, null, 1L, LocalDateTime.now(), "dep");
        TransactionResponse t2 = new TransactionResponse(2L, new BigDecimal("5.00"), TransactionType.WITHDRAW, 1L, null, LocalDateTime.now(), "wd");
        when(transactionService.getAllTransactions()).thenReturn(List.of(t1, t2));

        mockMvc.perform(get("/api/transactions").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].type").value("WITHDRAW"));
    }

    @Test
    void getTransactionById_returnsTransaction() throws Exception {
        TransactionResponse t = new TransactionResponse(9L, new BigDecimal("99.00"), TransactionType.TRANSFER, 1L, 2L, LocalDateTime.now(), "x");
        when(transactionService.getTransactionById(9L)).thenReturn(t);

        mockMvc.perform(get("/api/transactions/9").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(9))
                .andExpect(jsonPath("$.fromAccountId").value(1));
    }

    @Test
    void transferEndpoint_createsTransaction() throws Exception {
        TransferRequest req = new TransferRequest(1L, 2L, new BigDecimal("12.34"), "pay");
        TransactionResponse created = new TransactionResponse(11L, new BigDecimal("12.34"), TransactionType.TRANSFER, 1L, 2L, LocalDateTime.now(), "pay");
        when(transactionService.transfer(eq(req))).thenReturn(created);

        mockMvc.perform(post("/api/transactions/transfer")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(11))
                .andExpect(jsonPath("$.type").value("TRANSFER"));
    }

    @Test
    void withdrawEndpoint_createsTransaction() throws Exception {
        WithdrawRequest req = new WithdrawRequest(1L, new BigDecimal("5.00"), "atm");
        TransactionResponse created = new TransactionResponse(12L, new BigDecimal("5.00"), TransactionType.WITHDRAW, 1L, null, LocalDateTime.now(), "atm");
        when(transactionService.withdraw(eq(req))).thenReturn(created);

        mockMvc.perform(post("/api/transactions/withdraw")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(12))
                .andExpect(jsonPath("$.fromAccountId").value(1));
    }

    @Test
    void depositEndpoint_createsTransaction() throws Exception {
        DepositRequest req = new DepositRequest(2L, new BigDecimal("7.00"), "gift");
        TransactionResponse created = new TransactionResponse(13L, new BigDecimal("7.00"), TransactionType.DEPOSIT, null, 2L, LocalDateTime.now(), "gift");
        when(transactionService.deposit(eq(req))).thenReturn(created);

        mockMvc.perform(post("/api/transactions/deposit")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(13))
                .andExpect(jsonPath("$.toAccountId").value(2));
    }
}

