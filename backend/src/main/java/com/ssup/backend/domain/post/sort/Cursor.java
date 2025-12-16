package com.ssup.backend.domain.post.sort;

import com.ssup.backend.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Cursor {

    private final Long id;
    private final Long key;

    public static Cursor empty() {
        return new Cursor(null, null);
    }

    public boolean isEmpty() {
        return id == null;
    }

    public static Cursor from(Post post, PostSortType sort) {
        if (post == null) {
            return empty();
        }

        return new Cursor(
                post.getId(),
                sort.extractCursorKey(post)
        );
    }
}
