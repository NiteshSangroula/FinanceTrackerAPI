package com.example.FinanceTrackerAPI.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.StreamSupport;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.FinanceTrackerAPI.dto.request.DepositRequest;
import com.example.FinanceTrackerAPI.dto.request.TransferRequest;
import com.example.FinanceTrackerAPI.dto.request.WithdrawRequest;
import com.example.FinanceTrackerAPI.dto.response.TransactionResponse;
import com.example.FinanceTrackerAPI.entity.Transaction;
import com.example.FinanceTrackerAPI.entity.TransactionType;
import com.example.FinanceTrackerAPI.exception.AccountNotFoundException;
import com.example.FinanceTrackerAPI.exception.InsufficientBalanceException;
import com.example.FinanceTrackerAPI.exception.TransactionNotFoundException;
import com.example.FinanceTrackerAPI.repository.AccountRepository;
import com.example.FinanceTrackerAPI.repository.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;

    public TransactionService(
            TransactionRepository transactionRepository,
            AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    private TransactionResponse mapToTransactionResponse(Transaction transaction) {
        return new TransactionResponse(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getFromAccountId(),
                transaction.getToAccountId(),
                transaction.getCreatedAt(),
                transaction.getDescription());
    }

    private Transaction createAndSaveTransaction(
            TransactionType type,
            BigDecimal amount,
            Long fromAccountId,
            Long toAccountId,
            String description) {

        Transaction transaction = new Transaction();
        transaction.setAmount(amount);
        transaction.setType(type);
        transaction.setFromAccountId(fromAccountId);
        transaction.setToAccountId(toAccountId);
        transaction.setDescription(description);
        transaction.setCreatedAt(LocalDateTime.now());

        return transactionRepository.save(transaction);

    }

    // 1. TRANSFER
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {

        BigDecimal amount = request.amount();

        if (!accountRepository.existsById(request.fromAccountId())) {
            throw new AccountNotFoundException(request.fromAccountId());
        }

        if (!accountRepository.existsById(request.toAccountId())) {
            throw new AccountNotFoundException(request.toAccountId());
        }

        // 1. Debit sender first
        int updated = accountRepository.changeBalance(
                request.fromAccountId(),
                amount.negate());

        if (updated == 0) {
            throw new InsufficientBalanceException(request.fromAccountId());
        }

        // 2. Credit receiver
        int credited = accountRepository.changeBalance(
                request.toAccountId(),
                amount);

        if (credited == 0) {
            throw new AccountNotFoundException(request.toAccountId());
        }

        // 3. Record transaction
        Transaction saved = createAndSaveTransaction(
                TransactionType.TRANSFER,
                amount,
                request.fromAccountId(),
                request.toAccountId(),
                request.description());

        return mapToTransactionResponse(saved);
    }

    // 2. WITHDRAW
    @Transactional
    public TransactionResponse withdraw(WithdrawRequest request) {
        BigDecimal amount = request.amount();

        if (!accountRepository.existsById(request.fromAccountId())) {
            throw new AccountNotFoundException(request.fromAccountId());
        }

        int updated = accountRepository.changeBalance(
                request.fromAccountId(),
                amount.negate());

        if (updated == 0) {
            throw new InsufficientBalanceException(request.fromAccountId());
        }

        Transaction saved = createAndSaveTransaction(
                TransactionType.WITHDRAW,
                amount,
                request.fromAccountId(),
                null,
                request.description());

        return mapToTransactionResponse(saved);
    }

    // 3. DEPOSIT
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        BigDecimal amount = request.amount();

        if (!accountRepository.existsById(request.toAccountId())) {
            throw new AccountNotFoundException(request.toAccountId());
        }

        int updated = accountRepository.changeBalance(
                request.toAccountId(),
                amount);

        if (updated == 0) {
            throw new AccountNotFoundException(request.toAccountId());
        }

        Transaction saved = createAndSaveTransaction(
                TransactionType.DEPOSIT,
                amount,
                null,
                request.toAccountId(),
                request.description());

        return mapToTransactionResponse(saved);
    }

    public List<TransactionResponse> getAllTransactions() {
        return StreamSupport
                .stream(transactionRepository.findAll().spliterator(), false)
                .map(this::mapToTransactionResponse)
                .toList();
    }

    public TransactionResponse getTransactionById(long id) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        return mapToTransactionResponse(transaction);
    }

    public void dummy(String name) {
    }

}
