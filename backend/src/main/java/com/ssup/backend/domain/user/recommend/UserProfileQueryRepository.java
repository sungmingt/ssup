package com.ssup.backend.domain.user.recommend;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssup.backend.domain.language.LanguageType;
import com.ssup.backend.domain.language.QLanguage;
import com.ssup.backend.domain.location.QLocation;
import com.ssup.backend.domain.user.QUser;
import com.ssup.backend.domain.user.language.QUserLanguage;
import com.ssup.backend.domain.user.profile.dto.UserLocationResponse;
import com.ssup.backend.domain.user.recommend.dto.UserRecommendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
@RequiredArgsConstructor
public class UserProfileQueryRepository {

    private final JPAQueryFactory queryFactory;

    private static final QUser user = QUser.user;
    private static final QLocation location = QLocation.location;
    private static final QLocation parent = new QLocation("parent");
    private static final QUserLanguage ul = QUserLanguage.userLanguage;
    private static final QLanguage language = QLanguage.language;

    //===== authenticated =====
    public List<UserRecommendResponse> findRecommendUsers(List<Long> ids) {
        List<Tuple> result = queryFactory
                .select(selectFields())
                .from(user)
                .join(user.location, location)
                .join(location.parent, parent)
                .leftJoin(user.languages, ul)
                .leftJoin(ul.language, language)
                .where(user.id.in(ids))
                .fetch();

        Map<Long, UserRecommendResponse> map = new LinkedHashMap<>();

        for (Tuple t : result) {
            Long userId = t.get(user.id);

            UserRecommendResponse dto = map.computeIfAbsent(
                    userId,
                    id -> createBaseDto(t)
            );

            addLanguage(dto, t);
        }

        return new ArrayList<>(map.values());
    }

    private Expression<?>[] selectFields() {
        return new Expression[]{
                user.id,
                user.nickname,
                user.imageUrl,
                user.age,
                location.id,
                location.name,
                parent.id,
                parent.name,
                ul.type,
                language.name
        };
    }

    private UserRecommendResponse createBaseDto(Tuple t) {
        return UserRecommendResponse.builder()
                .id(t.get(user.id))
                .nickname(t.get(user.nickname))
                .imageUrl(t.get(user.imageUrl))
                .age(t.get(user.age))
                .location(
                        UserLocationResponse.builder()
                                .siGunGuId(t.get(location.id))
                                .siGunGuName(t.get(location.name))
                                .siDoId(t.get(parent.id))
                                .siDoName(t.get(parent.name))
                                .build()
                )
                .usingLanguages(new ArrayList<>())
                .learningLanguages(new ArrayList<>())
                .build();
    }

    private void addLanguage(UserRecommendResponse dto, Tuple t) {
        LanguageType type = t.get(ul.type);
        String languageName = t.get(language.name);

        if (type == null || languageName == null) return;

        if (type == LanguageType.USING) {
            dto.getUsingLanguages().add(languageName);
        } else {
            dto.getLearningLanguages().add(languageName);
        }
    }

    //===== anonymous =====
    public List<Long> findRandomUserIds(int limit) {
        Long countResult = queryFactory
                .select(user.count())
                .from(user)
                .fetchOne();

        long count = countResult != null ? countResult : 0L;

        if (count == 0) return List.of();

        int offset = (int) Math.max(0, Math.random() * (count - limit));

        return queryFactory
                .select(user.id)
                .from(user)
                .offset(offset)
                .limit(limit)
                .fetch();
    }
}