package com.ssup.backend.domain.post;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.interest.InterestCategoryRepository;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
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

@ActiveProfiles("test")
@SpringBootTest
@Transactional
class PostFilterTest {

    @Autowired
    private PostService postService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private InterestRepository interestRepository;
    @Autowired
    private InterestCategoryRepository interestCategoryRepository;
    @Autowired
    private PostRepository postRepository;

    private Location seoul;
    private Location gangnam;
    private Interest coding;

    @BeforeEach
    void setUp() {
        //location setup
        seoul = locationRepository.save(Location.builder()
                .name("서울").level(1).parent(null).build());
        gangnam = locationRepository.save(Location.builder()
                .name("강남구").level(2).parent(seoul).build());

        //interest setup
        InterestCategory tech = interestCategoryRepository.save(InterestCategory.builder()
                .code("TECH").name("기술").build());
        coding = interestRepository.save(Interest.builder()
                .code("CODING").name("코딩").category(tech).build());

        //user setup
        //테스트 유저 A: 서울 거주, 코딩 관심사 -> 게시글 작성
        User userA = User.builder().nickname("서울유저").location(seoul).build();
        userA.addInterest(coding);
        userRepository.save(userA);
        createPost(userA, "서울 유저의 글", "한국어", "English");

        //테스트 유저 B: 강남구 거주 -> 게시글 작성
        User userB = User.builder().nickname("강남유저").location(gangnam).build();
        userRepository.save(userB);
        createPost(userB, "강남구 유저의 글", "English", "日本語");
    }

    //===== tests =====

    @Test
    @DisplayName("상위 지역(서울) 필터 적용 시, 하위 지역(강남구) 게시글까지 필터에 포함되어야 한다.")
    void filterByParentLocation_success() {
        //given
        PostFilterCondition condition = new PostFilterCondition(seoul.getId(), null, null, null);

        //when
        PostSliceResponse response = postService.findList(null, condition, PostSortType.LATEST, null, null, 10);

        //then
        //expect: 서울 유저 글 + 강남구 유저 글 총 2개
        assertThat(response.getItems()).hasSize(2);
        assertThat(response.getItems()).extracting("title")
                .containsExactlyInAnyOrder("서울 유저의 글", "강남구 유저의 글");
    }

    @Test
    @DisplayName("특정 관심사 필터필터가 정상 작동한다")
    void filterByInterest_success() {
        //given
        PostFilterCondition condition = new PostFilterCondition(null, null, null, coding.getId());

        //when
        PostSliceResponse response = postService.findList(null, condition, PostSortType.LATEST, null, null, 10);

        //then
        //expect: 코딩 관심사가 있는 유저 A의 글만 조회
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getTitle()).isEqualTo("서울 유저의 글");
    }

    @Test
    @DisplayName("사용 언어 필터가 정상 작동한다")
    void filterByUsingLanguage_success() {
        //given
        PostFilterCondition condition = new PostFilterCondition(null, "한국어", null, null);

        //when
        PostSliceResponse response = postService.findList(null, condition, PostSortType.LATEST,null, null, 10);

        //then
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getTitle()).isEqualTo("서울 유저의 글");
    }

    @Test
    @DisplayName("여러 필터(위치+언어+관심사) 적용 시, 모든 조건에 부합하는 글만 조회된다")
    void multipleFilter_success() {
        //given
        PostFilterCondition condition = new PostFilterCondition(
                seoul.getId(),
                "한국어",
                "English",
                coding.getId()
        );

        //when
        PostSliceResponse response = postService.findList(null, condition, PostSortType.LATEST,null, null, 10);

        //then
        assertThat(response.getItems()).hasSize(1);
        assertThat(response.getItems().get(0).getAuthorName()).isEqualTo("서울유저");
    }

    @Test
    @DisplayName("조건에 맞는 게시글이 없는 경우 빈 결과를 반환한다")
    void noMatchFilter_returnEmpty() {
        //given
        PostFilterCondition condition = new PostFilterCondition(null, "null", null, null);

        //when
        PostSliceResponse response = postService.findList(null, condition, PostSortType.LATEST, null, null, 10);

        //then
        assertThat(response.getItems()).isEmpty();
        assertThat(response.isHasNext()).isFalse();
    }

    //===== init =====

    private void createPost(User user, String title, String usingLanguage, String learningLanguage) {
        postRepository.save(Post.builder()
                .author(user)
                .title(title)
                .content("내용")
                .usingLanguage(usingLanguage)
                .learningLanguage(learningLanguage)
                .build());
    }
}