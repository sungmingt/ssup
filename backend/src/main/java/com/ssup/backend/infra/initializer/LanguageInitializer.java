package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class LanguageInitializer implements CommandLineRunner {

    private final LanguageRepository languageRepository;

    @Override
    public void run(String... args) {
        List<Language> languages = List.of(
                Language.builder().code("KO").name("한국어").build(),
                Language.builder().code("EN").name("English").build(),
                Language.builder().code("JAP").name("日本語").build(),
                Language.builder().code("ZH").name("中文").build(),
                Language.builder().code("ES").name("Español").build(),
                Language.builder().code("FR").name("Français").build(),
                Language.builder().code("DE").name("Deutsch").build()
        );

        for (Language language : languages) {
            if (!languageRepository.existsByCode(language.getCode())) {
                languageRepository.save(language);
            }
        }
    }
}
