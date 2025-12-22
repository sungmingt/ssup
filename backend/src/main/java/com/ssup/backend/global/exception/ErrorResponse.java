package com.ssup.backend.global.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorResponse {

    private final String code; //비즈니스적 code (httpStatus X)
    private final String message;
    private final List<ErrorDetail> errors;

    public static ErrorResponse of(ErrorCode errorCode) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.getMessage(),
                List.of()
        );
    }

    public static ErrorResponse of(ErrorCode errorCode, List<ErrorDetail> errors) {
        return new ErrorResponse(
                errorCode.name(),
                errorCode.getMessage(),
                errors
        );
    }

    public static ErrorResponse validation(ErrorCode errorCode, BindingResult bindingResult) {
        return of(errorCode, ErrorDetail.from(bindingResult));
    }

    public static ErrorResponse validation(ErrorCode errorCode, Set<ConstraintViolation<?>> violations) {
        return of(errorCode, ErrorDetail.from(violations));
    }
}
