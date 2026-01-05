package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.*;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Auth", description = "사용자 인증 API")
public class AuthController {

    private final AuthService authService;
    private final AppUserProvider appUserProvider;

    @Operation(summary = "유저의 인증 정보 조회", description = "프론트엔드에서 인증 여부 확인을 위한 API")
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

    @Operation(summary = "회원 가입", description = "nickname, email, password를 통해 회원가입한다.")
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public SignUpResponse signUp(@Valid @RequestBody SignUpRequest request) {
        return authService.signUp(request);
    }

    @Operation(summary = "ATK 재발급 요청", description = "RTK 쿠키를 통해 재발급을 진행한다.")
    @PostMapping("/reissue")
    public TokenReissueResponse reissue(HttpServletRequest request, HttpServletResponse response) {
        authService.reissue(request, response);
        return TokenReissueResponse.of(TOKEN_REISSUED);
    }

    @Operation(summary = "로그인 요청", description = "email, password를 통해 로그인한다.")
    @PostMapping("/login")
    public ResponseEntity<Void> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        authService.login(loginRequest, response);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "로그아웃 요청", description = "로그아웃과 인증 쿠키 삭제를 요청한다.")
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        authService.logout(request, response);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "유저의 계정 삭제 요청", description = "유저의 인증 정보를 통해 계정 삭제를 요청한다.글")
    @PostMapping("/quit")
    public ResponseEntity<Void> quit(HttpServletRequest request, HttpServletResponse response) {
        authService.quit(appUserProvider.getUserId(), request, response);
        return ResponseEntity.noContent().build();
    }
}