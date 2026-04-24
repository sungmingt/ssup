package com.ssup.backend.domain.user.recommend;

import com.ssup.backend.domain.user.recommend.dto.UserRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/recommend")
public class UserRecommendController {

    private final UserRecommendService recommendService;

    @GetMapping("/{userId}")
    public List<UserRecommendResponse> recommend(@PathVariable("userId") Long userId) {
        return recommendService.recommend(userId);
    }

    @GetMapping("/anonymous")
    public List<UserRecommendResponse> recommendAnonymous() {
        return recommendService.recommendAnonymous();
    }
}