package com.ssup.backend.fixture.language;

import com.ssup.backend.domain.language.Language;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class LanguageJpaFixture {

    public static Language createEnglish(EntityManager em) {
        Language english = Language.builder().code("EN").name("English").build();
        em.persist(english);
        return english;
    }

    public static Language createKorean(EntityManager em) {
        Language korean = Language.builder().code("KO").name("한국어").build();
        em.persist(korean);
        return korean;
    }
}
