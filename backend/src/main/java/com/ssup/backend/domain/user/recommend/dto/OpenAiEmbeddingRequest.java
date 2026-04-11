package com.ssup.backend.domain.user.recommend.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OpenAiEmbeddingRequest {

    private String model;
    private String input;
}