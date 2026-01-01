package com.ssup.backend.domain.heart.slice.post;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.heart.dto.HeartResponse;
import com.ssup.backend.domain.heart.post.PostHeartController;
import com.ssup.backend.domain.heart.post.PostHeartFacade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(PostHeartController.class)
@AutoConfigureMockMvc(addFilters = false)
class PostHeartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PostHeartFacade postHeartFacade;

    @MockBean
    AppUserProvider appUserProvider;

    @DisplayName("글 좋아요 요청 - 성공")
    @Test
    void toggleHeart_success() throws Exception {
        //given
        Long userId = 1L;
        Long postId = 1L;

        given(postHeartFacade.tryToggleHeart(userId, postId))
                .willReturn(HeartResponse.of(true, 1));
        given(appUserProvider.getUserId()).willReturn(userId);

        //when, then
        mockMvc.perform(post("/api/posts/1/hearts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hearted").value(true))
                .andExpect(jsonPath("$.heartCount").value(1));
    }
}