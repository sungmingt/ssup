package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.SignUpRequest;
import com.ssup.backend.domain.auth.dto.SignUpResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@Tag(name = "Auth API", description = "사용자 프로필 API")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }
}