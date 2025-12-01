package com.batuhan.emg_api_gateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class InternalSecretFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(InternalSecretFilter.class);
    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";

    @Value("${INTERNAL_ACCESS_SECRET}")
    private String internalSecretRaw;

    private volatile String internalSecret;

    public InternalSecretFilter() {}

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (this.internalSecret == null) {

            if (internalSecretRaw == null || internalSecretRaw.trim().isEmpty()) {
                log.error("FATAL CONFIG ERROR: INTERNAL_ACCESS_SECRET environment variable NOT RESOLVED (Value is empty).");
                this.internalSecret = "";
            } else {
                this.internalSecret = internalSecretRaw.trim();
            }

            log.info("DIAGNOSTIC: Gateway Secret successfully loaded via Field Injection. Length: {}", this.internalSecret.length());
        }

        ServerHttpRequest request = exchange.getRequest();
        ServerHttpRequest modifiedRequest = request.mutate()
                .header(INTERNAL_SECRET_HEADER, this.internalSecret)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    @Override
    public int getOrder() {
        return -200;
    }
}