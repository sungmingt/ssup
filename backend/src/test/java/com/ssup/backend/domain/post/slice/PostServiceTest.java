package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private PostService postService;

    private final User user =
            User.builder()
                    .id(1L)
                    .email("minwoo123@gmail.com")
                    .contact("010-1234-1234")
                    .nickname("민우")
                    .build();

    private final Post post =
            Post.builder()
                    .id(1L)
                    .title("안녕하세요 반가워요")
                    .content("하이 한국어 공부하고 있어요~")
                    .author(user)
                    .build();

    @DisplayName("게시글을 조회한다 - 성공")
    @Test
    void findPost_ok() {
        // given
        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        // when
        PostResponse response = postService.findPost(1L);

        // then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("안녕하세요 반가워요");
        assertThat(response.getAuthorName()).isEqualTo("민우");

        then(postRepository)
                .should(times(1))
                .findById(1L);
    }

    //todo: test 복원
}
