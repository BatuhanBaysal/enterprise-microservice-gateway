package com.batuhan.emg_service_account;

import com.batuhan.emg_service_account.controller.AccountController;
import com.batuhan.emg_service_account.controller.AuthenticationController;
import com.batuhan.emg_service_account.mapper.AccountMapper;
import com.batuhan.emg_service_account.service.AccountServiceImpl;
import com.batuhan.emg_service_account.service.JwtService;
import com.batuhan.emg_service_account.repository.AccountRepository;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

@WebMvcTest(
        controllers = {
                AccountController.class,
                AuthenticationController.class
        }
)
class EmgServiceAccountApplicationTests {

    @MockBean
    private AccountServiceImpl accountServiceImpl;

    @MockBean
    private AccountMapper accountMapper;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @Test
    void contextLoads() {

    }
}