package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.interest.UserInterest;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.profile.dto.*;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.ssup.backend.global.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final ImageStorage imageStorage;
    private final LocationRepository locationRepository;
    private final InterestRepository interestRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse findUserProfile(Long userId) {
        User user = userRepository.findUserProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserProfileResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserMeProfileResponse findMyProfile(Long userId) {
        User user = userRepository.findMeProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserMeProfileResponse.of(user);
    }

    //추가정보 기입 + userStatus: ACTIVE
    public UserMeProfileResponse createMyProfile(Long userId, UserMeProfileCreateRequest request) {
        User user = userRepository.findMeProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        Location location = locationRepository.findById(request.getLocation().getSiGunGuId())
                .orElseThrow(() -> new SsupException(LOCATION_NOT_FOUND));

        user.initProfile(request.getImageUrl(), request.getAge(), request.getGender(),
                request.getIntro(), request.getContact(), location
        );

        return UserMeProfileResponse.of(user);
    }

    public UserMeProfileResponse updateMyProfile(Long userId, MultipartFile image, UserProfileUpdateRequest request) {
        User user = userRepository.findMeProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        updateMyLocation(request.getUserLocationUpdateRequest(), user);
        updateMyInterests(request.getInterestIds(), user);
        updateMyProfileImage(image, request.isRemoveImage(), user);
        user.updateProfile(request.getNickname(), request.getIntro(),
                request.getAge(), request.getGender(), request.getContact());

        return UserMeProfileResponse.of(user);
    }

    public void deleteMyAccount(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        if (user.getImageUrl() != null) {
            imageStorage.deleteByUrl(user.getImageUrl());
            user.updateImageUrl(null);
        }

        user.delete();
    }

    //===== business methods =====

    private void updateMyProfileImage(MultipartFile image, boolean removeImage, User user) {
        String prevImageUrl = user.getImageUrl();

        //새로운 이미지가 있다면 -> 교체
        if (image != null && !image.isEmpty()) {
            String newImageUrl = imageStorage.upload(ImageType.PROFILE, image);
            if (prevImageUrl != null) {
                imageStorage.deleteByUrl(prevImageUrl);
            }

            user.updateImageUrl(newImageUrl);
        } else if (removeImage) { //새로운 이미지가 없고, 삭제의 의미라면
            user.updateImageUrl(null);
        }
    }

    private void updateMyLocation(UserLocationUpdateRequest request, User user) {
        if(request == null) return;

        Location siGunGu = locationRepository.findById(request.getSiGunGuId())
                .orElseThrow(() -> new SsupException(ErrorCode.LOCATION_NOT_FOUND));

        if (siGunGu.getLevel() != 2) {
            throw new SsupException(INVALID_LOCATION_LEVEL);
        }

        user.updateLocation(siGunGu);
    }

    private void updateMyInterests(List<Long> interestIds, User user) {
        //삭제: [], 수정없음: null
        if (interestIds == null) return;

        user.getInterests().clear();  //orphanRemoval

        for (Long interestId : interestIds) {
            Interest interest = interestRepository.findById(interestId)
                    .orElseThrow(() -> new SsupException(INTEREST_NOT_FOUND));

            user.addInterest(interest);
        }
    }
}
