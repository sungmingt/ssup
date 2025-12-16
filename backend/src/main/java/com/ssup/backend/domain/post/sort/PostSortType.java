package com.ssup.backend.domain.post.sort;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;

import java.util.List;

@AllArgsConstructor
public enum PostSortType {

    //최신순
    LATEST {
        @Override
        public List<Post> fetch(PostRepository repository,
                                Long cursorKey,
                                Long cursorId,
                                Pageable pageable
        ) {
            return repository.findLatest(cursorId, pageable);
        }

        @Override
        public Long extractCursorKey(Post post) {
            return null;
        }
    },

    //조회수순
    VIEWS {
        @Override
        public List<Post> fetch(PostRepository repository,
                                Long cursorKey,
                                Long cursorId,
                                Pageable pageable
        ) {
            return repository.findByViews(cursorKey, cursorId, pageable);
        }

        @Override
        public Long extractCursorKey(Post post) {
            return post.getViewCount();
        }
    };

    //=== abstract ===

    public abstract List<Post> fetch(PostRepository repository,
                                     Long cursorKey,
                                     Long cursorId,
                                     Pageable pageable
    );

    public abstract Long extractCursorKey(Post post);
}
