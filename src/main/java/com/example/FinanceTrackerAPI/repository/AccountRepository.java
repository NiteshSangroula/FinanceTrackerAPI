package com.example.FinanceTrackerAPI.repository;

import org.springframework.data.repository.CrudRepository;

import com.example.FinanceTrackerAPI.entity.Account;

public interface AccountRepository extends CrudRepository<Account, Long> {

}
