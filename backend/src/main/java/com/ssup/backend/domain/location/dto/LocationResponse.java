package com.ssup.backend.domain.location.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "사용자 위치 정보 DTO")
public class LocationResponse {

    @Schema(description = "위치 ID", example = "1")
    private Long id;

    @Schema(description = "위치 이름", example = "서울특별시")
    private String name;

    @Schema(description = "위치 레벨", example = "1")
    private int level;

    @Schema(description = "상위 위치")
    private LocationResponse parent;
}