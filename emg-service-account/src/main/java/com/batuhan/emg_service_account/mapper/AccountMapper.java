package com.batuhan.emg_service_account.mapper;

import com.batuhan.emg_service_account.dto.AccountCreateRequest;
import com.batuhan.emg_service_account.dto.AccountResponse;
import com.batuhan.emg_service_account.entity.AccountEntity;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public AccountEntity toEntity(AccountCreateRequest request) {
        AccountEntity entity = new AccountEntity();
        entity.setFirstName(request.getFirstName());
        entity.setLastName(request.getLastName());
        entity.setUsername(request.getUsername());
        entity.setEmail(request.getEmail());
        entity.setPasswordHash(request.getPassword());
        return entity;
    }

    public AccountResponse toResponse(AccountEntity entity) {
        AccountResponse response = new AccountResponse();
        response.setId(entity.getId());
        response.setFirstName(entity.getFirstName());
        response.setLastName(entity.getLastName());
        response.setUsername(entity.getUsername());
        response.setEmail(entity.getEmail());
        response.setAccountActive(entity.isAccountActive());
        response.setCreatedAt(entity.getCreatedAt());
        response.setUpdatedAt(entity.getUpdatedAt());
        return response;
    }
}