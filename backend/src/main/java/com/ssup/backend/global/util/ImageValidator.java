package com.ssup.backend.global.util;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageValidator {

    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png", "image/webp");

    private static final long MAX_SIZE = 5 * 1024 * 1024; // 5MB

    private void validateImage(MultipartFile file) {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("이미지 파일만 업로드 가능합니다.");//todo: custom error
        }
        if (file.getSize() > MAX_SIZE) {
            throw new IllegalArgumentException("파일 크기는 5MB 이하만 허용됩니다."); //todo: custom error
        }
    }

}
