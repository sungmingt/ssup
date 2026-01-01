package com.ssup.backend.domain.match;

import com.ssup.backend.domain.match.dto.MatchRequestResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matches")
@Tag(name = "Match", description = "매치 요청 API")
public class MatchController {

    @Operation(summary = "매치 요청 목록 조회", description = "사용자 매치 요청 조회")
    @PostMapping
    public MatchRequestResponse requestMatch() {


        return new MatchRequestResponse();
    }

    @Operation(summary = "매치 요청 목록 조회", description = "사용자 매치 요청 조회")
    @GetMapping("/requested")
    public List<MatchRequestResponse> getMatchRequestsSent() {


        return List.of(new MatchRequestResponse());
    }

    @Operation(summary = "받은 매치 요청 목록 조회", description = "사용자가 받은 매치 요청 조회")
    @GetMapping("/received")
    public List<MatchRequestResponse> getMatchReceived() {


        return List.of(new MatchRequestResponse());
    }
}
