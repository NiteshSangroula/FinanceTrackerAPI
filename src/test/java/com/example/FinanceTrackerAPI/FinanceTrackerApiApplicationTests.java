package com.example.FinanceTrackerAPI;

import com.example.FinanceTrackerAPI.dto.request.CreateAccountRequest;
import com.example.FinanceTrackerAPI.dto.request.DepositRequest;
import com.example.FinanceTrackerAPI.dto.response.AccountResponse;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.service.AccountService;
import com.example.FinanceTrackerAPI.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FinanceTrackerApiApplicationTests {

    @Autowired
    private AccountService accountService;

    @Autowired
    private TransactionService transactionService;

    @Test
    void contextLoads() {
        // basic smoke: application context starts and services are injected
        assertNotNull(accountService);
        assertNotNull(transactionService);
    }

    @Test
    @Transactional
    void createAccountAndListAccounts() {
        List<AccountResponse> before = accountService.getAllAccounts();
        int beforeSize = before.size();

        CreateAccountRequest req = new CreateAccountRequest("Test Account", new BigDecimal("123.45"));
        AccountResponse created = accountService.createAccount(req);

        assertNotNull(created);
        assertEquals("Test Account", created.name());
        assertEquals(0, created.balance().compareTo(new BigDecimal("123.45")));

        List<AccountResponse> after = accountService.getAllAccounts();
        assertEquals(beforeSize + 1, after.size());
    }

    @Test
    @Transactional
    void depositIncreasesBalance() {
        // pick an existing account (data.sql inserts two accounts with id 1 and 2)
        AccountResponse before = accountService.getAccountById(1L);
        BigDecimal original = before.balance();

        DepositRequest depositRequest = new DepositRequest(1L, new BigDecimal("100.00"), "Test deposit");
        TransactionResponse tx = transactionService.deposit(depositRequest);

        assertNotNull(tx);
        assertEquals(1L, tx.toAccountId());
        assertEquals(0, tx.amount().compareTo(new BigDecimal("100.00")));

        AccountResponse after = accountService.getAccountById(1L);
        assertEquals(0, after.balance().compareTo(original.add(new BigDecimal("100.00"))));
    }

}
