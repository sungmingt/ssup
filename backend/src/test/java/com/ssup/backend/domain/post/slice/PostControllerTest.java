package com.ssup.backend.domain.post.slice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.post.PostController;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.*;
import com.ssup.backend.domain.post.sort.PostSortType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    @MockBean
    PostService postService;

    @Autowired
    ObjectMapper objectMapper;

    //todo: 인증 구현 후 userId -> AppUser

    @DisplayName("최신순 목록 조회 - 성공")
    @Test
    void findList_latest_success() throws Exception {
        //given
        given(postService.findList(
                eq(1L),
                eq(PostSortType.LATEST),
                isNull(),
                isNull(),
                eq(15)
        )).willReturn(new PostSliceResponse(List.of(), 10L, 3L, false));

        //when, then
        mockMvc.perform(get("/api/posts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isArray())
                .andExpect(jsonPath("$.hasNext").exists());
    }

    @DisplayName("조회수순 목록 조회 - 성공")
    @Test
    void findList_sortByViews_success() throws Exception {
        //given
        given(postService.findList(
                eq(1L),
                eq(PostSortType.VIEWS),
                eq(250L),
                eq(10L),
                eq(3)
        )).willReturn(new PostSliceResponse(List.of(), 10L, 3L, false));

        //when, then
        mockMvc.perform(
                        get("/api/posts")
                                .param("sortType", PostSortType.VIEWS.name())
                                .param("cursorKey", "250")
                                .param("cursorValue", "10")
                                .param("size", "3")
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("게시글 상세 조회 api - 성공")
    void findPost_success() throws Exception {
        //given
        given(postService.find(1L, 1L))
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
        PostCreateResponse response = getPostCreateResponse();
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
        PostUpdateResponse response = getPostUpdateResponse();
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

    private PostCreateResponse getPostCreateResponse() {
        return PostCreateResponse.builder()
                .id(1L)
                .title("title1")
                .content("content1")
                .build();
    }

    private PostUpdateResponse getPostUpdateResponse() {
        return PostUpdateResponse.builder()
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
