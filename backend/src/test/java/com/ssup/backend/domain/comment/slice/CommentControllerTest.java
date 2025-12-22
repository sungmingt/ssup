package com.ssup.backend.domain.comment.slice;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ssup.backend.domain.comment.CommentController;
import com.ssup.backend.domain.comment.CommentService;
import com.ssup.backend.domain.comment.dto.CommentCreateRequest;
import com.ssup.backend.domain.comment.dto.CommentResponse;
import com.ssup.backend.domain.comment.dto.CommentUpdateRequest;
import com.ssup.backend.global.exception.GlobalControllerAdvice;
import com.ssup.backend.global.exception.SsupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static com.ssup.backend.global.exception.ErrorCode.COMMENT_NOT_FOUND;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;

import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommentController.class)
@Import(GlobalControllerAdvice.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    @Autowired
    private ObjectMapper objectMapper;

    @DisplayName("이미지가 없는 댓글 작성 - 성공")
    @Test
    void createComment_withoutImage_success() throws Exception {
        //given
        Long postId = 10L;
        CommentCreateRequest request = new CommentCreateRequest("댓글 내용");
        CommentResponse response = getCommentResponse(1L);

        MockMultipartFile dto = getDtoPart(request);

        given(commentService.create(anyLong(), eq(postId), any(), any()))
                .willReturn(response);

        //when, then
        mockMvc.perform(
                        multipart("/api/posts/{postId}/comments", postId)
                                .file(dto)
                                .contentType(MediaType.MULTIPART_FORM_DATA)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("댓글 내용"))
                .andExpect(jsonPath("$.imageUrl").doesNotExist());
    }

    @DisplayName("이미지를 포함한 댓글 작성 - 성공")
    @Test
    void createComment_withImage_success() throws Exception {
        //given
        Long postId = 10L;
        CommentCreateRequest request = new CommentCreateRequest("댓글 내용");
        CommentResponse response = getCommentResponse(1L);

        given(commentService.create(anyLong(), eq(postId), any(), any()))
                .willReturn(response);

        MockMultipartFile dto = getDtoPart(request);
        MockMultipartFile image = getImagePart();

        //when, then
        mockMvc.perform(
                        multipart("/api/posts/{postId}/comments", postId)
                                .file(dto)
                                .file(image)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.imageUrl").value(response.getImageUrl()));
    }

    @DisplayName("댓글 수정 api - 성공")
    @Test
    void updateComment_success() throws Exception {
        //given
        Long postId = 10L;
        Long commentId = 3L;

        CommentUpdateRequest request = new CommentUpdateRequest("수정된 내용", false);
        CommentResponse response = new CommentResponse(commentId, postId, 1L, "image-url", "name", "수정된 내용", null, 0L, LocalDateTime.now());

        given(commentService.update(anyLong(), eq(postId), eq(commentId), any(), any()))
                .willReturn(response);

        MockMultipartFile dto = getDtoPart(request);

        //when, then
        mockMvc.perform(
                        multipart("/api/posts/{postId}/comments/{id}", postId, commentId)
                                .file(dto)
                                .with(req -> {
                                    req.setMethod("PUT");
                                    return req;
                                })
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value("수정된 내용"));
    }

    @DisplayName("댓글 삭제 api - 성공")
    @Test
    void deleteComment_success() throws Exception {
        //given
        Long postId = 10L;
        Long commentId = 3L;

        //when, then
        mockMvc.perform(
                        delete("/api/posts/{postId}/comments/{id}", postId, commentId)
                )
                .andExpect(status().isOk());

        verify(commentService).delete(1L, postId, commentId);
    }

    @DisplayName("댓글 단건 조회 api - 성공")
    @Test
    void findComment_success() throws Exception {
        //given
        Long postId = 10L;
        Long commentId = 5L;
        CommentResponse response = getCommentResponse(commentId);

        given(commentService.find(postId, commentId))
                .willReturn(response);

        //when, then
        mockMvc.perform(
                        get("/api/posts/{postId}/comments/{id}", postId, commentId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(commentId));
    }

    @DisplayName("댓글 목록 조회 api - 성공")
    @Test
    void findCommentList_success() throws Exception {
        //given
        Long postId = 10L;

        CommentResponse response1 = getCommentResponse(1L);
        CommentResponse response2 = getCommentResponse(2L);
        List<CommentResponse> responses = List.of(response1, response2);

        given(commentService.findList(postId)).willReturn(responses);

        //when, then
        mockMvc.perform(
                        get("/api/posts/{postId}/comments", postId)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @DisplayName("존재하지 않는 댓글 조회 - 404")
    @Test
    void findComment_notFound_return404() throws Exception {
        //given
        Long postId = 10L;
        Long commentId = 999L;

        given(commentService.find(postId, commentId))
                .willThrow(new SsupException(COMMENT_NOT_FOUND));

        //when, then
        mockMvc.perform(
                        get("/api/posts/{postId}/comments/{id}", postId, commentId)
                )
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(COMMENT_NOT_FOUND.name()));
    }

    //=== init ===

    private MockMultipartFile getImagePart() {
        return new MockMultipartFile(
                "images",
                "test.png",
                MediaType.IMAGE_PNG_VALUE,
                "data".getBytes()
        );
    }

    private MockMultipartFile getDtoPart(Object dto) throws JsonProcessingException {
        return new MockMultipartFile(
                "dto",
                "",
                "application/json",
                objectMapper.writeValueAsBytes(dto)
        );
    }

    private static CommentResponse getCommentResponse(Long id) {
        return new CommentResponse(
                        id,
                        1L,
                        1L,
                "image-url",
                        "name",
                        "댓글 내용",
                        null,
                        0L,
                        LocalDateTime.now()
        );
    }
}