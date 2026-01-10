package com.ssup.backend.domain.post.sort;

import com.ssup.backend.domain.heart.post.PostHeartRepository;
import com.ssup.backend.domain.post.*;
import com.ssup.backend.domain.post.dto.PostListResponse;
import com.ssup.backend.domain.post.dto.PostSliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class PostSliceFetcher {

    private final PostQueryRepository queryRepository;
    private final PostHeartRepository postHeartRepository;

    public PostSliceResponse fetch(Long userId,
                                   PostFilterCondition condition,
                                   PostSortType sortType,
                                   Long cursorKey,
                                   Long cursorId,
                                   int size
    ) {
        List<Post> posts = queryRepository.findPostsByCondition(condition, sortType, cursorKey, cursorId, size);

        //1개 더 가져왔을때, 실제로 1개 더 있다면 다음 페이지가 존재 -> hasNext 초기화
        boolean hasNext = posts.size() > size;

        //초기화용이기 떄문에 삭제
        if (hasNext) {
            posts.remove(size);
        }

        //유저가 좋아요한 글 목록 조회
        Set<Long> heartedPostIds = findHeartedPostIds(userId, posts);

        List<PostListResponse> items = PostListResponse.of(posts, heartedPostIds);

        //다음 커서 저장
        Post lastPost = posts.isEmpty() ? null : posts.get(posts.size() - 1);
        Cursor nextCursor = Cursor.from(lastPost, sortType);

        return new PostSliceResponse(
                items,
                nextCursor.getKey(),
                nextCursor.getId(),
                hasNext
        );
    }

    private Set<Long> findHeartedPostIds(Long userId, List<Post> posts) {
        Set<Long> heartedPostIds = Collections.emptySet();

        if (userId != null && !posts.isEmpty()) {
            List<Long> postIds = posts.stream()
                    .map(Post::getId)
                    .toList();

            heartedPostIds = new HashSet<>(
                    postHeartRepository.findHeartedPostIds(userId, postIds)
            );
        }

        return heartedPostIds;
    }
}
