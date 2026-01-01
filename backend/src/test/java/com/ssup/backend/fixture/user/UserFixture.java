package com.ssup.backend.fixture.user;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageLevel;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.fixture.language.LanguageFixture;
import com.ssup.backend.fixture.location.LocationFixture;

public class UserFixture {

    public static User createUser() {
        Location siDo = LocationFixture.createSiDo();
        Location siGunGu = LocationFixture.createSiGunGu(siDo);

        Language korean = LanguageFixture.createKorean();
        Language english = LanguageFixture.createEnglish();

        User user = User.builder()
                .nickname("tester")
                .email("test@gmail.com")
                .imageUrl("old.png")
                .age(33)
                .location(siGunGu)
                .build();

        user.addLanguage(korean, LanguageLevel.NATIVE, LanguageType.USING);
        user.addLanguage(english, LanguageLevel.ADVANCED, LanguageType.LEARNING);

        return user;
    }
}
