package com.batuhan.emg_service_account.controller;

import com.batuhan.emg_service_account.dto.AccountCreateRequest;
import com.batuhan.emg_service_account.dto.AccountResponse;
import com.batuhan.emg_service_account.entity.AccountEntity;
import com.batuhan.emg_service_account.mapper.AccountMapper;
import com.batuhan.emg_service_account.service.AccountService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;
    private final AccountMapper accountMapper;

    @PostMapping
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountCreateRequest request) {
        AccountEntity entityToCreate = accountMapper.toEntity(request);
        AccountEntity createdEntity = accountService.createAccount(entityToCreate);
        AccountResponse response = accountMapper.toResponse(createdEntity);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountEntity entity = accountService.getAccountById(id);
        AccountResponse response = accountMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountEntity> entities = accountService.getAllAccounts();
        List<AccountResponse> responses = entities.stream()
                .map(accountMapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    @GetMapping("/username/{username}")
    public ResponseEntity<AccountResponse> getAccountByUsername(@PathVariable String username) {
        AccountEntity entity = accountService.getAccountByUsername(username);
        AccountResponse response = accountMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<AccountResponse> getAccountByEmail(@PathVariable String email) {
        AccountEntity entity = accountService.getAccountByEmail(email);
        AccountResponse response = accountMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/active/{id}")
    public ResponseEntity<AccountResponse> getActiveAccountById(@PathVariable Long id) {
        AccountEntity entity = accountService.getActiveAccountById(id);
        AccountResponse response = accountMapper.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id,
            @Valid @RequestBody AccountCreateRequest request) {
        AccountEntity entityToUpdate = accountMapper.toEntity(request);
        AccountEntity updatedEntity = accountService.updateAccount(id, entityToUpdate);
        AccountResponse response = accountMapper.toResponse(updatedEntity);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}