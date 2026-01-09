package com.ssup.backend.domain.user;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.location.Location;
import org.junit.jupiter.api.Test;

import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

class UserTest {

    @Test
    @DisplayName("프로필 정보 수정 테스트")
    void updateProfile() {
        User user = User.builder()
                .nickname("old")
                .age(10)
                .gender(Gender.MALE)
                .contact("010")
                .build();

        user.updateProfile("new", "intro", 20, Gender.FEMALE, "020");

        assertThat(user.getNickname()).isEqualTo("new");
        assertThat(user.getIntro()).isEqualTo("intro");
        assertThat(user.getAge()).isEqualTo(20);
        assertThat(user.getGender()).isEqualTo(Gender.FEMALE);
        assertThat(user.getContact()).isEqualTo("020");
    }

    @Test
    @DisplayName("프로필 이미지 수정 테스트")
    void updateImageUrl() {
        User user = User.builder().imageUrl("old.png").build();
        user.updateImageUrl("new.png");
        assertThat(user.getImageUrl()).isEqualTo("new.png");
    }

    @Test
    @DisplayName("지역 수정 테스트")
    void updateLocation() {
        Location location = Location.builder().id(1L).build();
        User user = User.builder().build();

        user.updateLocation(location);
        assertThat(user.getLocation()).isEqualTo(location);
    }

    @Test
    @DisplayName("프로필 init 테스트")
    void initProfile() {
        User user = User.builder().build();
        user.initProfile(25, Gender.MALE, "hello", "010");

        assertThat(user.getAge()).isEqualTo(25);
        assertThat(user.getGender()).isEqualTo(Gender.MALE);
        assertThat(user.getIntro()).isEqualTo("hello");
        assertThat(user.getContact()).isEqualTo("010");
    }

    @Test
    @DisplayName("회원 상태 변경 테스트 (ACTIVE/DELETED)")
    void activateAndDelete() {
        User user = User.builder().status(UserStatus.PENDING).build();

        user.activate();
        assertThat(user.getStatus()).isEqualTo(UserStatus.ACTIVE);

        user.delete();
        assertThat(user.getStatus()).isEqualTo(UserStatus.DELETED);
    }

    @Test
    @DisplayName("언어/관심사 추가 테스트")
    void addLanguageAndInterest() {
        User user = User.builder().build();
        Language language = Language.builder().id(1L).build();
        Interest interest = Interest.builder().id(1L).build();

        user.addLanguage(language, LanguageLevel.BEGINNER, LanguageType.USING);
        user.addInterest(interest);

        assertThat(user.getLanguages()).hasSize(1);
        assertThat(user.getInterests()).hasSize(1);
    }
}
