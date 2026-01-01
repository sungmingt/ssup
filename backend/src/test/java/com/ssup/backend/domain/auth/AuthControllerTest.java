package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.MeResponse;
import com.ssup.backend.domain.user.UserStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    AuthService authService;

    @MockBean
    AppUserProvider appUserProvider;

    @Test
    void me_success() throws Exception {

        MeResponse response = MeResponse.builder()
                .id(1L)
                .nickname("tester")
                .imageUrl("img.png")
                .status(UserStatus.ACTIVE)
                .build();

        given(appUserProvider.getUserId()).willReturn(1L);
        given(authService.me(1L)).willReturn(response);

        mockMvc.perform(get("/api/auth/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));
    }
}