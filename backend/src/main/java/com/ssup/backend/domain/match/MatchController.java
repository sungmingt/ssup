package com.ssup.backend.domain.match;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.match.dto.MatchAcceptResponse;
import com.ssup.backend.domain.match.dto.MatchCreateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
@Tag(name = "Match", description = "매치 요청 API")
public class MatchController {

    private final MatchService matchService;
    private final AppUserProvider appUserProvider;

    @Operation(summary = "매치 요청", description = "상대 유저에게 매치 요청을 보낸다.")
    @PostMapping
    public ResponseEntity<Void> createMatchRequest(@RequestBody MatchCreateRequest request) {
        matchService.createRequest(appUserProvider.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "매치 수락 요청", description = "받은 매치 요청을 수락")
    @PutMapping("/{id}/accept")
    public ResponseEntity<MatchAcceptResponse> acceptMatchRequest(@PathVariable("id") Long id) {
        MatchAcceptResponse response = matchService.acceptRequest(appUserProvider.getUserId(), id);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "매치 거절 요청", description = "받은 매치 요청을 거절")
    @PutMapping("/{id}/reject")
    public ResponseEntity<Void> rejectMatchRequest(@PathVariable("id") Long id) {
        matchService.rejectRequest(appUserProvider.getUserId(), id);
        return ResponseEntity.ok().build();
    }
}
