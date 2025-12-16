package com.ssup.backend.global.exception;

import jakarta.validation.ConstraintViolation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.validation.BindingResult;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ErrorDetail {

    private final String field;
    private final Object rejectedValue;
    private final String reason;

    public static List<ErrorDetail> from(BindingResult bindingResult) {
        return bindingResult.getFieldErrors().stream()
                .map(error -> new ErrorDetail(
                        error.getField(),
                        error.getRejectedValue(),
                        error.getDefaultMessage()
                ))
                .toList();
    }

    public static List<ErrorDetail> from(Set<ConstraintViolation<?>> violations) {
        return violations.stream()
                .map(v -> new ErrorDetail(
                        v.getPropertyPath().toString(),
                        v.getInvalidValue(),
                        v.getMessage()
                ))
                .toList();
    }
}
