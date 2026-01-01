package com.ssup.backend.domain.user.language;

import com.ssup.backend.domain.language.Language;
import com.ssup.backend.domain.language.LanguageRepository;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.language.dto.UserLanguageRequestItem;
import com.ssup.backend.domain.user.language.dto.UserLanguageResponse;
import com.ssup.backend.domain.user.language.dto.UserLanguageResponseItem;
import com.ssup.backend.domain.user.language.dto.UserLanguageUpdateRequest;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.ssup.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserLanguageService {

    private final UserRepository userRepository;
    private final UserLanguageRepository userLanguageRepository;
    private final LanguageRepository languageRepository;

    @Transactional(readOnly = true)
    public UserLanguageResponse findUserLanguages(Long userId) {
        User user = userRepository.findWithLanguages(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        List<UserLanguageResponseItem> using = new ArrayList<>();
        List<UserLanguageResponseItem> learning = new ArrayList<>();

        for (UserLanguage ul : user.getLanguages()) {
            UserLanguageResponseItem item =
                    new UserLanguageResponseItem (
                            ul.getLanguage().getId(),
                            ul.getLanguage().getCode(),
                            ul.getLanguage().getName(),
                            ul.getLevel()
                    );

            if (ul.getType() == LanguageType.USING) {
                using.add(item);
            } else {
                learning.add(item);
            }
        }

        return new UserLanguageResponse(using, learning);
    }

    public void updateUserLanguages(Long userId, UserLanguageUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        user.getLanguages().clear();
        userLanguageRepository.flush();

        for (UserLanguageRequestItem item : request.getLanguages()) {
            Language language = languageRepository.findById(item.getLanguageId())
                    .orElseThrow(() -> new SsupException(LANGUAGE_NOT_FOUND));

            user.addLanguage(language, item.getLevel(), item.getType());
        }
    }
}
