package com.ssup.backend.infra.s3;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Profile("test")
public class FakeImageStorage implements ImageStorage {

    private String fakeUrl = "https://fake.ssup/image/";

    @Override
    public String upload(ImageType type, MultipartFile file) {
        return fakeUrl + file.getOriginalFilename();
    }

    @Override
    public List<String> uploadMultiple(ImageType type, List<MultipartFile> files) {
        return files.stream()
                .map(file -> fakeUrl + file.getOriginalFilename())
                .toList();
    }

    @Override
    public void deleteByUrl(String key) {
    }
}
