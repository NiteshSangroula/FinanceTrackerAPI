package com.example.FinanceTrackerAPI.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.FinanceTrackerAPI.dto.request.DepositRequest;
import com.example.FinanceTrackerAPI.dto.request.TransferRequest;
import com.example.FinanceTrackerAPI.dto.request.WithdrawRequest;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.service.TransactionService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponse> getTransactionById(
            @PathVariable long id) {
        return ResponseEntity.ok(transactionService.getTransactionById(id));
    }

    @PostMapping("/transfer")
    public ResponseEntity<TransactionResponse> createTransferTransaction(
            @Valid @RequestBody TransferRequest request) {

        TransactionResponse created = transactionService.transfer(request);

        TransactionService.dummy(request.description());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<TransactionResponse> createWithdrawTransaction(
            @Valid @RequestBody WithdrawRequest request) {

        TransactionResponse created = transactionService.withdraw(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    @PostMapping("/deposit")
    public ResponseEntity<TransactionResponse> createDepositTransaction(
            @Valid @RequestBody DepositRequest request) {

        TransactionResponse created = transactionService.deposit(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

}
