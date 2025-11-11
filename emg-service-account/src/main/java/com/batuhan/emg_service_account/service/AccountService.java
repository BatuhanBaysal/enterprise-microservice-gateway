package com.batuhan.emg_service_account.service;

import com.batuhan.emg_service_account.entity.AccountEntity;

import java.util.List;

public interface AccountService {

    AccountEntity createAccount(AccountEntity accountEntity);
    AccountEntity getAccountById(Long id);
    List<AccountEntity> getAllAccounts();
    AccountEntity getAccountByUsername(String username);
    AccountEntity getAccountByEmail(String email);
    AccountEntity getActiveAccountById(Long id);
    AccountEntity updateAccount(Long id, AccountEntity accountEntity);
    void deleteAccount(Long id);
}