package com.batuhan.emg_service_account.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.batuhan.emg_service_account.entity.Permission.*;

@RequiredArgsConstructor
@Getter
public enum Role {

    USER(
            Set.of(
                    PRODUCT_READ
            )
    ),

    ADMIN(
            Set.of(
                    PRODUCT_READ,
                    PRODUCT_CREATE,
                    PRODUCT_UPDATE,
                    PRODUCT_DELETE
            )
    ),

    DEVELOPER(
            Set.of(
                    PRODUCT_READ,
                    PRODUCT_CREATE,
                    PRODUCT_UPDATE,
                    PRODUCT_DELETE
            )
    )
    ;

    private final Set<Permission> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toList());

        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}