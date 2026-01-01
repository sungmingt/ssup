package com.ssup.backend.domain.heart.slice.comment;

import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.heart.comment.CommentHeartController;
import com.ssup.backend.domain.heart.comment.CommentHeartFacade;
import com.ssup.backend.domain.heart.dto.HeartResponse;
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
@WebMvcTest(CommentHeartController.class)
@AutoConfigureMockMvc(addFilters = false)
class CommentHeartControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    CommentHeartFacade commentHeartFacade;

    @MockBean
    AppUserProvider appUserProvider;

    @DisplayName("댓글 좋아요 요청 - 성공")
    @Test
    void toggleHeart_success() throws Exception {
        //given
        Long userId = 1L;
        Long commentId = 1L;

        given(commentHeartFacade.tryToggleHeart(userId, commentId))
                .willReturn(HeartResponse.of(true, 1));
        given(appUserProvider.getUserId()).willReturn(userId);

        //when, then
        mockMvc.perform(post("/api/comments/1/hearts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.hearted").value(true))
                .andExpect(jsonPath("$.heartCount").value(1));
    }
}