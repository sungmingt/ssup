package com.ssup.backend.domain.auth;

import com.ssup.backend.domain.auth.dto.SignUpRequest;
import com.ssup.backend.domain.auth.dto.SignUpResponse;
import com.ssup.backend.domain.user.SocialType;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;

    public SignUpResponse signUp(SignUpRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new SsupException(ErrorCode.EMAIL_ALREADY_EXISTS);
        }

        if (userRepository.existsByNickname(request.getNickname())) {
            throw new SsupException(ErrorCode.NICKNAME_ALREADY_EXISTS);
        }

        User user = User.builder()
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password((request.getPassword())) //todo: 암호화
                .status(UserStatus.PENDING)
                .socialType(SocialType.NONE)
                .build();

        User savedUser = userRepository.save(user);
        return SignUpResponse.of(savedUser);
    }



//    public Long signUp(OAuthSignUpRequest request) {
//        OAuthUserInfo info = oauthService.getUserInfo(request);
//        User user = createSocialUser(info);
//
//    User user = User.builder()
//            .email(oauthEmail)
//            .nickname(oauthNickname)
//            .imageUrl(oauthProfileImage)
//            .status(PENDING)
//            .socialType(KAKAO)
//            .socialId(oauthId)
//            .build();
//        return user.getId();
//    }
}
