package com.ssup.backend.domain.match;

import com.ssup.backend.domain.match.dto.MatchAcceptResponse;
import com.ssup.backend.domain.match.dto.MatchCreateRequest;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class MatchServiceTest {

    @InjectMocks
    private MatchService matchService;

    @Mock
    private MatchRepository matchRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    @DisplayName("매치 신청 - 성공")
    void createRequest_Success() {
        //given
        Long requesterId = 1L;
        Long receiverId = 2L;
        MatchCreateRequest request = new MatchCreateRequest(receiverId);

        User requester = mock(User.class);
        User receiver = mock(User.class);

        given(userRepository.findById(requesterId)).willReturn(Optional.of(requester));
        given(userRepository.findById(receiverId)).willReturn(Optional.of(receiver));
        given(matchRepository.existsByRequesterIdAndReceiverIdAndStatusIn(any(), any(), any()))
                .willReturn(false);

        //when
        matchService.createRequest(requesterId, request);

        //then
        verify(matchRepository, times(1)).save(any(Match.class));
    }

    @Test
    @DisplayName("매치 수락 시 상대방의 연락처 반환 - 성공")
    void acceptRequest_Success() {
        //given
        Long currentUserId = 2L;
        Long requestId = 100L;

        User requester = mock(User.class);
        User receiver = mock(User.class);
        when(requester.getContact()).thenReturn("010-1234-5678");
        when(receiver.getId()).thenReturn(currentUserId);

        Match match = Match.builder()
                .requester(requester)
                .receiver(receiver)
                .status(MatchStatus.PENDING)
                .build();

        when(matchRepository.findById(requestId)).thenReturn(Optional.of(match));

        //when
        MatchAcceptResponse response = matchService.acceptRequest(currentUserId, requestId);

        //then
        assertThat(response.getPartnerContact()).isEqualTo("010-1234-5678");
        assertThat(match.getStatus()).isEqualTo(MatchStatus.ACCEPTED);
    }

    @Test
    @DisplayName("매치 수락 실패 - 수신자가 아닌 사람이 수락할 수 없다")
    void acceptRequest_Fail_InvalidUser() {
        //given
        Long otherUserId = 999L;
        Long requestId = 100L;

        User receiver = mock(User.class);
        given(receiver.getId()).willReturn(2L);

        Match match = Match.builder()
                .receiver(receiver)
                .build();

        given(matchRepository.findById(requestId)).willReturn(Optional.of(match));

        //when, then
        assertThatThrownBy(() -> matchService.acceptRequest(otherUserId, requestId))
                .isInstanceOf(SsupException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_REQUEST);
    }

    @Test
    @DisplayName("매치 거절 시 REJECTED로 매치 상태가 변경되어야 한다")
    void rejectRequest_Success() {
        //given
        Long currentUserId = 2L;
        Long requestId = 100L;

        User receiver = mock(User.class);
        given(receiver.getId()).willReturn(currentUserId);

        Match match = Match.builder()
                .receiver(receiver)
                .status(MatchStatus.PENDING)
                .build();

        given(matchRepository.findById(requestId)).willReturn(Optional.of(match));

        //when
        matchService.rejectRequest(currentUserId, requestId);

        //then
        assertThat(match.getStatus()).isEqualTo(MatchStatus.REJECTED);
    }

    @Test
    @DisplayName("매치 거절 실패 - 이미 ACCEPTED 처리된 매칭은 거절할 수 없다")
    void rejectRequest_Fail_AlreadyProcessed() {
        //given
        Long currentUserId = 2L;
        Long requestId = 100L;
        User receiver = mock(User.class);
        given(receiver.getId()).willReturn(currentUserId);

        Match match = Match.builder()
                .receiver(receiver)
                .status(MatchStatus.ACCEPTED)
                .build();

        given(matchRepository.findById(requestId)).willReturn(Optional.of(match));

        //when, then
        assertThatThrownBy(() -> matchService.rejectRequest(currentUserId, requestId))
                .isInstanceOf(SsupException.class)
                .hasFieldOrPropertyWithValue("errorCode", ErrorCode.MATCH_ALREADY_PROCESSED);
    }
}