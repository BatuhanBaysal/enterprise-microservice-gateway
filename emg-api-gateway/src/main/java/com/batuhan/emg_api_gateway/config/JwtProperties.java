package com.batuhan.emg_api_gateway.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Data
@Component
public class JwtProperties {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;
}