package com.example.FinanceTrackerAPI.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.example.FinanceTrackerAPI.dto.request.CreateAccountRequest;
import com.example.FinanceTrackerAPI.dto.response.AccountResponse;
import com.example.FinanceTrackerAPI.service.AccountService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = AccountController.class)
class AccountControllerWebMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccountService accountService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getAllAccounts_returnsList() throws Exception {
        AccountResponse a1 = new AccountResponse(1L, "A", new BigDecimal("100.00"), LocalDateTime.now());
        AccountResponse a2 = new AccountResponse(2L, "B", new BigDecimal("200.00"), LocalDateTime.now());
        when(accountService.getAllAccounts()).thenReturn(List.of(a1, a2));

        mockMvc.perform(get("/api/accounts").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].name").value("B"));
    }

    @Test
    void getAccountById_returnsAccount() throws Exception {
        AccountResponse a = new AccountResponse(5L, "Pete", new BigDecimal("50.00"), LocalDateTime.now());
        when(accountService.getAccountById(5L)).thenReturn(a);

        mockMvc.perform(get("/api/accounts/5").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.name").value("Pete"));
    }

    @Test
    void createAccount_returnsCreated() throws Exception {
        CreateAccountRequest req = new CreateAccountRequest("New", new BigDecimal("10.00"));
        AccountResponse created = new AccountResponse(10L, "New", new BigDecimal("10.00"), LocalDateTime.now());
        when(accountService.createAccount(req)).thenReturn(created);

        mockMvc.perform(post("/api/accounts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.name").value("New"));
    }

    @Test
    void deleteAccount_returnsNoContent() throws Exception {
        // just ensure controller returns 204 when service does not throw
        mockMvc.perform(delete("/api/accounts/99"))
                .andExpect(status().isNoContent());
    }
}

