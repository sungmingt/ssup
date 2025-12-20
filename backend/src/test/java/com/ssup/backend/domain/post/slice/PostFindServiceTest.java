package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.heart.post.PostHeartRepository;
import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserService;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostFindServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private PostHeartRepository postHeartRepository;
    @Mock
    private UserService userService;
    @Mock
    private ImageStorage imageStorage;
    @InjectMocks
    private PostService postService;

    MockMultipartFile mockMultipartFile;
    private final User user = getUser();
    private final Post post = getPost();

    @DisplayName("게시글을 조회한다 - 성공")
    @Test
    void findPost_ok() {
        //given
        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        //when
        PostResponse response = postService.find(1L, 1L);

        //then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("안녕하세요 반가워요");
        assertThat(response.getAuthorName()).isEqualTo("민우");

        then(postRepository)
                .should(times(1))
                .findById(1L);
    }

    @DisplayName("존재하지 않는 게시글을 조회한다 - 실패")
    @Test
    void findPost_fail_notFound() {
        //given
        given(postRepository.findById(1L))
                .willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> postService.find(1L, 1L))
                .isInstanceOf(SsupException.class)
                .hasMessage(ErrorCode.POST_NOT_FOUND.getMessage());

        then(postRepository)
                .should(times(1))
                .findById(1L);
    }

    @DisplayName("게시글을 조회한다 - 조회수 증가")
    @Test
    void findPost_increaseViewCount() {
        //given
        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));
        given(postHeartRepository.existsByPostIdAndUserId(1L, 1L))
                .willReturn(false);

        //when
        postService.find(1L, 1L);

        //then
        assertThat(post.getViewCount()).isEqualTo(1);

        then(postRepository)
                .should(times(1))
                .findById(1L);
    }

    //--- init ---

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("minwoo123@gmail.com")
                .contact("010-1234-1234")
                .nickname("민우")
                .build();
    }

    private Post getPost() {
        return Post.builder()
                .id(1L)
                .title("안녕하세요 반가워요")
                .content("하이 한국어 공부하고 있어요~")
                .imageUrls(List.of("s3.aws.com", "s3.aws.com"))
                .author(user)
                .build();
    }
}
