package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.Post;
import com.ssup.backend.domain.post.PostRepository;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostCreateRequest;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserService;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import com.ssup.backend.infra.s3.S3ImageStorage;
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
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.times;
import static org.mockito.Mockito.verify;

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
    private final Post post = getPost();

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

    //--- init ---
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
                .imageUrls(List.of("s3.aws.com", "s3.aws.com"))
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
                        "imag2",
                        "test2.png",
                        "image/png",
                        "data" .getBytes()
                )
        );
    }
}
