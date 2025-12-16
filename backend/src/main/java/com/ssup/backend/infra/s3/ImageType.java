package com.ssup.backend.infra.s3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImageType {

    PROFILE("profile"),
    DEFAULT("default"),
    POST("post"),
    COMMENT("comment");

    private final String value;
}
