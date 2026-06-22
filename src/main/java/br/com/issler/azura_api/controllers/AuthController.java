package br.com.issler.azura_api.controllers;

import br.com.issler.azura_api.dtos.LoginDTO;
import br.com.issler.azura_api.dtos.RegisterDTO;
import br.com.issler.azura_api.dtos.TokenResponse;
import br.com.issler.azura_api.services.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {
    private final AuthService authService;

    @Value("${jwt.expiration}")
    private long expirationTime;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@Valid @RequestBody RegisterDTO registerDTO) throws Exception {
        authService.register(registerDTO);
    }

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public TokenResponse login(@Valid @RequestBody LoginDTO loginDTO) throws Exception {
        String token = authService.login(loginDTO);

        return TokenResponse.builder()
                .token(token)
                .expires_in(expirationTime)
                .build();
    }
}
