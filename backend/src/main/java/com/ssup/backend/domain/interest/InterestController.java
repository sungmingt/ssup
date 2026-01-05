package com.ssup.backend.domain.interest;

import com.ssup.backend.domain.interest.dto.InterestResponse;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/interests")
public class InterestController {

    private final InterestService interestService;

    @Operation(summary = "전체 관심사 목록 조회", description = "ssup 서비스에서 등록한 관심사 목록 조회")
    @GetMapping
    public List<InterestResponse> getAll() {
        return interestService.findAll();
    }
}