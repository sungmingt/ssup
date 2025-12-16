package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostListResponse;
import com.ssup.backend.domain.post.dto.PostSliceResponse;
import com.ssup.backend.domain.post.sort.PostSortType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class PostServiceSortTest {

    @Autowired
    private PostService postService;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @DisplayName("최신순 첫 페이지 조회 - 성공")
    @Test
    void firstSearch_latest_success() {
        //given
        PostSliceResponse response = postService.findList(PostSortType.LATEST, null, null, 3);

        assertThat(response.getItems()).hasSize(3);
        assertThat(response.isHasNext()).isTrue();
        assertThat(response.getNextCursorKey()).isNull();
        assertThat(response.getNextCursorId()).isNotNull();
    }

    @DisplayName("조회수순 첫 페이지 조회 - 성공")
    @Test
    void firstSearch_sortByViews_success() {
        PostSliceResponse result =
                postService.findList(
                        PostSortType.VIEWS,
                        null,
                        null,
                        3
                );

        assertThat(result.getItems()).hasSize(3);
        assertThat(result.getItems().get(0).getViewCount()).isEqualTo(300);
        assertThat(result.getItems().get(2).getViewCount()).isEqualTo(250);

        assertThat(result.getNextCursorId()).isNotNull();
        assertThat(result.getNextCursorKey()).isEqualTo(250);
    }

    @DisplayName("조회수순 두번째 페이지 조회 - 성공(중복x, 다음 cursor 리턴)")
    @Test
    void secondSearch_sortByViews_success() {
        //given
        PostSliceResponse first =
                postService.findList(
                        PostSortType.VIEWS,
                        null,
                        null,
                        3
                );

        //when
        PostSliceResponse second =
                postService.findList(
                        PostSortType.VIEWS,
                        first.getNextCursorKey(),
                        first.getNextCursorId(),
                        3
                );

        //then
        assertThat(second.getItems()).hasSize(3);

        //중복 검증 (첫번째 조회와 중복이 없어야 한다)
        assertThat(second.getItems())
                .extracting(PostListResponse::getId)
                .doesNotContainAnyElementsOf(
                        first.getItems()
                                .stream()
                                .map(PostListResponse::getId)
                                .toList()
                );
    }

    @DisplayName("마지막 페이지 조회 - hasNext=false")
    @Test
    void lastPage_hasNext_isFalse() {
        PostSliceResponse first =
                postService.findList(
                        PostSortType.LATEST,
                        null,
                        null,
                        4
                );

        PostSliceResponse second =
                postService.findList(
                        PostSortType.LATEST,
                        null,
                        first.getNextCursorId(),
                        4
                );

        assertThat(second.isHasNext()).isFalse();
    }

    //=== setup ===

    @BeforeEach
    void setUp() {
        User user = getUser();
        createPost(user, 300);
        createPost(user, 300);

        createPost(user, 250);
        createPost(user, 220);

        createPost(user, 200);
        createPost(user, 150);
    }

    private User getUser() {
        User user = User.builder()
                .email("test@test.com")
                .nickname("tester")
                .build();

        return userRepository.save(user);
    }

    private Post createPost(User user, int viewCount) {
        Post post = Post.builder()
                .author(user)
                .title("title")
                .content("content")
                .viewCount(viewCount)
                .build();

        return postRepository.save(post);
    }


}
