package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.ssup.backend.global.exception.ErrorCode.TOKEN_REISSUED;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "사용자 프로필 API")
public class AuthController {

    private final AuthService authService;
    private final AppUserProvider appUserProvider;

    @GetMapping("/me")
    public MeResponse me() {
        return authService.me(appUserProvider.getUserId());
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/reissue")
    public TokenReissueResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return TokenReissueResponse.of(TOKEN_REISSUED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.login(loginRequest, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }
}