package com.ssup.backend.domain.post.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.post.PostController;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostCreateRequest;
import com.ssup.backend.domain.post.dto.PostResponse;
import com.ssup.backend.domain.post.dto.PostUpdateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ContextConfiguration(classes = PostController.class) //Controller test: JPA 컨텍스트 로딩 x -> Jpa Auditing 설정 테스트에서 제외
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    //todo: 인증 구현 후 userId -> AppUser

    @Test
    @DisplayName("게시글 상세 조회 api - 성공")
    void findPost_success() throws Exception {
        //given
        given(postService.find(1L))
                .willReturn(
                        PostResponse.builder()
                                .id(1L)
                                .title("제목")
                                .authorName("sam")
                                .build()
                );

        //when & then
        mockMvc.perform(get("/api/posts/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.authorName").value("sam"));
    }

    @Test
    @DisplayName("게시글 작성 api - 성공")
    void createPost_success() throws Exception {
        PostResponse response = getPostResponse();
        PostCreateRequest request = PostCreateRequest.builder().title("title1").build();

        //given
        given(postService.create(anyLong(), anyList(), any()))
                .willReturn(response);

        MockMultipartFile dto = getDtoPart(request);
        MockMultipartFile images = getImagePart();

        mockMvc.perform(
                        multipart("/api/posts")
                                .file(images)
                                .file(dto)
                                .param("userId", "1")
                )
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("게시글 수정 api - 성공")
    void updatePost_success() throws Exception {
        PostResponse response = getPostResponse();
        PostUpdateRequest request = PostUpdateRequest.builder().title("title1").build();

        //given
        given(postService.update(anyLong(), anyLong(), anyList(), any()))
                .willReturn(response);

        MockMultipartFile dto = getDtoPart(request);
        MockMultipartFile images = getImagePart();

        mockMvc.perform(
                        multipart("/api/posts/{id}", 1)
                                .file(images)
                                .file(dto)
                                .param("userId", "1")
                                .with(req -> {
                                    req.setMethod(HttpMethod.PUT.name());
                                    return req;
                                })
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 삭제 api - 성공")
    void deletePost_success() throws Exception {
        //given

        mockMvc.perform(
                        delete("/api/posts/{id}", 1)
                                .with(req -> {
                                    req.setMethod(HttpMethod.DELETE.name());
                                    return req;
                                })
                )
                .andExpect(status().isNoContent());

        BDDMockito.verify(postService).delete(1L);
    }

    //=== init ===

    private PostResponse getPostResponse() {
        return PostResponse.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .build();
    }

    private MockMultipartFile getDtoPart(Object dto) throws Exception {
        return new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
    }

    private MockMultipartFile getImagePart() throws Exception {
        return new MockMultipartFile(
                "images",
                "test.png",
                "image/png",
                "data".getBytes()
        );
    }
}
