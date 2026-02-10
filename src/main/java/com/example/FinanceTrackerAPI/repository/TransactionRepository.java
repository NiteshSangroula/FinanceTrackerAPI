package com.example.FinanceTrackerAPI.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.FinanceTrackerAPI.entity.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

}
