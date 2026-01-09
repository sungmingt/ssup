package com.ssup.backend.global;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.ErrorDetail;
import com.ssup.backend.global.exception.ErrorResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Path;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.Set;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class ErrorResponseTest {

    @Test
    @DisplayName("ErrorCode 기반 ErrorResponse 생성")
    void of_errorCode() {
        ErrorResponse response = ErrorResponse.of(ErrorCode.USER_NOT_FOUND);
        assertThat(response.getCode()).isEqualTo(ErrorCode.USER_NOT_FOUND.name());
        assertThat(response.getMessage()).isEqualTo(ErrorCode.USER_NOT_FOUND.getMessage());
        assertThat(response.getErrors()).isEmpty();
    }

    @Test
    @DisplayName("BindingResult 기반 validation ErrorResponse 생성")
    void validation_fromBindingResult() {
        //given
        BindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "target");

        bindingResult.addError(new FieldError(
                "target",
                "email",
                "test@test",
                false,
                null,
                null,
                "이메일 형식 오류"
        ));

        //when
        ErrorResponse response =
                ErrorResponse.validation(ErrorCode.INVALID_REQUEST, bindingResult);

        //then
        assertThat(response.getErrors()).hasSize(1);

        ErrorDetail detail = response.getErrors().get(0);
        assertThat(detail.getField()).isEqualTo("email");
        assertThat(detail.getRejectedValue()).isEqualTo("test@test");
        assertThat(detail.getReason()).isEqualTo("이메일 형식 오류");
    }

    @Test
    @DisplayName("ConstraintViolation 기반 validation ErrorResponse 생성")
    void validation_fromConstraintViolation() {
        //given
        ConstraintViolation<?> violation = mock(ConstraintViolation.class);
        Path path = mock(Path.class);

        given(path.toString()).willReturn("age");
        given(violation.getPropertyPath()).willReturn(path);
        given(violation.getInvalidValue()).willReturn(0);
        given(violation.getMessage()).willReturn("나이는 필수입니다");

        //when
        ErrorResponse response =
                ErrorResponse.validation(ErrorCode.INVALID_REQUEST, Set.of(violation));

        //then
        ErrorDetail detail = response.getErrors().get(0);
        assertThat(detail.getField()).isEqualTo("age");
        assertThat(detail.getRejectedValue()).isEqualTo(0);
        assertThat(detail.getReason()).isEqualTo("나이는 필수입니다");
    }
}
