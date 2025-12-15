package com.ssup.backend.domain.post;

import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static com.ssup.backend.global.exception.ErrorCode.POST_NOT_FOUND;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(value = "게시글 통합 테스트")
@Transactional
class PostTest {

    @Autowired
    PostService postService;

    @Autowired
    PostRepository postRepository;

    @Autowired
    UserRepository userRepository;

    @Test
    @DisplayName("게시글 조회 - 조회수 1 증가")
    void findPost_increaseViewCount() {
        //given
        User user = userRepository.save(
                User.builder()
                        .nickname("sam")
                        .email("sam123@gmail.com")
                        .build()
        );

        Post post = postRepository.save(
                Post.builder()
                        .title("제목")
                        .content("내용")
                        .usingLanguage("EN")
                        .learningLanguage("KR")
                        .author(user)
                        .viewCount(0)
                        .build()
        );

        //when
        PostResponse response = postService.find(post.getId());

        //then
        Post updatedPost = postRepository.findById(post.getId()).
                orElseThrow(() -> new SsupException(POST_NOT_FOUND));

        assertThat(updatedPost.getViewCount()).isEqualTo(1);
        assertThat(response.getAuthorName()).isEqualTo("sam");
        assertThat(response.getTitle()).isEqualTo("제목");
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 - 예외")
    void findPost_notFound() {
        assertThatThrownBy(() -> postService.find(999L))
                .isInstanceOf(SsupException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.POST_NOT_FOUND);
    }
}
