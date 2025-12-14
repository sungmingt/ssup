package com.ssup.backend.infra.s3;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@Profile("test")
public class FakeImageStorage implements ImageStorage {

    @Override
    public String upload(MultipartFile file) {
        return "https://fake.ssup/image/" + file.getOriginalFilename();
    }

    @Override
    public void delete(String key) {
    }
}
