package com.batuhan.emg_service_product.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

@Configuration
public class GatewayAuditorAware implements AuditorAware<String> {

    private static final String AUTH_USER_HEADER = "X-Auth-User";

    @Override
    public Optional<String> getCurrentAuditor() {
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();

        if (servletRequestAttributes == null) {
            return Optional.of("SYSTEM_USER");
        }

        String username = servletRequestAttributes.getRequest().getHeader(AUTH_USER_HEADER);
        return Optional.ofNullable(username).or(() -> Optional.of("ANONYMOUS"));
    }
}