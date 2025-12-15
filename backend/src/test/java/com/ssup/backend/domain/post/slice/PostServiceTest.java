package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostCreateRequest;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.post.dto.PostUpdateRequest;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserService;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;
    @Mock
    private UserService userService;
    @Mock
    private ImageStorage imageStorage;
    @InjectMocks
    private PostService postService;

    private final User user = getUser();

    @DisplayName("게시글을 작성한다 - 성공")
    @Test
    void createPost_success() {
        //given
        given(userService.findUserById(1L))
                .willReturn(user);
        given(imageStorage.uploadMultiple(eq(ImageType.POST), anyList()))
                .willReturn(List.of("url1", "url2"));
        given(postRepository.save(any(Post.class)))
                .willAnswer(invocation -> invocation.getArgument(0));

        List<MultipartFile> images = getMultipartFiles();

        PostCreateRequest request = PostCreateRequest.builder()
                .title("title1")
                .content("content1")
                .build();

        //when
        PostResponse response = postService.create(1L, images, request);

        //then
        assertThat(response.getImageUrls()).hasSize(2);

        then(imageStorage)
                .should(times(1))
                .uploadMultiple(ImageType.POST, images);

//        verify(imageStorage).uploadMultiple(eq(ImageType.POST), anyList());
    }

    @DisplayName("게시글을 수정한다 - 성공")
    @Test
    void updatePost_success() {
        Post post = getPost();

        //given
        given(imageStorage.uploadMultiple(eq(ImageType.POST), anyList()))
                .willReturn(List.of("url3", "url4"));
        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        List<MultipartFile> addedImages = getMultipartFiles();

        PostUpdateRequest request = PostUpdateRequest.builder()
                .title("new title")
                .content("new content")
                .keepImageUrls(List.of("url1"))
                .build();

        //when
        PostResponse response = postService.update(1L, 1L, addedImages, request);

        //then (기존 url1, url2 에서, url1만 유지 + url3, url4 추가)
        assertThat(response.getImageUrls()).hasSize(3);
        assertThat(response.getTitle()).isEqualTo(request.getTitle());

        then(imageStorage)
                .should(times(1))
                .uploadMultiple(ImageType.POST, addedImages);
    }

    //=== init ===

    private User getUser() {
        return User.builder()
                .id(1L)
                .email("minwoo123@gmail.com")
                .contact("010-1234-1234")
                .nickname("민우")
                .build();
    }

    private Post getPost() {
        return Post.builder()
                .id(1L)
                .title("안녕하세요 반가워요")
                .content("하이 한국어 공부하고 있어요~")
                .imageUrls(new ArrayList<>(List.of("url1", "url2")))
                .author(user)
                .build();
    }

    private List<MultipartFile> getMultipartFiles() {
        return List.of(
                new MockMultipartFile(
                        "image1",
                        "test1.png",
                        "image/png",
                        "data" .getBytes()
                ),
                new MockMultipartFile(
                        "image2",
                        "test2.png",
                        "image/png",
                        "data" .getBytes()
                )
        );
    }
}
