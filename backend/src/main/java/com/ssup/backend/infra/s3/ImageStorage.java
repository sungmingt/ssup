package com.ssup.backend.infra.s3;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ImageStorage {

    String upload(ImageType type, MultipartFile file);
    List<String> uploadMultiple(ImageType type, List<MultipartFile> file);
    void deleteByUrl(String url);
}