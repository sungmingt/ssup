package com.ssup.backend.infra.s3;

import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static com.ssup.backend.global.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Service
@Profile({"local", "prod"})
@RequiredArgsConstructor
public class S3ImageStorage implements ImageStorage {

    private final S3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.s3.base-url}")
    private String baseUrl;

    @Override
    public String upload(ImageType type, MultipartFile file) {
        String key = createKey(type, file);

        try {
            s3Client.putObject(
                    PutObjectRequest.builder()
                            .bucket(bucket)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    RequestBody.fromBytes(file.getBytes())
            );
        } catch (IOException e) {
            throw new SsupException(INTERNAL_SERVER_ERROR);
        }

        return getUrl(key);
    }

    @Override
    public List<String> uploadMultiple(ImageType type, List<MultipartFile> files) {
        List<String> urls = new ArrayList<>();

        for(MultipartFile file : files) {
            String key = createKey(type, file);

            try {
                s3Client.putObject(
                        PutObjectRequest.builder()
                                .bucket(bucket)
                                .key(key)
                                .contentType(file.getContentType())
                                .build(),
                        RequestBody.fromBytes(file.getBytes())
                );
            } catch (IOException e) {
                throw new SsupException(INTERNAL_SERVER_ERROR);
            }

            urls.add(getUrl(key));
        }

        return urls;
    }

    @Override
    public void delete(String key) {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );
    }

    private String createKey(ImageType type, MultipartFile file) {
        return type + "/" + UUID.randomUUID() + "-" + file.getOriginalFilename();
    }

    private String getUrl(String key) {
        return baseUrl + key;
    }
}
