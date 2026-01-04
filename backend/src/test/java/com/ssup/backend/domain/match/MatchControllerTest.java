package com.ssup.backend.domain.match;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.auth.AppUserProvider;
import com.ssup.backend.domain.match.dto.MatchAcceptResponse;
import com.ssup.backend.domain.match.dto.MatchCreateRequest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(MatchController.class)
@AutoConfigureMockMvc(addFilters = false)
class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MatchService matchService;

    @MockBean
    private AppUserProvider appUserProvider;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("POST /api/matches - 매치 신청 API 성공")
    void createRequest_ApiSuccess() throws Exception {
        //given
        Long userId = 1L;
        MatchCreateRequest request = new MatchCreateRequest(2L);
        given(appUserProvider.getUserId()).willReturn(userId);

        //when, then
        mockMvc.perform(post("/api/matches")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        verify(matchService).createRequest(any(), any());
    }

    @Test
    @DisplayName("POST /api/matches/{id}/accept - 매치 수락 API 성공")
    void acceptRequest_ApiSuccess() throws Exception {
        //given
        Long matchId = 1L;
        MatchAcceptResponse response = new MatchAcceptResponse(matchId, "010-1234-5678", "매칭 성공!");

        given(appUserProvider.getUserId()).willReturn(1L);
        given(matchService.acceptRequest(any(), eq(matchId))).willReturn(response);

        //when, then
        mockMvc.perform(put("/api/matches/{id}/accept", matchId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.partnerContact").value("010-1234-5678"))
                .andExpect(jsonPath("$.id").value(matchId));
    }

    @Test
    @DisplayName("POST /api/matches/{id}/reject - 매치 거절 API 성공")
    void rejectRequest_ApiSuccess() throws Exception {
        //given
        Long matchId = 1L;
        given(appUserProvider.getUserId()).willReturn(1L);

        //when, then
        mockMvc.perform(put("/api/matches/{id}/reject", matchId))
                .andExpect(status().isOk());

        verify(matchService).rejectRequest(any(), eq(matchId));
    }
}