package com.ssup.backend.domain.user;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserService {

    private final UserRepository userRepository;
//    private final UserLanguageRepository userLanguageRepository;
//    private final LanguageRepository languageRepository;
//    private final UserInterestRepository userInterestRepository;
//    private final LocationRepository locationRepository;
//    private final MatchService matchService;
//

    public User findUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow();
    }

//    public SignUpResponse signUp(SignUpRequest request) {
//        Location location = locationRepository.findById(request.getLocationId())
//                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 지역입니다."));
//
//        User user = User.builder()
//                .nickname(request.getNickname())
//                .email(request.getEmail())
//                .password(request.getPassword()) //todo: encode
//                .age(request.getAge())
//                .gender(request.getGender())
//                .location(location)
//                .contact(request.getContact())
//                .intro(request.getIntro())
//                .build();
//
//        User savedUser = userRepository.save(user);
//
//        //Language 매핑
//        for (UserLanguageRequest languageRequest : request.getLanguages()) {
//            Language language = languageRepository.findById(languageRequest.getLanguageId())
//                    .orElseThrow();
//
//            UserLanguage userLanguage = new UserLanguage(user, language, languageRequest.getLevel(), languageRequest.getType());
//            userLanguageRepository.save(userLanguage);
//        }
//
//
//        return SignUpResponse.of(savedUser);
//    }

//    public UserProfileResponse getUserProfile(Long currentUserId, Long targetUserId) {
//        User user = userRepository.findById(targetUserId)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        // 언어
//        List<UserLanguageResponse> languages = userLanguageRepository.findByUserId(targetUserId)
//                .stream()
//                .map(lang -> new UserLanguageResponse(
//                        lang.getLanguage().getName(),
//                        lang.getLevel(),
//                        lang.getType()
//                ))
//                .toList();
//
//        // 관심사
//        List<String> interests = userInterestRepository.findByUserId(targetUserId)
//                .stream()
//                .map(ui -> ui.getInterest().getName())
//                .toList();
//
//        // Location 계층
//        Location loc = user.getLocation();
//        LocationResponse location = buildLocationResponse(loc);
//
//        // contact는 매치된 경우만 반환
//        String contact = matchService.isMatched(currentUserId, targetUserId)
//                ? user.getContact()
//                : null;
//
//        return new UserProfileResponse(
//                user.getId(),
//                user.getNickname(),
//                user.getIntro(),
//                user.getImageUrl(),
//                languages,
//                interests,
//                location,
//                contact
//        );
//    }
//
//    private LocationResponse buildLocationResponse(Location loc) {
//        if (loc == null) return null;
//
//        LocationResponse parent = loc.getParent() != null
//                ? buildLocationResponse(loc.getParent())
//                : null;
//
//        return new LocationResponse(loc.getId(), loc.getName(), loc.getLevel(), parent);
//    }

}
