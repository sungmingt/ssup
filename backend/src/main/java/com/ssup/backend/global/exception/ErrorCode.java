package com.ssup.backend.global.exception;

import lombok.Getter;

public enum ErrorCode {

    //USER
    USER_NOT_FOUND(404, "존재하지 않는 회원입니다."),
    USER_NOT_AUTHORIZED(403, "로그인되지 않은 사용자입니다."),
    USER_ALREADY_AUTHORIZED(400, "이미 로그인 상태입니다."),
    NOT_RESOURCE_OWNER(403, "해당 리소스의 소유자가 아닙니다."),

    //USER INFO
    EMAIL_ALREADY_EXISTS(409, "이미 존재하는 이메일입니다."),
    EMAIL_NOT_EXISTS(400, "존재하지 않는 이메일입니다."),
    EMAIL_NOT_VALID(400, "이메일 형식이 올바르지 않습니다."),
    PASSWORD_NOT_VALID(400, "비밀번호 형식이 올바르지 않습니다."),
    PASSWORD_NOT_MATCH(400, "비밀번호가 일치하지 않습니다."),

    //AUTH
    ILLEGAL_REGISTRATION_ID(400, "잘못된 registration id입니다."),
    INVALID_SOCIAL_TYPE(400, "올바르지 않은 소셜 로그인 제공자입니다."),

    //TOKEN
    ACCESS_TOKEN_EXPIRED(401, "access token 만료, reissue 요청이 필요합니다."),
    REFRESH_TOKEN_EXPIRED(401, "refresh token 만료, 강제 로그아웃"),
    ACCESS_TOKEN_INVALID(401, "유효하지 않은 access token 입니다."),
    REFRESH_TOKEN_INVALID(401, "유효하지 않은 refresh token 입니다."),
    REQUIRED_TOKEN_MISSING(401, "토큰이 존재하지 않습니다."),
    TOKEN_FROM_BLACKLIST(401, "blacklist에 등록된 토큰입니다."),
    PAYLOAD_NOT_VALID(401, "토큰의 payload가 유효하지 않습니다."),

    TOKEN_INVALID(401, "유효하지 않은 토큰입니다."),
    TOKEN_SIGNATURE_INVALID(401, "올바르지 않은 토큰 시그니처입니다."),
    TOKEN_EXPIRED(401, "만료된 토큰입니다."),

    //FILE
    IMAGE_NOT_FOUND(404, "이미지가 존재하지 않습니다."),
    FILE_NOT_FOUND(404, "파일이 존재하지 않습니다."),

    //MATCH
    BUNGAE_NOT_FOUND(404, "존재하지 않는 번개입니다."),
    NO_BUNGAEJOIN_HISTORY(404, "해당 유저의 번개 참여 이력이 존재하지 않습니다."),

    //COMMENT
    COMMENT_NOT_FOUND(404, "존재하지 않는 댓글입니다."),

    //INTEREST
    INTEREST_NOT_FOUND(400, "사용할 수 없는 관심사 항목입니다."),

    //POST
    POST_NOT_FOUND(404, "존재하지 않는 게시글입니다."),

    //GLOBAL
    INVALID_REQUEST(400, "요청 값이 올바르지 않습니다."),
    METHOD_NOT_ALLOWED(405, "허용되지 않은 HTTP 메서드입니다."),
    INTERNAL_SERVER_ERROR(500, "서버 내부 오류입니다.");

    @Getter
    private final int status;
    @Getter
    private final String message;

    ErrorCode(final int code, final String message){
        this.status = code;
        this.message = message;
    }
}