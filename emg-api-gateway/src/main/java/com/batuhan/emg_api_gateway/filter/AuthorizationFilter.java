package com.batuhan.emg_api_gateway.filter;

import com.batuhan.emg_api_gateway.util.JwtValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthorizationFilter implements GlobalFilter, Ordered {

    private final JwtValidator jwtValidator;

    public static final List<String> COMPLETELY_OPEN_ENDPOINTS = List.of(
            "/api/account/login",
            "/api/account/v1/accounts"
    );

    public static final List<String> PUBLIC_GET_ENDPOINTS = List.of(
            "/api/product/v1/products",
            "/api/product/v1/products/"
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();
        String path = request.getPath().toString();
        HttpMethod method = request.getMethod();

        boolean isCompletelyOpen = COMPLETELY_OPEN_ENDPOINTS.stream().anyMatch(path::equals);

        if (isCompletelyOpen) {
            return chain.filter(exchange);
        }

        boolean isPublicGet = PUBLIC_GET_ENDPOINTS.stream().anyMatch(path::equals);

        if (isPublicGet && method == HttpMethod.GET) {
            return chain.filter(exchange);
        }

        if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
            return onError(exchange, "Authorization header missing", HttpStatus.UNAUTHORIZED);
        }

        String authHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
        String jwt;
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
        } else {
            return onError(exchange, "Bearer token format is invalid", HttpStatus.UNAUTHORIZED);
        }

        if (!jwtValidator.validateToken(jwt)) {
            return onError(exchange, "JWT Token is invalid or expired", HttpStatus.UNAUTHORIZED);
        }

        String username = jwtValidator.extractUsername(jwt);
        ServerHttpRequest modifiedRequest = request.mutate()
                .header("X-Auth-User", username)
                .build();

        return chain.filter(exchange.mutate().request(modifiedRequest).build());
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(httpStatus);
        response.getHeaders().add("Content-Type", "application/json");
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -100;
    }
}