package com.ssup.backend.domain.comment;

import com.ssup.backend.domain.comment.dto.CommentCreateRequest;
import com.ssup.backend.domain.comment.dto.CommentListResponse;
import com.ssup.backend.domain.comment.dto.CommentResponse;
import com.ssup.backend.domain.comment.dto.CommentUpdateRequest;
import com.ssup.backend.domain.heart.comment.CommentHeartService;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.ssup.backend.domain.comment.CommentValidator.validateComment;
import static com.ssup.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentService {

    private final CommentRepository commentRepository;
    private final CommentHeartService commentHeartService;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ImageStorage imageStorage;

    public CommentResponse create(Long userId, Long postId, MultipartFile image, CommentCreateRequest request) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        String imageUrl = imageStorage.upload(ImageType.COMMENT, image);
        post.increaseCommentCount();

        Comment comment = Comment.builder()
                .post(post)
                .author(user)
                .content(request.getContent())
                .imageUrl(imageUrl)
                .deleted(false)
                .build();

        Comment savedComment = commentRepository.save(comment);
        return CommentResponse.of(savedComment);
    }

    public CommentResponse update(Long userId, Long postId, Long commentId, MultipartFile image, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        //validate
        validateComment(comment, postId, userId);

        //update
        comment.updateContent(request.getContent().trim());
        updateImage(image, request.isRemoveImage(), comment);

        return CommentResponse.of(comment);
    }

    public void delete(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SsupException(COMMENT_NOT_FOUND));

        validateComment(comment, postId, userId);

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        post.decreaseCommentCount();

        comment.softDelete();
    }

    @Transactional(readOnly = true)
    public CommentResponse find(Long userId, Long postId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new SsupException(COMMENT_NOT_FOUND));

        if (!comment.getPost().getId().equals(postId) || comment.isDeleted()) {
            throw new SsupException(USER_NOT_FOUND);
        }

        return CommentResponse.of(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentListResponse> findList(Long userId, Long postId) {
        List<Comment> comments = commentRepository.findByPostIdAndDeletedFalseOrderByCreatedAtAsc(postId);

        Set<Long> heartedCommentIds = new HashSet<>();
        if (userId != null) {
            heartedCommentIds = commentHeartService.findHeartedCommentIds(userId, comments);
        }

        return CommentListResponse.of(comments, heartedCommentIds);
    }

    private void updateImage(MultipartFile image, boolean removeImage, Comment comment) {
        String prevImageUrl = comment.getImageUrl();

        //새로운 이미지가 있다면 -> 교체
        if (image != null && !image.isEmpty()) {

            String newImageUrl = imageStorage.upload(ImageType.COMMENT, image);
            if (prevImageUrl != null) {
                imageStorage.deleteByUrl(prevImageUrl);
            }

            comment.updateImageUrl(newImageUrl);
        } else if (removeImage) { //새로운 이미지가 없고, 삭제의 의미라면
            comment.updateImageUrl(null);
        }
    }
}
