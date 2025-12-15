package com.ssup.backend.domain.post;

import com.ssup.backend.domain.post.dto.PostCreateRequest;
import com.ssup.backend.domain.post.dto.PostListResponse;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.post.dto.PostUpdateRequest;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserService;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import com.ssup.backend.infra.s3.S3ImageStorage;
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
    private final UserService userService;
    private final ImageStorage imageStorage;

    //todo: userId -> appUser
    public PostResponse create(Long userId, List<MultipartFile> images, PostCreateRequest request) {
        Post post = request.toEntity();
        List<String> imageUrls = imageStorage.uploadMultiple(ImageType.POST, images);
        post.setImages(imageUrls);

        User user = userService.findUserById(userId);
        post.setAuthor(user);

        Post savedPost = postRepository.save(post);
        return PostResponse.of(user, savedPost);
    }

    public PostResponse update(Long userId, Long postId, List<MultipartFile> addedImages, PostUpdateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        List<String> newImages = imageStorage.updateMultiple(request.getKeepImages(), addedImages);

        post.setImages(newImages);

        return PostResponse.of(post.getAuthor(), post);
    }


    public List<PostListResponse> findList() {
        List<Post> postList = postRepository.findAll();
        return PostListResponse.of(postList);
    }

    public PostResponse find(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        post.increaseViewCount();
        User author = post.getAuthor();
        return PostResponse.of(author, post);
    }


}
