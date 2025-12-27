package com.ssup.backend.infra.security.oauth.info;

import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;

import java.util.Map;

public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo create(String registrationId,
                                        Map<String, Object> attributes) {

        return switch (registrationId) {
            case "google" -> new GoogleOAuth2UserInfo(attributes);
            case "kakao" -> new KakaoOAuth2UserInfo(attributes);
            default -> throw new SsupException(ErrorCode.ILLEGAL_REGISTRATION_ID);
        };
    }
}