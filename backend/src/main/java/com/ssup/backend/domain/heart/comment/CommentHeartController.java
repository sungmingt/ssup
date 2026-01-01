package com.ssup.backend.domain.heart.comment;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentHeartController {

    private final CommentHeartFacade commentHeartFacade;
    private final AppUserProvider appUserProvider;

    @PostMapping("/{commentId}/hearts")
    public HeartResponse toggleHeart(@PathVariable("commentId") Long commentId) {
        return commentHeartFacade.tryToggleHeart(appUserProvider.getUserId(), commentId);
    }
}