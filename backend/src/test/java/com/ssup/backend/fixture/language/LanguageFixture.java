package com.ssup.backend.fixture.language;

import com.ssup.backend.domain.language.Language;

public class LanguageFixture {

    public static Language createEnglish() {
        return Language.builder()
                .code("EN")
                .name("English")
                .build();
    }

    public static Language createKorean() {
        return Language.builder()
                .code("KO")
                .name("한국어")
                .build();
    }
}
