package com.ssup.backend.global.util;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class ImageValidator {

    private static final List<String> ALLOWED_TYPES =
            List.of("image/jpeg", "image/png", "image/webp");

    private static final long MAX_SIZE = 5 * 1024 * 1024; //5MB

    public static void validateImage(MultipartFile file) {
        if (file.getSize() > MAX_SIZE) {
            throw new SsupException(ErrorCode.FILE_SIZE_EXCEEDED);        }
    }

    public static void validateImages(List<MultipartFile> images) {
        for (MultipartFile image : images) {
            if (image.getSize() > MAX_SIZE) {
                throw new SsupException(ErrorCode.FILE_SIZE_EXCEEDED);
            }
        }
    }
}
