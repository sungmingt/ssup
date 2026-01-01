package com.ssup.backend.domain.post;

import com.ssup.backend.domain.heart.post.PostHeartRepository;
import com.ssup.backend.domain.post.dto.*;
import com.ssup.backend.domain.post.sort.PostSliceFetcher;
import com.ssup.backend.domain.post.sort.PostSortType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.aop.CheckUserStatus;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static com.ssup.backend.domain.post.PostValidator.*;
import static com.ssup.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final PostSliceFetcher postSliceFetcher;
    private final PostHeartRepository postHeartRepository;
    private final UserRepository userRepository;
    private final ImageStorage imageStorage;

    @CheckUserStatus(UserStatus.ACTIVE)
    public PostCreateResponse create(Long userId, List<MultipartFile> images, PostCreateRequest request) {
        Post post = request.toEntity();
        List<String> imageUrls = imageStorage.uploadMultiple(ImageType.POST, images);
        post.addImages(imageUrls);

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        post.setAuthor(user);

        Post savedPost = postRepository.save(post);
        return PostCreateResponse.of(user, savedPost);
    }

    @CheckUserStatus(UserStatus.ACTIVE)
    public PostUpdateResponse update(Long userId, Long postId, List<MultipartFile> addedImages, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        validatePostOwner(userId, post.getAuthor().getId());
        updateImages(addedImages, request.getKeepImageUrls(), post);
        post.update(request.getTitle(), request.getContent(), request.getUsingLanguage(), request.getLearningLanguage());

        return PostUpdateResponse.of(post.getAuthor(), post);
    }

    @Transactional(readOnly = true)
    public PostSliceResponse findList(Long userId,
                                      PostSortType sort,
                                      Long cursorKey,
                                      Long cursorId,
                                      int size) {

        return postSliceFetcher.fetch(userId, sort, cursorKey, cursorId, size);
    }

    public PostResponse find(Long userId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        post.increaseViewCount();
        User author = post.getAuthor();

        boolean heartedByMe = false;

        if (userId != null) {
            heartedByMe = postHeartRepository.existsByPostIdAndUserId(id, userId);
        }

        return PostResponse.of(author, post, heartedByMe);
    }

    @CheckUserStatus(UserStatus.ACTIVE)
    public void delete(Long userId, Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        validatePostOwner(userId, post.getAuthor().getId());
        postRepository.deleteById(id);
    }

    //=====

    private void updateImages(List<MultipartFile> addedImages, List<String> keepImageUrls, Post post) {
        //삭제 대상 이미지 추출
        List<String> removedImages = post.getImageUrls().stream()
                .filter(url -> !keepImageUrls.contains(url))
                .toList();

        //S3에서 삭제
        removedImages.forEach(imageStorage::deleteByUrl);

        //기존 이미지 교체
        post.replaceImages(keepImageUrls);

        //새 이미지 업로드 후 추가
        if (addedImages != null && !addedImages.isEmpty()) {
            List<String> addedImageUrls = imageStorage.uploadMultiple(ImageType.POST, addedImages);
            post.addImages(addedImageUrls);
        }
    }
}
