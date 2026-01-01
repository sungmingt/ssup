package com.ssup.backend.domain.language.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.language.UserLanguageController;
import com.ssup.backend.domain.user.language.UserLanguageService;
import com.ssup.backend.domain.user.language.dto.UserLanguageRequestItem;
import com.ssup.backend.domain.user.language.dto.UserLanguageResponse;
import com.ssup.backend.domain.user.language.dto.UserLanguageResponseItem;
import com.ssup.backend.domain.user.language.dto.UserLanguageUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserLanguageController.class)
@AutoConfigureMockMvc(addFilters = false)
class UserLanguageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserLanguageService userLanguageService;

    @MockBean
    AppUserProvider appUserProvider;

    @DisplayName("유저 언어 조회 api - 성공")
    @Test
    void findUserLanguages_success() throws Exception {
        //given
        Long userId = 1L;
        UserLanguageResponse response =
                UserLanguageResponse.builder()
                        .usingLanguages(List.of(
                                new UserLanguageResponseItem(1L, "EN", "영어", LanguageLevel.NATIVE)))
                        .learningLanguages(List.of())
                        .build();

        given(userLanguageService.findUserLanguages(userId)).willReturn(response);
        given(appUserProvider.getUserId()).willReturn(userId);

        //when, then
        mockMvc.perform(get("/api/users/1/languages"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.usingLanguages[0].code").value("EN"));
    }

    @DisplayName("유저 언어 생성/수정 api - 성공")
    @Test
    void saveMyLanguages_success() throws Exception {
        //given
        Long userId = 1L;
        UserLanguageUpdateRequest request =
                new UserLanguageUpdateRequest(
                        List.of(new UserLanguageRequestItem(1L, LanguageType.USING, LanguageLevel.ADVANCED))
                );

        //when, then
        mockMvc.perform(put("/api/users/me/languages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk());
        given(appUserProvider.getUserId()).willReturn(userId);

        then(userLanguageService).should().updateUserLanguages(anyLong(), any());
    }
}
