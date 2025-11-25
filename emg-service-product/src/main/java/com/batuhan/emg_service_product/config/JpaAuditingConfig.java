package com.batuhan.emg_service_product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "gatewayAuditorAware")
public class JpaAuditingConfig {

}