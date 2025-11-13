package com.batuhan.emg_service_product.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

@Component
public class InternalAccessFilter extends OncePerRequestFilter {

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";
    private static final String AUTH_USER_HEADER = "X-Auth-User";

    @Value("${service.internal.secret}")
    private String internalSecret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String incomingSecret = request.getHeader(INTERNAL_SECRET_HEADER);
        final String authUser = request.getHeader(AUTH_USER_HEADER);

        if (incomingSecret == null || !incomingSecret.equals(internalSecret)) {
            sendErrorResponse(response, "Access denied. Request did not originate from Gateway or provided wrong internal key.", HttpStatus.FORBIDDEN);
            return;
        }

        if (authUser == null || authUser.isEmpty() || authUser.equalsIgnoreCase("ANONYMOUS")) {
            sendErrorResponse(response, "Access denied. User authentication token missing or invalid.", HttpStatus.UNAUTHORIZED);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                authUser,
                null,
                Collections.emptyList()
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}