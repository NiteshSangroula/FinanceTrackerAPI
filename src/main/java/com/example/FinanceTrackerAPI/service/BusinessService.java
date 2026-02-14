package com.example.FinanceTrackerAPI.service;

import com.example.FinanceTrackerAPI.entity.Account;

public class BusinessService {
    AccountService accountService;

        public BusinessService(AccountService accountService) {
            this.accountService = accountService;
        }

        public void dummy(String description) {
            System.out.println("Dummy method called with description: " + description);
            accountService.dangerousWork(Long.parseLong(description));
        }

        public void objectMethod(Account account) {
            System.out.println("Object method called with: " + account);
            accountService.dangerousWork1(account);
        }
}
