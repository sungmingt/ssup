package com.ssup.backend.domain.user.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.user.Gender;
import com.ssup.backend.domain.user.profile.UserMeProfileController;
import com.ssup.backend.domain.user.profile.UserProfileService;
import com.ssup.backend.domain.user.profile.dto.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(UserMeProfileController.class)
class UserMeProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserProfileService userProfileService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("나의 프로필 조회 - 성공")
    @Test
    void findMyProfile_success() throws Exception {
        //given
        UserMeProfileResponse response = getUserMeProfileResponse();

        given(userProfileService.findMyProfile(1L)).willReturn(response);

        //when, then
        mockMvc.perform(get("/api/users/me/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nickname").value("테스트"))
                .andExpect(jsonPath("$.location.siGunGuName").value("강남구"));
    }

    @DisplayName("가입 후, 나의 프로필 추가정보 입력 - 성공")
    @Test
    void createMyProfile_success() throws Exception {
        //given
        UserMeProfileCreateRequest request = UserMeProfileCreateRequest.builder()
                .age(20)
                .gender(Gender.MALE)
                .intro("intro")
                .contact("010-1111-2222")
                .location(new UserLocationUpdateRequest(2L))
                .build();

        MockMultipartFile dto = getDtoPart(request);
        MockMultipartFile image = getImagePart();

        given(userProfileService.createMyProfile(any(), any(), any()))
                .willReturn(UserMeProfileResponse.builder().id(1L).build());

        //when, then
        mockMvc.perform(multipart("/api/users/me/profile")
                        .file(dto)
                        .file(image))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L));
    }

    @DisplayName("나의 프로필 수정 - 성공")
    @Test
    void updateMyProfile_success() throws Exception {
        //given
        UserProfileUpdateRequest request = new UserProfileUpdateRequest(
                "변경닉네임",
                null,
                "intro",
                25,
                Gender.FEMALE,
                "010-2222-3333",
                false,
                new UserLocationUpdateRequest(3L),
                List.of(1L, 2L)
        );

        MockMultipartFile dto = getDtoPart(request);
        MockMultipartFile image = getImagePart();

        given(userProfileService.updateMyProfile(eq(1L), any(), any()))
                .willReturn(UserMeProfileResponse.builder().id(1L).build());

        //when, then
        mockMvc.perform(multipart("/api/users/me/profile")
                        .file(dto)
                        .file(image)
                        .with(requestBuilder -> {
                            requestBuilder.setMethod("PUT");
                            return requestBuilder;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }

    //===== init =====

    private UserMeProfileResponse getUserMeProfileResponse() {
        return UserMeProfileResponse.builder()
                .id(1L)
                .email("test@gmail.com")
                .nickname("테스트")
                .imageUrl("profile.png")
                .intro("hi")
                .age(20)
                .contact("010-1111-2222")
                .location(UserLocationResponse.builder()
                        .siDoId(1L)
                        .siDoName("서울")
                        .siGunGuId(2L)
                        .siGunGuName("강남구")
                        .build())
                .interests(List.of())
                .build();
    }

    private MockMultipartFile getDtoPart(Object dto) throws Exception {
        return new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
    }

    private MockMultipartFile getImagePart(){
        return new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "data".getBytes()
        );
    }
}
