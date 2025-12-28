package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.auth.CurrentUser;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostHeartController {

    private final PostHeartFacade postHeartFacade;

    @PostMapping("/{postId}/hearts")
    public HeartResponse toggleHeart(@PathVariable("postId") Long postId,
                                     @CurrentUser AppUser appUser) {
        return postHeartFacade.tryToggleHeart(appUser.getId(), postId);
    }
}
