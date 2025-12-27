package com.ssup.backend.infra.security.oauth;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.infra.security.oauth.info.OAuth2UserInfo;
import com.ssup.backend.infra.security.oauth.info.OAuth2UserInfoFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    //여기서는 유저 정보 입력 및 생성
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        OAuth2UserInfo userInfo =
                OAuth2UserInfoFactory.create(
                        userRequest.getClientRegistration().getRegistrationId(),
                        oAuth2User.getAttributes()
                );

        User user = saveOrCreateUser(userInfo);

        return new DefaultOAuth2User(
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                Map.of("userId", user.getId()),
                "userId"
        );
    }

    private User saveOrCreateUser(OAuth2UserInfo oAuth2UserInfo) {
        Optional<User> socialUser =
                userRepository.findBySocialTypeAndSocialId(
                        oAuth2UserInfo.getSocialType(), oAuth2UserInfo.getSocialId()
                );

        if (socialUser.isPresent()) {
            return socialUser.get();
        }

        return userRepository.save(oAuth2UserInfo.toEntity());
    }
}
