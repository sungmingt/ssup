package com.ssup.backend.domain.post.slice;

import com.ssup.backend.domain.post.PostController;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PostController.class)
@ContextConfiguration(classes = PostController.class) //Controller test: JPA 컨텍스트 로딩 x -> Jpa Auditing 설정 테스트에서 제외
class PostControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    PostService postService;

    @Test
    @DisplayName("게시글 상세 조회 API")
    void findPost() throws Exception {
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
        mockMvc.perform(get("/api/posts/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("제목"))
                .andExpect(jsonPath("$.authorName").value("sam"));
    }
}
