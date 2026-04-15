package com.ssup.backend.domain.user;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.match.Match;
import com.ssup.backend.domain.match.MatchRepository;
import com.ssup.backend.domain.match.MatchStatus;
import com.ssup.backend.domain.user.interest.UserInterestRepository;
import com.ssup.backend.domain.user.profile.UserProfileService;
import com.ssup.backend.domain.user.profile.dto.*;
import com.ssup.backend.domain.user.recommend.event.UserProfileChangeTrigger;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mock.web.MockMultipartFile;

import java.util.*;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserProfileServiceTest {

    @InjectMocks
    private UserProfileService userProfileService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageStorage imageStorage;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private InterestRepository interestRepository;

    @Mock
    private UserInterestRepository userInterestRepository;

    @Mock
    private MatchRepository matchRepository;

    @Mock
    private UserProfileChangeTrigger profileChangeTrigger;

    @DisplayName("나의 프로필 조회 - 성공")
    @Test
    void findMyProfile_success() {
        //given
        User user = getUser();

        given(userRepository.findMeProfileById(1L))
                .willReturn(Optional.of(user));

        //when
        UserMeProfileResponse result = userProfileService.findMyProfile(1L);

        //then
        assertThat(result.getId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("상대 프로필 조회 - 비로그인 사용자")
    void findUserProfile_noLogin() {
        //given
        User user = getUser();
        given(userRepository.findUserProfileById(2L))
                .willReturn(Optional.of(user));

        //when
        UserProfileResponse response = userProfileService.findUserProfile(null, 2L);

        //then
        assertThat(response.getAge()).isEqualTo(user.getAge());
    }

    @Test
    @DisplayName("상대 프로필 조회 시, 인증 유저라면 매치 존재 여부를 반환한다.")
    void findUserProfile_withMatch() {
        //given
        User other = getUser();
        User me = User.builder().id(other.getId() + 1).build();
        Match match = Match.builder()
                .status(MatchStatus.ACCEPTED)
                .requester(me)
                .build();

        given(userRepository.findUserProfileById(other.getId()))
                .willReturn(Optional.of(other));
        given(matchRepository.findActiveMatchBetweenUsers(me.getId(), other.getId()))
                .willReturn(Optional.of(match));

        //when
        UserProfileResponse res = userProfileService.findUserProfile(me.getId(), other.getId());

        //then
        assertThat(res.getMatchInfoResponse().getMatchStatus()).isEqualTo(MatchStatus.ACCEPTED);
        assertThat(res.getMatchInfoResponse().isAmIRequester()).isTrue();
    }

    @DisplayName("회원가입 후 추가정보 입력 - 실패 (존재하지 않는 지역)")
    @Test
    void createMyProfile_locationNotFound() {
        //given
        User user = User.builder().id(1L).build();

        given(userRepository.findById(user.getId()))
                .willReturn(Optional.of(user));

        UserMeProfileCreateRequest request = UserMeProfileCreateRequest.builder()
                .age(20)
                .contact("010")
                .location(new UserLocationUpdateRequest(99L))
                .interests(List.of(new UserInterestRequestItem(1L)))
                .build();

        given(locationRepository.findById(99L))
                .willReturn(Optional.empty());
        given(interestRepository.findAllById(Set.of(1L))).willReturn(List.of(getInterest(1L)));

        MockMultipartFile image = getMockImage();

        //when, then
        assertThatThrownBy(() -> userProfileService.createMyProfile(user.getId(), image, request))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.LOCATION_NOT_FOUND.getMessage());
    }

    @Test
    @DisplayName("추가정보 입력 - 실패 (관심사 일부 조회)")
    void createMyProfile_interestMismatch() {
        //given
        User user = User.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(interestRepository.findAllById(Set.of(1L,2L)))
                .willReturn(List.of(getInterest(1L))); //size 불일치

        UserMeProfileCreateRequest req = UserMeProfileCreateRequest.builder()
                .interests(List.of(
                        new UserInterestRequestItem(1L),
                        new UserInterestRequestItem(2L)
                ))
                .build();

        //when, then
        assertThatThrownBy(() -> userProfileService.createMyProfile(1L, null, req))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.INTEREST_NOT_FOUND.getMessage());
    }

    @DisplayName("나의 프로필 수정 시 이미지 수정 - 성공")
    @Test
    void updateMyProfile_imageReplace_success() {
        //given
        User user = getUser();
        MockMultipartFile image = getMockImage();

        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                "nick", null, "intro", 20, Gender.MALE,
                "010", false, null, null
        );

        given(userRepository.findMeProfileById(1L))
                .willReturn(Optional.of(user));
        given(imageStorage.upload(eq(ImageType.PROFILE), any()))
                .willReturn("new.png");

        String newImageUrl = "new.png";
        String oldImageUrl = user.getImageUrl();

        //when
        userProfileService.updateMyProfile(1L, image, request);

        //then
        verify(imageStorage).upload(eq(ImageType.PROFILE), any());
        verify(imageStorage).deleteByUrl(oldImageUrl);
        assertThat(user.getImageUrl()).isEqualTo(newImageUrl);
    }

    @DisplayName("나의 프로필 수정 - 존재하지 않는 지역 입력 시 예외를 던진다.")
    @Test
    void updateMyLocation_invalidLevel() {
        //given
        User user = getUser();
        Interest interest = getInterest(1L);
        user.addInterest(interest);

        Location wrongLevel = Location.builder().id(3L).level(1).build();

        given(userRepository.findMeProfileById(1L))
                .willReturn(Optional.of(user));
        given(locationRepository.findById(3L))
                .willReturn(Optional.of(wrongLevel));

        UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .userLocationUpdateRequest(new UserLocationUpdateRequest(3L))
                .build();

        //when, then
        assertThatThrownBy(() ->
                userProfileService.updateMyProfile(1L, null, request)
        ).isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.INVALID_LOCATION_LEVEL.getMessage());
    }

    @Test
    @DisplayName("나의 프로필 수정 - removeImage=true일 경우, 프로필 이미지를 삭제한다.")
    void updateMyProfile_removeImage_ifTrue() {
        User user = getUser();
        given(userRepository.findMeProfileById(1L)).willReturn(Optional.of(user));

        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                "nick", null, "intro", 20, Gender.MALE,
                "010", true, null, null
        );

        userProfileService.updateMyProfile(1L, null, request);

        assertThat(user.getImageUrl()).isNull();
    }

    @Test
    @DisplayName("나의 프로필 수정 - interest=null일 경우 변경되지 않는다.")
    void updateMyInterests_null() {
        User user = getUser();
        user.addInterest(getInterest(1L));

        given(userRepository.findMeProfileById(1L)).willReturn(Optional.of(user));

        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                "nick", null, "intro", 20, Gender.MALE,
                "010", true, null, null
        );

        userProfileService.updateMyProfile(1L, null, request);

        assertThat(user.getInterests()).hasSize(1);
    }

    @Test
    @DisplayName("나의 프로필 수정 - location=null일 경우 변경되지 않는다.")
    void updateMyLocation_null() {
        User user = getUser();

        given(userRepository.findMeProfileById(1L)).willReturn(Optional.of(user));

        UserProfileUpdateRequest req = UserProfileUpdateRequest.builder()
                .nickname("nickname")
                .age(33)
                .gender(Gender.MALE)
                .contact("010")
                .userLocationUpdateRequest(null)
                .build();

        userProfileService.updateMyProfile(1L, null, req);

        assertThat(user.getLocation()).isNotNull();
    }

    @DisplayName("나의 프로필 수정 - 관심사가 존재하면, 새로운 관심사로 모두 교체한다.")
    @Test
    void updateMyInterests_replaceAll() {
        //given
        User user = getUser();
        Interest i1 = getInterest(1L);
        Interest i2 = getInterest(2L);

        given(userRepository.findMeProfileById(1L)).willReturn(Optional.of(user));
        given(interestRepository.findAllById(List.of(i1.getId(), i2.getId()))).willReturn(List.of(i1, i2));

        UserProfileUpdateRequest request = UserProfileUpdateRequest.builder()
                .intro("hihi")
                .contact("123")
                .age(33)
                .interestIds(List.of(1L, 2L))
                .build();

        //when
        userProfileService.updateMyProfile(1L, null, request);

        //then
        assertThat(user.getInterests()).hasSize(2);
    }

    //===== init =====

    private User getUser() {
        return User.builder()
                .id(1L)
                .imageUrl("old.png")
                .age(33)
                .languages(new HashSet<>())
                .location(new Location(1L, "강남구", 1, Location.builder().id(1L).name("서울특별시").level(1).build()))
                .build();
    }

    private Interest getInterest(Long id) {
        InterestCategory category = InterestCategory.builder()
                .id(1L)
                .code("SPORT")
                .name("운동")
                .build();

        return Interest.builder()
                .id(id)
                .code("HEALTH")
                .name("헬스")
                .category(category)
                .build();
    }

    private MockMultipartFile getMockImage(){
        return new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "data".getBytes()
        );
    }
}
