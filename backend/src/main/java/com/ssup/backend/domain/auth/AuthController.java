package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.*;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
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
    public ResponseEntity<MeResponse> me() {
        //인증이 필요한 API 호출에만 재발급 요청이 가도록, 로그인/비로그인 모두 정상응답.
        //프론트에서는 인증 여부만 알면 된다 (user or null)
        try {
            Long userId = appUserProvider.getUserId();
            if (userId == null) return ResponseEntity.ok(null);
            return ResponseEntity.ok(authService.me(userId));
        } catch (Exception e) {
            return ResponseEntity.ok(null);
        }
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @PostMapping("/reissue")
    public TokenReissueResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return TokenReissueResponse.of(TOKEN_REISSUED);
    }

    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.login(loginRequest, response);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/quit")
    public ResponseEntity<Void> quit(HttpServletRequest request, HttpServletResponse response) {
        authService.delete(appUserProvider.getUserId(), request, response);
        return ResponseEntity.noContent().build();
    }
}