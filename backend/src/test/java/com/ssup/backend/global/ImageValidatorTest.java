package com.ssup.backend.global;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.global.util.ImageValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

class ImageValidatorTest {

    @Test
    @DisplayName("이미지 검증 - 성공")
    void validateImage_success() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png",
                new byte[1024]
        );

        ImageValidator.validateImage(file);
    }

    @Test
    @DisplayName("이미지 검증 - 실패(용량 초과)")
    void validateImage_fail_sizeExceeded() {
        MockMultipartFile file = new MockMultipartFile(
                "file", "big.png", "image/png",
                new byte[6 * 1024 * 1024]
        );

        assertThatThrownBy(() -> ImageValidator.validateImage(file))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.FILE_SIZE_EXCEEDED.getMessage());
    }

    @Test
    @DisplayName("이미지 리스트 검증 - 성공")
    void validateImages_success() {
        List<MultipartFile> files = List.of(
                new MockMultipartFile("f1","1.png","image/png", new byte[100]),
                new MockMultipartFile("f2","2.png","image/png", new byte[200])
        );

        ImageValidator.validateImages(files);
    }

    @Test
    @DisplayName("이미지 리스트 검증 - 실패 (일부 용량 초과)")
    void validateImages_fail_partialExceeded() {
        List<MultipartFile> files = List.of(
                new MockMultipartFile("f1","1.png","image/png", new byte[100]),
                new MockMultipartFile("f2","2.png","image/png", new byte[6 * 1024 * 1024])
        );

        assertThatThrownBy(() -> ImageValidator.validateImages(files))
                .isInstanceOf(SsupException.class)
                .hasMessageContaining(ErrorCode.FILE_SIZE_EXCEEDED.getMessage());
    }
}