package com.ssup.backend.domain.user.recommend;

import com.ssup.backend.domain.user.recommend.dto.OpenAiEmbeddingRequest;
import com.ssup.backend.domain.user.recommend.dto.OpenAiEmbeddingResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Component
@Slf4j
public class OpenAiClient {

    //WebClient를 통해 openApi 호출
    //openApi key 등 설정 -> openApi embedding 호출
    //실제 호출 및 결과 저장은 UserEmbeddingService에서 진행

    private final WebClient webClient;

    public OpenAiClient(
            @Value("${openai.api.key}") String apiKey
    ) {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.openai.com/v1")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    public List<Double> embed(String text) {
        OpenAiEmbeddingRequest request = new OpenAiEmbeddingRequest();
        request.setModel("text-embedding-3-large");
        request.setInput(text);

        log.info("### request 생성 완료");
        OpenAiEmbeddingResponse response = webClient.post()
                .uri("/embeddings")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(OpenAiEmbeddingResponse.class)
                .block();

        log.info("### response 응답 완료");
        return response.getData().get(0).getEmbedding();
    }
}