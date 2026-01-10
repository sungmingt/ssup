package com.ssup.backend.domain.post;

import com.ssup.backend.domain.post.dto.PostFilterMetadataResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/filters")
@Tag(name = "Post Filter API", description = "게시글 필터 API")
public class PostFilterController {

    private final PostFilterService postFilterService;

    @Operation(summary = "필터 조건 목록 조회", description = "게시글 필터 제공에 필요한 location, language, interest 목록을 반환한다.")
    @GetMapping
    public PostFilterMetadataResponse findFilterMetadata() {
        return postFilterService.findFilterMetadata();
    }
}
