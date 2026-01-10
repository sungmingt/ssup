package com.ssup.backend.domain.post;

import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.language.LanguageRepository;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.post.dto.PostFilterMetadataResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostFilterService {

    private final LocationRepository locationRepository;
    private final LanguageRepository languageRepository;
    private final InterestRepository interestRepository;

    public PostFilterMetadataResponse findFilterMetadata() {
        return PostFilterMetadataResponse.of(
                locationRepository.findAll(),
                languageRepository.findAll(),
                interestRepository.findAll()
        );
    }
}
