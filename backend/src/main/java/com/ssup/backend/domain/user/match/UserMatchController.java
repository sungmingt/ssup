package com.ssup.backend.domain.user.match;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.match.MatchService;
import com.ssup.backend.domain.match.dto.MatchListResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@Tag(name = "User Match", description = "유저 매치 상태 API")
public class UserMatchController {

    private final MatchService matchService;
    private final AppUserProvider appUserProvider;

    @GetMapping("/me/matches")
    public List<MatchListResponse> findUserMatchHistory() {
        return matchService.findUserMatchHistory(appUserProvider.getUserId());
    }
}
