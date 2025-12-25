package com.ssup.backend.fixture.user;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.fixture.language.LanguageJpaFixture;
import com.ssup.backend.fixture.location.LocationJpaFixture;
import jakarta.persistence.EntityManager;

public class UserJpaFixture {

    public static User createUser(EntityManager em) {
        Location siDo = LocationJpaFixture.createSiDo(em);
        Location siGunGu = LocationJpaFixture.createSiGunGu(em, siDo);

        Language korean = LanguageJpaFixture.createKorean(em);
        Language english = LanguageJpaFixture.createEnglish(em);

        User user = User.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .imageUrl("old.png")
                .age(33)
                .location(siGunGu)
                .build();

        user.addLanguage(korean, LanguageLevel.NATIVE, LanguageType.USING);
        user.addLanguage(english, LanguageLevel.ADVANCED, LanguageType.LEARNING);

        em.persist(user);
        return user;
    }
}
