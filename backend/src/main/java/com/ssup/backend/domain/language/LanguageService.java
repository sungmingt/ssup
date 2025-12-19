package com.ssup.backend.domain.language;

import com.ssup.backend.domain.language.dto.LanguageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LanguageService {

    private final LanguageRepository languageRepository;

    public List<LanguageResponse> findList() {
        return languageRepository.findAll()
                .stream()
                .map(LanguageResponse::of)
                .toList();
    }

    public boolean existsByName(String name) {
        return languageRepository.existsByName(name);
    }
}