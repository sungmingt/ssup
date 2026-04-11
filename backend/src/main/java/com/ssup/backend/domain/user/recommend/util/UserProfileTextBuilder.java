package com.ssup.backend.domain.user.recommend.util;

import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.language.UserLanguage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
public class UserProfileTextBuilder {

    public String build(User user) {

        log.info("### text builder called");
        List<String> nativeLanguages = new ArrayList<>();
        List<String> learningLanguages = new ArrayList<>();

        for (UserLanguage ul : user.getLanguages()) {
            String languageName = ul.getLanguage().getName();
            String level = ul.getLevel().name();

            log.info("### language name:{}", languageName);
            log.info("### level:{}", level);


            String label = languageName + " (" + level + ")";

            if (ul.getType() == LanguageType.USING) {
                nativeLanguages.add(label);
            }

            if (ul.getType() == LanguageType.LEARNING) {
                learningLanguages.add(label);
            }
        }

        String interests = user.getInterests() == null || user.getInterests().isEmpty()
                ? "None"
                : user.getInterests().stream()
                .map(ui -> ui.getInterest().getName())
                .collect(Collectors.joining(", "));

        //todo: activity level

        return """
                User Profile:
                Native Languages: %s
                Learning Languages: %s
                Interests: %s
                Location: %s
                Self Introduction: %s
                """
                .formatted(
                        nativeLanguages.isEmpty() ? "None" : String.join(", ", nativeLanguages),
                        learningLanguages.isEmpty() ? "None" : String.join(", ", learningLanguages),
                        interests,
                        normalize(user.getLocation().getName()),
                        sanitize(user.getIntro())
                );
    }

    private String normalize(String value) {
        return value == null ? "Unknown" : value.trim();
    }

    private String normalizeActivity(int score) {
        if (score > 80) return "High";
        if (score > 50) return "Medium";
        return "Low";
    }

    private String sanitize(String text) {
        if (text == null) return "None";
        return text.replaceAll("[\\n\\r]", " ");
    }
}