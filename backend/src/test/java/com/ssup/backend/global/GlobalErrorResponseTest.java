package com.ssup.backend.global;

import com.ssup.backend.domain.comment.CommentController;
import com.ssup.backend.domain.comment.CommentService;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.GlobalControllerAdvice;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static com.ssup.backend.global.exception.ErrorCode.COMMENT_NOT_FOUND;
import static com.ssup.backend.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = CommentController.class)
@Import(GlobalControllerAdvice.class)
class GlobalErrorResponseTest {

    //예외가 Advice에 정상적으로 잡히는지
    //json 포맷이 의도한 형식인지

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @DisplayName("SsupException은 공통 ErrorResponse 형식으로 변환된다")
    @Test
    void ssupException_isConvertedToErrorResponse() throws Exception {
        given(commentService.find(anyLong(), anyLong()))
                .willThrow(new SsupException(COMMENT_NOT_FOUND));

        mockMvc.perform(get("/api/posts/1/comments/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").exists())
                .andExpect(jsonPath("$.message").exists());
    }

    @DisplayName("예상치 못한 RuntimeException은 500으로 변환된다")
    @Test
    void runtimeException_returnsInternalServerError() throws Exception {
        given(commentService.find(anyLong(), anyLong()))
                .willThrow(new RuntimeException());

        mockMvc.perform(get("/api/posts/1/comments/999"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value((ErrorCode.INTERNAL_SERVER_ERROR.name())))
                .andExpect(jsonPath("$.message").value(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()));
    }
}