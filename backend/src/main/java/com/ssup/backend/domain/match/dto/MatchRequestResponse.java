package com.ssup.backend.domain.match.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "매치 요청 응답 DTO")
public class MatchRequestResponse {

    private Long id;

    private Long requesterId;

    private Long receiverId;

    @Schema(description = "PENDING | ACCEPTED | REJECTED")
    private String status;

    private LocalDateTime requestedAt;

    private LocalDateTime acceptedAt;
}
