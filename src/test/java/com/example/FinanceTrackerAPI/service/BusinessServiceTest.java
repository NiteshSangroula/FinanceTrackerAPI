package com.example.FinanceTrackerAPI.service;


import com.example.FinanceTrackerAPI.entity.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BusinessServiceTest {
    BusinessService businessService;
    AccountService accountService;

    @BeforeEach
    void setUp() {
        accountService = spy(new AccountService(null)); // Spy needs actual instance
        businessService = new BusinessService(accountService);
    }
    @Test
    void testDangerousWork(){
        String description = "12345";
//        doNothing().when(accountService).deleteAccount(any(Long.class));

        Account account = Account.builder().balance(new BigDecimal(123456L)).build();
        // Act
//        businessService.dummy(description);

        businessService.objectMethod(account);
        // Assert

        verify(accountService).dangerousWork1(argThat(a -> {
            return a.getBalance().compareTo(new BigDecimal(123456L)) == 0;
        }));
//        verify(accountService).dangerousWork(12345L);
//        verify(accountService).deleteAccount(any(Long.class));

    }

}
