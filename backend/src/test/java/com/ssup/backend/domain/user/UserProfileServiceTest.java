package com.ssup.backend.domain.user;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.user.profile.UserProfileService;
import com.ssup.backend.domain.user.profile.dto.UserLocationUpdateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileCreateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
import com.ssup.backend.fixture.user.UserFixture;
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
import org.springframework.mock.web.MockMultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @DisplayName("회원가입 후 추가정보 입력 - 실패 (존재하지 않는 지역)")
    @Test
    void createMyProfile_locationNotFound() {
        //given
        User user = User.builder().id(1L).build();

        given(userRepository.findMeProfileById(1L))
                .willReturn(Optional.of(user));

        UserMeProfileCreateRequest request = UserMeProfileCreateRequest.builder()
                .age(20)
                .contact("010")
                .location(new UserLocationUpdateRequest(99L))
                .build();

        given(locationRepository.findById(99L))
                .willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userProfileService.createMyProfile(1L, request))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.LOCATION_NOT_FOUND.getMessage());
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

    @DisplayName("나의 프로필 수정 - 새로운 관심사로 모두 교체한다.")
    @Test
    void updateMyInterests_replaceAll() {
        //given
        User user = getUser();
        Interest i1 = getInterest(1L);
        Interest i2 = getInterest(2L);

        given(userRepository.findMeProfileById(1L)).willReturn(Optional.of(user));
        given(interestRepository.findById(1L)).willReturn(Optional.of(i1));
        given(interestRepository.findById(2L)).willReturn(Optional.of(i2));

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

    @Test
    void deleteMyAccount_softDelete_success() {
        User user = UserFixture.createUser();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));

        userProfileService.deleteMyAccount(1L);

        assertThat(user.getStatus()).isEqualTo(UserStatus.DELETED);
    }

    //===== init =====

    private User getUser() {
        return User.builder()
                .id(1L)
                .imageUrl("old.png")
                .age(33)
                .languages(new ArrayList<>())
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
