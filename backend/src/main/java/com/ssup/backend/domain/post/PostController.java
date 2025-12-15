package com.ssup.backend.domain.post;

import com.ssup.backend.domain.post.dto.PostCreateRequest;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.post.dto.PostUpdateRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import static org.springframework.http.HttpStatus.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
@Tag(name = "Post", description = "게시글 API")
public class PostController {

    private final PostService postService;

//    @Operation(summary = "글 목록 조회", description = "전체 글 목록 조회")
//    @GetMapping
//    public List<PostListResponse> getPosts() {
//
//        return postService.findList();
//
//    }

    @Operation(summary = "글 조회", description = "글 상세 정보 조회")
    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable("id") Long id) {
        return postService.find(id);
    }

    @Operation(summary = "글 작성", description = "새로운 글 작성")
    @PostMapping
    @ResponseStatus(CREATED)
    public PostResponse createPost(@RequestPart(name = "images", required = false) List<MultipartFile> images,
                                   @RequestPart(name = "dto") @Validated PostCreateRequest request) {

        return postService.create(1L, images, request); //todo: AppUser로부터 userId 추출
    }

    @Operation(summary = "글 수정", description = "작성한 글을 수정")
    @PutMapping("/{id}")
    public PostResponse update(@PathVariable("id") Long id,
                               @RequestPart(name = "images", required = false) List<MultipartFile> images,
                               @RequestPart(name = "dto") @Validated PostUpdateRequest request) {

        return postService.update(1L, id, images, request);
    }

    @Operation(summary = "글 삭제", description = "사용자가 작성한 글을 삭제")
    @DeleteMapping("/{id}")
    public HttpStatus deletePost(@PathVariable("id") Long id) {


        return NO_CONTENT;
    }
}
