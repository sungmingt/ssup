package com.ssup.backend.domain.heart.post;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post Heart", description = "게시글 좋아요 API")
public class PostHeartController {

    private final PostHeartFacade postHeartFacade;
    private final AppUserProvider appUserProvider;

    @Operation(summary = "게시글 좋아요 요청", description = "게시글 좋아요 요청")
    @PostMapping("/{postId}/hearts")
    public HeartResponse toggleHeart(@PathVariable("postId") Long postId) {
        return postHeartFacade.tryToggleHeart(appUserProvider.getUserId(), postId);
    }
}
