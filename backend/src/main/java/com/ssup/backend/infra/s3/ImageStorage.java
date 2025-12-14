package com.ssup.backend.infra.s3;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorage {

    String upload(MultipartFile file);

    void delete(String key);
}