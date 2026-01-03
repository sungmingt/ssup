package com.ssup.backend.domain.match;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.match.dto.MatchAcceptResponse;
import com.ssup.backend.domain.match.dto.MatchCreateRequest;
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

    @PostMapping
    public ResponseEntity<Void> createMatchRequest(@RequestBody MatchCreateRequest request) {
        matchService.createRequest(appUserProvider.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //매치 요청 수락 (수락 시 상대방의 연락처 응답)
    @PostMapping("/{id}/accept")
    public ResponseEntity<MatchAcceptResponse> acceptMatchRequest(@PathVariable("id") Long id) {
        MatchAcceptResponse response = matchService.acceptRequest(appUserProvider.getUserId(), id);
        return ResponseEntity.ok(response);
    }

//    //매치 요청 거절
//    @PostMapping("/{id}/reject")
//    public ResponseEntity<Void> rejectMatchRequest(@PathVariable("id") Long id) {
//        matchService.reject(appUserProvider.getUserId(), id);
//        return ResponseEntity.ok().build();
//    }
}
