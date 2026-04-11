package com.ssup.backend.domain.user.recommend.test;

import com.ssup.backend.domain.user.recommend.OpenAiClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmbeddingTestService {

    private final OpenAiClient openAiClient;

    public EmbeddingTestService(OpenAiClient openAiClient) {
        this.openAiClient = openAiClient;
    }

    public int testEmbeddingSize() {
        List<Double> embedding = openAiClient.embed("Hello AI");
        return embedding.size(); // 보통 3072
    }
}