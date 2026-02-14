package com.example.FinanceTrackerAPI.controller;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.example.FinanceTrackerAPI.dto.request.TransferRequest;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.service.TransactionService;

@ExtendWith(MockitoExtension.class)
class TransactionControllerTest {

    @Mock
    private TransactionService service;

    @InjectMocks
    private TransactionController controller;

    @Test
    void testCreateTransferTransaction_validData() {
        TransferRequest request = new TransferRequest(
                1L,
                2L,
                new BigDecimal("100"),
                "test");

        TransactionResponse mockResponse = mock(TransactionResponse.class);
        when(service.transfer(request))
                .thenReturn(mockResponse);

        doNothing().when(service).dummy(any());

        ResponseEntity<TransactionResponse> response = controller.createTransferTransaction(request);

        verify(service).transfer(request);

        verify(service).dummy(request.description());

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(mockResponse, response.getBody());
    }

}
