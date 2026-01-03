package com.ssup.backend.domain.match;

import com.ssup.backend.domain.match.dto.MatchAcceptResponse;
import com.ssup.backend.domain.match.dto.MatchCreateRequest;
import com.ssup.backend.domain.match.dto.MatchListResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class MatchService {

    private final MatchRepository matchRepository;
    private final UserRepository userRepository;

    public void createRequest(Long requesterId, MatchCreateRequest request) {
        if (requesterId.equals(request.getReceiverId())) {
            throw new SsupException(ErrorCode.INVALID_MATCH_REQUEST);
        }

        //PENDING/ACCEPTED -> 중복 신청 불가
        boolean exists = matchRepository.existsByRequesterIdAndReceiverIdAndStatusIn(
                requesterId,
                request.getReceiverId(),
                List.of(MatchStatus.PENDING, MatchStatus.ACCEPTED)
        );

        if (exists) {
            throw new SsupException(ErrorCode.MATCH_ALREADY_EXISTS);
        }

        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new SsupException(ErrorCode.USER_NOT_FOUND));
        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new SsupException(ErrorCode.USER_NOT_FOUND));

        Match matchRequest = Match.builder()
                .requester(requester)
                .receiver(receiver)
                .status(MatchStatus.PENDING)
                .build();

        matchRepository.save(matchRequest);
    }

    public MatchAcceptResponse acceptRequest(Long currentUserId, Long requestId) {
        Match matchRequest = matchRepository.findById(requestId)
                .orElseThrow(() -> new SsupException(ErrorCode.MATCH_NOT_FOUND));

        //수신 권한 확인
        if (!matchRequest.getReceiver().getId().equals(currentUserId)) {
            throw new SsupException(ErrorCode.INVALID_REQUEST);
        }

        //상태 확인 (PENDING)
        if (matchRequest.getStatus() != MatchStatus.PENDING) {
            throw new SsupException(ErrorCode.MATCH_ALREADY_PROCESSED);
        }

        matchRequest.accept();
        String contact = matchRequest.getRequester().getContact();
        return MatchAcceptResponse.of(matchRequest.getId(), contact);
    }

    public void rejectRequest(Long currentUserId, Long requestId) {
        Match matchRequest = matchRepository.findById(requestId)
                .orElseThrow(() -> new SsupException(ErrorCode.MATCH_NOT_FOUND));

        if (!matchRequest.getReceiver().getId().equals(currentUserId)) {
            throw new SsupException(ErrorCode.INVALID_REQUEST);
        }

        if (matchRequest.getStatus() != MatchStatus.PENDING) {
            throw new SsupException(ErrorCode.MATCH_ALREADY_PROCESSED);
        }

        matchRequest.reject();
    }

    @Transactional(readOnly = true)
    public List<MatchListResponse> findUserMatchHistory(Long userId) {
        List<Match> matches = matchRepository.findAllByUserId(userId);
        return MatchListResponse.of(matches, userId);
    }
}
