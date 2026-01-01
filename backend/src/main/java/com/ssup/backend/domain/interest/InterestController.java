package com.ssup.backend.domain.interest;

import com.ssup.backend.domain.interest.dto.InterestResponse;
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

    @GetMapping
    public List<InterestResponse> getAll() {
        return interestService.findAll();
    }
}