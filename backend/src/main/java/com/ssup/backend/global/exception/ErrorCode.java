package com.ssup.backend.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {

    //USER
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 회원입니다."),
    USER_NOT_AUTHORIZED(HttpStatus.FORBIDDEN, "로그인되지 않은 사용자입니다."),
    LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "로그인이 필요한 기능입니다."),
    USER_STATUS_PENDING(HttpStatus.FORBIDDEN, "추가 정보를 입력해주세요."),
    DELETED_USER(HttpStatus.FORBIDDEN, "탈퇴한 사용자 계정입니다."),
    USER_ALREADY_AUTHORIZED(HttpStatus.BAD_REQUEST, "이미 로그인 상태입니다."),
    NOT_RESOURCE_OWNER(HttpStatus.FORBIDDEN, "해당 리소스의 소유자가 아닙니다."),

    //USER INFO
    NICKNAME_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 사용중인 닉네임입니다."),
    EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    EMAIL_NOT_EXISTS(HttpStatus.BAD_REQUEST, "존재하지 않는 이메일입니다."),
    EMAIL_NOT_VALID(HttpStatus.BAD_REQUEST, "이메일 형식이 올바르지 않습니다."),
    PASSWORD_NOT_VALID(HttpStatus.BAD_REQUEST, "비밀번호 형식이 올바르지 않습니다."),
    PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다."),

    //AUTH
    ILLEGAL_REGISTRATION_ID(HttpStatus.BAD_REQUEST, "잘못된 registration id입니다."),
    INVALID_SOCIAL_TYPE(HttpStatus.BAD_REQUEST, "올바르지 않은 소셜 로그인 제공자입니다."),

    //TOKEN
    ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 access token입니다."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 refresh token입니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),

    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 존재하지 않습니다."),
    REFRESH_TOKEN_MISMATCH(HttpStatus.UNAUTHORIZED, "리프레시 토큰이 일치하지 않습니다."),

    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰입니다."),
    INVALID_ACCESS_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 액세스 토큰입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    TOKEN_SIGNATURE_INVALID(HttpStatus.UNAUTHORIZED, "올바르지 않은 토큰 시그니처입니다."),

    TOKEN_FROM_BLACKLIST(HttpStatus.UNAUTHORIZED, "blacklist에 등록된 토큰입니다."),
    TOKEN_REISSUED(HttpStatus.OK, "토큰이 재발급되었습니다."),

    //FILE
    IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "이미지가 존재하지 않습니다."),
    FILE_NOT_FOUND(HttpStatus.NOT_FOUND, "파일이 존재하지 않습니다."),

    //MATCH
    BUNGAE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 번개입니다."),
    NO_BUNGAEJOIN_HISTORY(HttpStatus.NOT_FOUND, "해당 유저의 번개 참여 이력이 존재하지 않습니다."),

    //COMMENT
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),
    NOT_COMMENT_OWNER(HttpStatus.FORBIDDEN, "댓글 작성자가 아닙니다."),
    COMMENT_CONTENT_INVALID(HttpStatus.BAD_REQUEST, "댓글 내용은 비어 있을 수 없습니다."),

    //INTEREST
    INTEREST_NOT_FOUND(HttpStatus.BAD_REQUEST, "사용할 수 없는 관심사 항목입니다."),

    //HEART
    HEART_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 좋아요한 리소스입니다."),

    //POST
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    NOT_POST_OWNER(HttpStatus.FORBIDDEN, "글 작성자가 아닙니다."),

    //GLOBAL
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "요청 값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류입니다."),
    TOO_MANY_TRAFFIC(HttpStatus.CONFLICT, "현재 요청이 많아 처리할 수 없습니다."),

    //LANGUAGE
    LANGUAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "제공되지 않는 언어입니다."),

    //LOCATION
    LOCATION_NOT_FOUND(HttpStatus.NOT_FOUND, "올바르지 않은 지역 정보입니다."),
    INVALID_LOCATION_LEVEL(HttpStatus.BAD_REQUEST, "지역 정보는 군/구 단위여야 합니다.");

    @Getter
    private final HttpStatus status;
    @Getter
    private final String message;

    ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }
}