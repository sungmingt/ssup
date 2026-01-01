package com.ssup.backend.domain.comment.slice;

import com.ssup.backend.domain.comment.Comment;
import com.ssup.backend.domain.comment.CommentRepository;
import com.ssup.backend.domain.comment.CommentService;
import com.ssup.backend.domain.comment.CommentValidator;
import com.ssup.backend.domain.comment.dto.CommentCreateRequest;
import com.ssup.backend.domain.comment.dto.CommentListResponse;
import com.ssup.backend.domain.comment.dto.CommentResponse;
import com.ssup.backend.domain.comment.dto.CommentUpdateRequest;
import com.ssup.backend.domain.heart.comment.CommentHeartService;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.verify;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class CommentServiceTest {

    @InjectMocks
    private CommentService commentService;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private CommentHeartService commentHeartService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ImageStorage imageStorage;
    @Mock
    private CommentValidator validator;

    @DisplayName("댓글 작성 - 성공")
    @Test
    void createComment_success() {
        //given
        Long userId = 1L;
        Long postId = 1L;

        User user = mock(User.class);
        Post post = mock(Post.class);
        MultipartFile image = mock(MultipartFile.class);

        CommentCreateRequest request = new CommentCreateRequest("댓글 내용");

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));
        given(userRepository.findById(userId))
                .willReturn(Optional.of(user));
        given(imageStorage.upload(ImageType.COMMENT, image))
                .willReturn("image-url");
        given(commentRepository.save(any(Comment.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        //when
        CommentResponse response = commentService.create(userId, postId, image, request);

        //then
        assertThat(response.getContent()).isEqualTo("댓글 내용");
        assertThat(response.getImageUrl()).isEqualTo("image-url");
        verify(commentRepository).save(any(Comment.class));
    }

    @DisplayName("댓글 삭제 시 soft delete 호출 - 성공")
    @Test
    void deleteComment_softDeleteExecution_success() {
        //given
        Long commentId = 1L;
        Long userId = 1L;
        Long postId = 10L;

        Comment comment = mock(Comment.class);
        Post post = mock(Post.class);
        User author = mock(User.class);

        given(author.getId()).willReturn(userId);
        given(post.getId()).willReturn(postId);
        given(comment.getAuthor()).willReturn(author);
        given(comment.getPost()).willReturn(post);

        given(commentRepository.findById(commentId))
                .willReturn(Optional.of(comment));

        given(postRepository.findById(postId))
                .willReturn(Optional.of(post));

        //when
        commentService.delete(userId, postId, commentId);

        //then
        verify(comment).softDelete();
        verify(post).decreaseCommentCount();
    }

    @DisplayName("특정 글의 댓글 목록 조회 - 성공")
    @Test
    void findList_success() {
        //given
        User user = getUser();
        Post post = getPost(10, user);
        Comment comment1 = getComment(post, user, "content1", false);
        Comment comment2 = getComment(post, user, "content2", false);
        List<Comment> commentList = List.of(comment1, comment2);

        given(commentRepository.findByPostIdAndDeletedFalseOrderByCreatedAtAsc(1L))
                .willReturn(commentList);
        given(commentHeartService.findHeartedCommentIds(1L, commentList))
                .willReturn(Set.of());

        //when
        List<CommentListResponse> result = commentService.findList(user.getId(), 1L);

        //then
        assertThat(result).hasSize(2);
    }

    //=== init ===

    private Comment getComment(Post post, User user, String content, boolean deleted) {
        return Comment.builder()
                .id(1L)
                .post(post)
                .author(user)
                .content(content)
                .deleted(deleted)
                .build();
    }

    private Post getPost(int viewCount, User author) {
        return Post.builder()
                .id(1L)
                .title("title ")
                .content("content")
                .author(author)
                .viewCount(viewCount)
                .build();
    }

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("email123@gmail.com")
                .build();
    }
}
