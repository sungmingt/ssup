package com.ssup.backend.domain.language.slice;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageRepository;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.language.UserLanguage;
import com.ssup.backend.domain.user.language.UserLanguageRepository;
import com.ssup.backend.domain.user.language.UserLanguageService;
import com.ssup.backend.domain.user.language.dto.UserLanguageRequestItem;
import com.ssup.backend.domain.user.language.dto.UserLanguageResponse;
import com.ssup.backend.domain.user.language.dto.UserLanguageUpdateRequest;
import com.ssup.backend.fixture.user.UserFixture;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class UserLanguageServiceTest {

    @InjectMocks
    private UserLanguageService userLanguageService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private LanguageRepository languageRepository;

    @Mock
    private UserLanguageRepository userLanguageRepository;

    @DisplayName("유저 언어 조회 - 성공 (USING/LEARNING 분리)")
    @Test
    void findUserLanguages_success() {
        //given
        User user = UserFixture.createUser();
        given(userRepository.findWithLanguages(1L)).willReturn(Optional.of(user));

        //when
        UserLanguageResponse response = userLanguageService.findUserLanguages(1L);

        //then
        assertThat(response.getUsingLanguages()).hasSize(1);
        assertThat(response.getLearningLanguages()).hasSize(1);
        assertThat(response.getUsingLanguages().get(0).getCode()).isEqualTo("KO");
        assertThat(response.getLearningLanguages().get(0).getCode()).isEqualTo("EN");
    }

    @DisplayName("유저 언어 조회 - 실패 (유저 없음)")
    @Test
    void findUserLanguages_userNotFound() {
        //given
        given(userRepository.findWithLanguages(1L)).willReturn(Optional.empty());

        //when, then
        assertThatThrownBy(() -> userLanguageService.findUserLanguages(1L))
                .isInstanceOf(SsupException.class);
    }

    @DisplayName("유저 언어 수정 - 기존 언어 삭제 후 재등록 수행")
    @Test
    void updateUserLanguages_clearAndReplace() {
        //given
        User user = getUser();
        user.getLanguages().add(new UserLanguage(user, Language.builder().id(99L).build(),
                LanguageLevel.BEGINNER, LanguageType.USING));

        Language english = Language.builder().id(1L).build();
        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(languageRepository.findById(1L)).willReturn(Optional.of(english));

        UserLanguageUpdateRequest request =
                new UserLanguageUpdateRequest(List.of(
                        new UserLanguageRequestItem(1L, LanguageType.LEARNING, LanguageLevel.ADVANCED)
                ));

        //when
        userLanguageService.updateUserLanguages(1L, request);

        //then
        assertThat(user.getLanguages()).hasSize(1);
        assertThat(user.getLanguages())
                .extracting(UserLanguage::getType)
                .contains(LanguageType.LEARNING);
    }

    @DisplayName("유저의 사용 언어 수정 - 성공")
    @Test
    void updateUserLanguage_success() {
        //given
        User user = getUser();
        Language english = Language.builder().id(1L).build();
        Language korean = Language.builder().id(2L).build();

        given(userRepository.findById(1L)).willReturn(Optional.of(user));
        given(languageRepository.findById(1L)).willReturn(Optional.of(english));
        given(languageRepository.findById(2L)).willReturn(Optional.of(korean));

        List<UserLanguageRequestItem> userLanguageRequestItems =
                List.of(
                        new UserLanguageRequestItem(
                        1L, LanguageType.USING, LanguageLevel.NATIVE
                ),
                        new UserLanguageRequestItem(
                        2L, LanguageType.LEARNING, LanguageLevel.BEGINNER
                )
        );
        UserLanguageUpdateRequest request = new UserLanguageUpdateRequest(userLanguageRequestItems);

        //when
        userLanguageService.updateUserLanguages(1L, request);

        //then
        assertThat(user.getLanguages()).hasSize(2);
        assertThat(user.getLanguages())
                .extracting("type")
                .containsExactlyInAnyOrder(
                        LanguageType.USING,
                        LanguageType.LEARNING
                );
    }

    //===== init =====

    private User getUser() {
        return User.builder()
                .id(1L)
                .languages(new HashSet<>())
                .build();
    }

    private Language getLanguage(Long id, String code) {
        return Language.builder()
                .id(id)
                .code(code)
                .build();
    }
}
