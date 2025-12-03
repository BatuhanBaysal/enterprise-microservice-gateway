package com.batuhan.emg_service_account.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class InternalAccessFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(InternalAccessFilter.class);

    private static final String INTERNAL_SECRET_HEADER = "X-Internal-Secret";
    private static final String AUTH_USER_HEADER = "X-Auth-User";
    private static final String AUTH_AUTHORITIES_HEADER = "X-Auth-Authorities";
    private final String internalSecret;

    public InternalAccessFilter(@Value("${INTERNAL_ACCESS_SECRET}") String internalSecretRaw) {
        if (internalSecretRaw == null || internalSecretRaw.trim().isEmpty()) {
            log.error("FATAL CONFIG ERROR: Account Service Internal Secret not resolved by Spring.");
        }
        this.internalSecret = cleanSecret(internalSecretRaw);
        log.info("DIAGNOSTIC: Account Service Internal Secret successfully loaded and cleaned. Length: {}", this.internalSecret.length());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String incomingSecret = request.getHeader(INTERNAL_SECRET_HEADER);
        final String authUser = request.getHeader(AUTH_USER_HEADER);
        final String authAuthorities = request.getHeader(AUTH_AUTHORITIES_HEADER);

        final String cleanedIncomingSecret = cleanSecret(incomingSecret);

        if (internalSecret == null || internalSecret.trim().isEmpty() || !cleanedIncomingSecret.equals(internalSecret)) {
            if (internalSecret == null || internalSecret.trim().isEmpty()) {
                log.error("Internal Secret (EXPECTED) is empty after cleaning. Check Docker config.");
            } else {
                log.error("CRITICAL ERROR DETECTED: Secret mismatch persists! Expected: [{}], Received: [{}]", internalSecret, cleanedIncomingSecret);
                doPostMatchDiagnostic(internalSecret, cleanedIncomingSecret);
            }
            sendErrorResponse(response, "Access denied. Internal key mismatch.", HttpStatus.FORBIDDEN);
            return;
        }

        if (authUser == null || authUser.isEmpty()) {
            sendErrorResponse(response, "Access denied. User authentication token missing or invalid.", HttpStatus.UNAUTHORIZED);
            return;
        }

        Collection<? extends GrantedAuthority> authorities = List.of();

        if (authAuthorities != null && !authAuthorities.isEmpty()) {
            authorities = Arrays.stream(authAuthorities.split(","))
                    .map(String::trim)
                    .filter(auth -> !auth.isEmpty())
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    authUser,
                    null,
                    authorities
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

    private String cleanSecret(String secret) {
        if (secret == null) {
            return "";
        }

        String cleaned = secret.replaceAll("\\P{Print}", "");
        cleaned = cleaned.replaceAll("\"", "");
        return cleaned.trim();
    }

    private void doPostMatchDiagnostic(String expected, String received) {
        String expectedCodes = getCharacterCodes(expected);
        String receivedCodes = getCharacterCodes(received);

        log.error("Expected Length: {} -> Codes: {}", expected.length(), expectedCodes);
        log.error("Received Length: {} -> Codes: {}", received.length(), receivedCodes);
    }

    private String getCharacterCodes(String s) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            sb.append((int) s.charAt(i)).append(" ");
        }
        return sb.toString().trim();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (path.equals("/api/v1/auth/login") || (path.equals("/api/v1/accounts") && request.getMethod().equalsIgnoreCase("POST"))) {
            return true;
        }

        return false;
    }

    private void sendErrorResponse(HttpServletResponse response, String message, HttpStatus status) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"" + message + "\"}");
    }
}