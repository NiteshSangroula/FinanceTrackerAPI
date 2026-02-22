package com.example.FinanceTrackerAPI.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.example.FinanceTrackerAPI.dto.request.DepositRequest;
import com.example.FinanceTrackerAPI.dto.request.TransferRequest;
import com.example.FinanceTrackerAPI.dto.request.WithdrawRequest;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.entity.TransactionType;
import com.example.FinanceTrackerAPI.service.TransactionService;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(TransactionController.class)
public class TransactionControllerMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TransactionService service;

    //-----------------------TRANSFER
    @Test
    void createTransferTransaction_validRequest_returns201() throws Exception{
        TransferRequest request = new TransferRequest(
            1L, 2L, new BigDecimal("100"), "test");

        TransactionResponse response = new TransactionResponse(
        1L, new BigDecimal("100"), TransactionType.TRANSFER, 1L, 2L, LocalDateTime.now(), "test");

        when(service.transfer(any(TransferRequest.class)))
            .thenReturn(response);

        mockMvc.perform(post("/api/transactions/transfer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.amount").value(100))
            .andExpect(jsonPath("$.type").value("TRANSFER"))
            .andExpect(jsonPath("$.fromAccountId").value(1L))
            .andExpect(jsonPath("$.toAccountId").value(2L))
            .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void createTransferTransaction_nullFromAccountId_returns400() throws Exception {
        TransferRequest request = new TransferRequest(
            null, 2L, new BigDecimal("100"), "test");

        mockMvc.perform(post("/api/transactions/transfer")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).transfer(any());
    }

    @Test
    void createTransferTransaction_nullToAccountId_returns400() throws Exception {
        TransferRequest request = new TransferRequest(
            1L, null, new BigDecimal("100"), "test");

        mockMvc.perform(post("/api/transactions/transfer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).transfer(any());
    }

    @Test
    void createTransferTransaction_nullBalance_returns400() throws Exception {
        TransferRequest request = new TransferRequest(
            1L, 2L, null, "test");

        mockMvc.perform(post("/api/transactions/transfer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).transfer(any());
    }

    @Test
    void createTransferTransaction_negativeBalance_returns400() throws Exception {
        TransferRequest request = new TransferRequest(
            1L, 2L, new BigDecimal("-100"), "test");

        mockMvc.perform(post("/api/transactions/transfer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).transfer(any());
    }

    @Test
    void createTransferTransaction_blankDescription_returns400() throws Exception {
        TransferRequest request = new TransferRequest(
            1L, 2L, new BigDecimal("100"), "");

        mockMvc.perform(post("/api/transactions/transfer")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).transfer(any());
    }


    //-----------------------WITHDRAW
    @Test
    void createWithdrawTransaction_validRequest_returns201() throws Exception {
        WithdrawRequest request = new WithdrawRequest(
            1L, new BigDecimal("100"), "test");

        TransactionResponse response = new TransactionResponse(
            1L, new BigDecimal("100"), TransactionType.WITHDRAW, 
            1L, null, LocalDateTime.now(), "test");

        when(service.withdraw(any(WithdrawRequest.class)))
            .thenReturn(response);


        mockMvc.perform(post("/api/transactions/withdraw")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.amount").value(100))
            .andExpect(jsonPath("$.type").value("WITHDRAW"))
            .andExpect(jsonPath("$.fromAccountId").value(1L))
            .andExpect(jsonPath("$.toAccountId").value(Matchers.nullValue()))
            .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void createWithdrawTransaction_negativeBalance_returns400() throws Exception {
        WithdrawRequest request = new WithdrawRequest(
            1L, new BigDecimal("-100"), "test");

        mockMvc.perform(post("/api/transactions/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).withdraw(any());
    }

    @Test
    void createWithdrawTransaction_nullBalance_returns400() throws Exception {
        WithdrawRequest request = new WithdrawRequest(
            1L, null, "test");

        mockMvc.perform(post("/api/transactions/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).withdraw(any());
    }

    @Test
    void createWithdrawTransaction_nullFromAccountId_returns400() throws Exception {
        WithdrawRequest request = new WithdrawRequest(
            null, new BigDecimal("100"), "test");

        mockMvc.perform(post("/api/transactions/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).withdraw(any());
    }

    @Test
    void createWithdrawTransaction_blankDescription_returns400() throws Exception {
        WithdrawRequest request = new WithdrawRequest(
            1L, new BigDecimal("100"), "");

        mockMvc.perform(post("/api/transactions/withdraw")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).withdraw(any());
    }


    //-----------------------DEPOSIT
    @Test
    void createDepositTransaction_validRequest_returns201() throws Exception {
        DepositRequest request = new DepositRequest(
            1L, new BigDecimal("100"), "test");

        TransactionResponse response = new TransactionResponse(
            1L, new BigDecimal("100"), TransactionType.DEPOSIT, 
            null, 1L, LocalDateTime.now(), "test");

        when(service.deposit(any(DepositRequest.class)))
            .thenReturn(response);


        mockMvc.perform(post("/api/transactions/deposit")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.amount").value(100))
            .andExpect(jsonPath("$.type").value("DEPOSIT"))
            .andExpect(jsonPath("$.fromAccountId").value(Matchers.nullValue()))
            .andExpect(jsonPath("$.toAccountId").value(1L))
            .andExpect(jsonPath("$.description").value("test"));
    }

    @Test
    void createDepositTransaction_negativeBalance_returns400() throws Exception {
        DepositRequest request = new DepositRequest(
            1L, new BigDecimal("-100"), "test");

        mockMvc.perform(post("/api/transactions/deposit")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).deposit(any());
    }

    @Test
    void createDepositTransaction_nullBalance_returns400() throws Exception {
        DepositRequest request = new DepositRequest(
            1L, null, "test");

        mockMvc.perform(post("/api/transactions/deposit")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).deposit(any());
    }

    @Test
    void createDepositTransaction_nullToAccountId_returns400() throws Exception {
        DepositRequest request = new DepositRequest(
            null, new BigDecimal("100"), "test");

        mockMvc.perform(post("/api/transactions/deposit")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).deposit(any());
    }

    @Test
    void createDepositTransaction_blankDescription_returns400() throws Exception {
        DepositRequest request = new DepositRequest(
            1L, new BigDecimal("100"), "");

        mockMvc.perform(post("/api/transactions/deposit")
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest());

        verify(service, never()).deposit(any());
    }

    //-----------GET ALL TRANSACTIONS

    

    //-----------GET TRANSACTION BY ID
}
