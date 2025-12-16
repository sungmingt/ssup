package com.ssup.backend.domain.post;

import com.ssup.backend.domain.post.dto.*;
import com.ssup.backend.domain.post.sort.PostSliceFetcher;
import com.ssup.backend.domain.post.sort.PostSortType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserService;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ssup.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostSliceFetcher postSliceFetcher;
    private final UserService userService;
    private final ImageStorage imageStorage;

    //todo: userId -> appUser
    public PostResponse create(Long userId, List<MultipartFile> images, PostCreateRequest request) {
        Post post = request.toEntity();
        List<String> imageUrls = imageStorage.uploadMultiple(ImageType.POST, images);
        post.addImages(imageUrls);

        User user = userService.findUserById(userId);
        post.setAuthor(user);

        Post savedPost = postRepository.save(post);
        return PostResponse.of(user, savedPost);
    }

    public PostResponse update(Long userId, Long postId, List<MultipartFile> addedImages, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        updateImages(addedImages, request.getKeepImageUrls(), post);
        post.update(request.getTitle(), request.getContent());

        return PostResponse.of(post.getAuthor(), post);
    }

    @Transactional(readOnly = true)
    public PostSliceResponse findList(PostSortType sort,
                                      Long cursorKey,
                                      Long cursorId,
                                      int size) {

        return postSliceFetcher.fetch(sort, cursorKey, cursorId, size);
    }

    @Transactional(readOnly = true)
    public PostResponse find(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        post.increaseViewCount();
        User author = post.getAuthor();
        return PostResponse.of(author, post);
    }

    public void delete(Long id) {
        postRepository.deleteById(id);

        //todo: 연관 comment, heart, images 모두 delete
    }

    //=====

    private void updateImages(List<MultipartFile> addedImages, List<String> keepImageUrls, Post post) {
        //삭제 대상 이미지 계산
        List<String> removedImages = post.getImageUrls().stream()
                .filter(url -> !keepImageUrls.contains(url))
                .toList();

        //S3에서 삭제
        removedImages.forEach(imageStorage::deleteByUrl);

        //기존 이미지 교체
        post.replaceImages(keepImageUrls);

        //새 이미지 업로드 후 추가
        if (!addedImages.isEmpty()) {
            List<String> addedImageUrls = imageStorage.uploadMultiple(ImageType.POST, addedImages);
            post.addImages(addedImageUrls);
        }
    }
}
