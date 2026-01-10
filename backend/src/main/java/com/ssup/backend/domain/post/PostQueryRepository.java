package com.ssup.backend.domain.post;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.ssup.backend.domain.post.sort.PostSortType;
import com.ssup.backend.domain.user.QUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.ssup.backend.domain.interest.QUserInterest.userInterest;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final JPAQueryFactory queryFactory;
    private final QPost post = QPost.post;
    private final QUser user = QUser.user;

    public List<Post> findPostsByCondition(PostFilterCondition condition,
                                           PostSortType sortType,
                                           Long cursorKey,
                                           Long cursorId,
                                           int size) {
        return queryFactory
                .selectFrom(post)
                .join(post.author, user).fetchJoin()
                .leftJoin(user.location)
                .where(
                        ltCursor(sortType, cursorKey, cursorId),
                        //지역
                        eqLocation(condition.locationId()),
                        //언어
                        eqUsingLanguage(condition.usingLanguage()),
                        eqLearningLanguage(condition.learningLanguage()),
                        //관심사
                        hasInterest(condition.interestId())
                )
                .orderBy(getOrderSpecifier(sortType))
                .limit(size + 1)
                .fetch();
    }

    private BooleanExpression ltCursor(PostSortType sortType, Long cursorKey, Long cursorId) {
        if (cursorId == null) return null;

        if (sortType == PostSortType.VIEWS) {
            //조회수 순 정렬: (조회수 < cursorKey) OR (조회수 == cursorKey AND ID < cursorId)
            return post.viewCount.lt(cursorKey)
                    .or(post.viewCount.eq(cursorKey).and(post.id.lt(cursorId)));
        }

        return post.id.lt(cursorId);
    }

    private OrderSpecifier<?>[] getOrderSpecifier(PostSortType sort) {
        if (sort == PostSortType.VIEWS) {
            return new OrderSpecifier[]{post.viewCount.desc(), post.id.desc()};
        }

        return new OrderSpecifier[]{post.id.desc()};
    }

    //작성자의 위치 필터
    private BooleanExpression eqLocation(Long locationId) {
        if (locationId == null) return null;
        // 작성자의 위치 ID가 일치하거나, 작성자의 위치 부모 ID가 일치하는 경우 (서울 선택 시 강남구 포함)
        return user.location.id.eq(locationId)
                .or(user.location.parent.id.eq(locationId));
    }

    //사용 언어 필터
    private BooleanExpression eqUsingLanguage(String usingLanguage) {
        if (usingLanguage == null) return null;
        return post.usingLanguage.eq(usingLanguage);
    }

    //학습 언어 필터
    private BooleanExpression eqLearningLanguage(String learningLanguage) {
        if (learningLanguage == null) return null;
        return post.learningLanguage.eq(learningLanguage);
    }

    //작성자의 관심사 필터
    private BooleanExpression hasInterest(Long interestId) {
        if (interestId == null) return null;
        //특정 관심사를 가진 유저가 작성한 글인지 체크
        return JPAExpressions
                .selectFrom(userInterest)
                .where(userInterest.user.eq(user)
                        .and(userInterest.interest.id.eq(interestId)))
                .exists();
    }
}