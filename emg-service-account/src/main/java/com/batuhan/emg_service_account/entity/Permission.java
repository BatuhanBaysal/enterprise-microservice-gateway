package com.batuhan.emg_service_account.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Permission {
    PRODUCT_READ("product:read"),
    PRODUCT_CREATE("product:create"),
    PRODUCT_UPDATE("product:update"),
    PRODUCT_DELETE("product:delete");

    private final String permission;
}