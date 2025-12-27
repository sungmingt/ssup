package com.ssup.backend.domain.auth.oauth;

import com.ssup.backend.domain.user.SocialType;

import java.util.Map;

public class KakaoOAuth2UserInfo implements OAuth2UserInfo {

    private final Map<String, Object> attributes;

    public KakaoOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getName() {
        return (String) getProfile().get("nickname");
    }

    @Override
    public String getEmail() {
        Map<String, Object> account = getAccount();
        return null;
    }

    @Override
    public SocialType getSocialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getSocialId() {
        return String.valueOf(attributes.get("id"));
    }

    private Map<String, Object> getAccount() {
        return (Map<String, Object>) attributes.get("kakao_account");
    }

    private Map<String, Object> getProfile() {
        return (Map<String, Object>) getAccount().get("profile");
    }
}
