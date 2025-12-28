package com.ssup.backend.domain.comment;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.auth.CurrentUser;
import com.ssup.backend.domain.comment.dto.CommentCreateRequest;
import com.ssup.backend.domain.comment.dto.CommentListResponse;
import com.ssup.backend.domain.comment.dto.CommentResponse;
import com.ssup.backend.domain.comment.dto.CommentUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comments")
@Tag(name = "Comment", description = "댓글 API")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "댓글 작성", description = "댓글 작성")
    @PostMapping
    public CommentResponse create(@PathVariable("postId") Long postId,
                                  @CurrentUser AppUser appUser,
                                  @RequestPart(name = "image", required = false) MultipartFile image,
                                  @RequestPart(name = "dto") CommentCreateRequest request) {
        return commentService.create(appUser.getId(), postId, image, request);
    }

    @Operation(summary = "댓글 수정", description = "작성한 댓글 수정")
    @PutMapping("/{id}")
    public CommentResponse update(@PathVariable("postId") Long postId,
                                  @PathVariable("id") Long id,
                                  @CurrentUser AppUser appUser,
                                  @RequestPart(name = "image", required = false) MultipartFile image,
                                  @RequestPart(name = "dto") CommentUpdateRequest request) {
        return commentService.update(appUser.getId(), postId, id, image, request);
    }

    @Operation(summary = "댓글 삭제", description = "작성한 댓글 삭제")
    @DeleteMapping("/{id}")
    public HttpStatus delete(@PathVariable("postId") Long postId,
                             @PathVariable("id") Long id,
                             @CurrentUser AppUser appUser) {
        commentService.delete(appUser.getId(), postId, id);
        return HttpStatus.NO_CONTENT;
    }

    @Operation(summary = "댓글 조회", description = "댓글 조회")
    @GetMapping("/{id}")
    public CommentResponse find(@PathVariable("postId") Long postId,
                                @PathVariable("id") Long id,
                                @CurrentUser AppUser appUser
    ) {
        return commentService.find(appUser.getId(), postId, id);
    }

    @Operation(summary = "특정 글의 댓글 목록 조회", description = "특정 글의 댓글 목록 조회")
    @GetMapping
    public List<CommentListResponse> findList(@PathVariable("postId") Long postId,
                                              @CurrentUser AppUser appUser) {
        return commentService.findList(appUser.getId(), postId);
    }
}
