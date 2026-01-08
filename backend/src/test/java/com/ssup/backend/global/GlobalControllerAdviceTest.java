package com.ssup.backend.global;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.GlobalControllerAdvice;
import com.ssup.backend.support.TestExceptionController;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TestExceptionController.class)
@Import(GlobalControllerAdvice.class)
@AutoConfigureMockMvc(addFilters = false)
class GlobalControllerAdviceTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("SsupException - ErrorCode 기반 응답 반환")
    void handleBusinessException() throws Exception {
        mockMvc.perform(get("/test-exception/business"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(ErrorCode.USER_NOT_FOUND.name()));
    }

    @Test
    @DisplayName("RuntimeException - INTERNAL_SERVER_ERROR")
    void handleRuntimeException() throws Exception {
        mockMvc.perform(get("/test-exception/runtime"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.INTERNAL_SERVER_ERROR.name()));
    }

    @Test
    @DisplayName("지원하지 않는 HTTP Method - 405")
    void handleMethodNotSupported() throws Exception {
        mockMvc.perform(put("/test-exception/method-not-supported"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.METHOD_NOT_ALLOWED.name()));
    }

    @Test
    @DisplayName("이미지 사이즈 초과 - FILE_SIZE_EXCEEDED")
    void handleMaxUploadSize() throws Exception {
        mockMvc.perform(get("/test-exception/max-upload"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code")
                        .value(ErrorCode.FILE_SIZE_EXCEEDED.name()));
    }
}
