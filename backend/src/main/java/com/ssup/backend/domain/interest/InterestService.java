package com.ssup.backend.domain.interest;

import com.ssup.backend.domain.interest.dto.InterestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class InterestService {

    private final InterestRepository interestRepository;

    public List<InterestResponse> findAll() {
        return interestRepository.findAll().stream()
                .map(InterestResponse::of)
                .toList();
    }
}
