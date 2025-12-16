package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class PostRepositoryTest {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private TestEntityManager em;

    @DisplayName("최신순 첫 조회(cursor X) - 성공")
    @Test
    void findList_latest_firstFind_success() {
        //given
        Pageable pageable = PageRequest.of(0, 3);

        //when
        List<Post> posts = postRepository.findByViews(
                null,
                null,
                pageable
        );

        //then
        assertThat(posts).hasSize(3);
        assertThat(posts.get(0).getViewCount()).isEqualTo(300);
        assertThat(posts.get(1).getViewCount()).isEqualTo(300);
        assertThat(posts.get(2).getViewCount()).isEqualTo(250);
    }

    @DisplayName("최신순 cursor 기반 조회 - 성공")
    @Test
    void findList_latest_success() {
        //given
        Pageable pageable = PageRequest.of(0, 3);

        List<Post> firstPage = postRepository.findLatest(
                null,
                pageable
        );

        Post lastPost = firstPage.get(2);

        //when
        List<Post> secondPage = postRepository.findLatest(
                lastPost.getId(),
                pageable
        );

        //then
        assertThat(secondPage)
                .extracting(Post::getId)
                .doesNotContainAnyElementsOf(
                        firstPage.stream()
                                .map(Post::getId)
                                .toList()
                );
    }

    @DisplayName("조회수순 조회 - 다음 페이지 cursor 기반 중복 없이 조회 - 성공")
    @Test
    void findList_orderByViewCount_noDuplication_success() {
        //given
        Pageable pageable = PageRequest.of(0, 3);

        //첫 페이지
        List<Post> firstPage = postRepository.findByViews(
                null,
                null,
                pageable
        );

        Post lastPost = firstPage.get(2); // viewCount = 250

        //when
        List<Post> secondPage = postRepository.findByViews(
                lastPost.getViewCount(),
                lastPost.getId(),
                pageable
        );

        //then
        assertThat(secondPage).hasSize(3);

        //중복 검증
        assertThat(secondPage)
                .extracting(Post::getId)
                .doesNotContainAnyElementsOf(
                        firstPage.stream()
                                .map(Post::getId)
                                .toList()
                );

        //순서 검증
        assertThat(secondPage.get(0).getViewCount()).isEqualTo(250);
        assertThat(secondPage.get(1).getViewCount()).isEqualTo(200);
    }

    //=== init ===
    @BeforeEach
    void setUp() {
        User author = initAndGetUser();

        // viewCount 기준 정렬 테스트용
        getPost(1, 300, author);
        getPost(2, 300, author);
        getPost(3, 250, author);
        getPost(4, 250, author);
        getPost(5, 200, author);
        getPost(6, 200, author);
        getPost(7, 150, author);

        em.flush();
        em.clear();
    }

    private Post getPost(long id, int viewCount, User author) {
        Post post = Post.builder()
                .title("title " + id)
                .content("content")
                .author(author)
                .viewCount(viewCount)
                .build();

        em.persist(post);
        return post;
    }

    private User initAndGetUser() {
        User user = User.builder()
                .email("email123@gmail.com")
                .build();

        return em.persist(user);
    }
}
