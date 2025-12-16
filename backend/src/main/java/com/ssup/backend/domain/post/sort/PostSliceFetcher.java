package com.ssup.backend.domain.post.sort;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.dto.PostListResponse;
import com.ssup.backend.domain.post.dto.PostSliceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PostSliceFetcher {

    private final PostRepository postRepository;

    public PostSliceResponse fetch(PostSortType sortType,
                                   Long cursorKey,
                                   Long cursorId,
                                   int size
    ) {
        //하나 더 가져와서 다음 페이지 존재 여부(hasNext) 판단
        Pageable pageable = PageRequest.of(0, size + 1);

        //정렬 조건 분기
        List<Post> posts = switch (sortType) {
            case LATEST -> postRepository.findLatest(cursorId, pageable);
            case VIEWS -> postRepository.findByViews(cursorKey, cursorId, pageable);
        };

        //1개 더 가져왔을때, 실제로 1개 더 있다면 다음 페이지가 존재 -> hasNext 초기화
        boolean hasNext = posts.size() > size;

        //초기화용이기 떄문에 삭제
        if (hasNext) posts.remove(size);

        List<PostListResponse> items = PostListResponse.of(posts);

        //다음 커서 저장
        Cursor nextCursor = Cursor.from(
                posts.isEmpty() ? null : posts.get(posts.size() - 1),
                sortType
        );

        return new PostSliceResponse(items, nextCursor.getKey(), nextCursor.getId(), hasNext);
    }
}
