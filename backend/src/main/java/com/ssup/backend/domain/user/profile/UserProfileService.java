package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestRepository;
import com.ssup.backend.domain.interest.UserInterest;
import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.domain.user.interest.UserInterestRepository;
import com.ssup.backend.domain.user.profile.dto.*;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.aop.CheckUserStatus;
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
    private final UserInterestRepository userInterestRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse findUserProfile(Long userId) {
        User user = userRepository.findUserProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserProfileResponse.of(user);
    }

    @CheckUserStatus(UserStatus.ACTIVE)
    @Transactional(readOnly = true)
    public UserMeProfileResponse findMyProfile(Long userId) {
        User user = userRepository.findMeProfileById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserMeProfileResponse.of(user);
    }

    public UserMeProfileResponse createMyProfile(Long userId, MultipartFile image, UserMeProfileCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        List<Long> interestIds = request.getInterests().stream()
                .map(UserInterestRequestItem::getInterestId)
                .toList();

        if (!interestIds.isEmpty()) {
            List<Interest> interests = interestRepository.findAllById(interestIds);
            if (interests.size() != interestIds.size()) {
                throw new SsupException(INTEREST_NOT_FOUND);
            }

            interests.forEach(i ->
                    user.getInterests().add(new UserInterest(user, i))
            );
        }

        updateMyProfileImage(image, false, user);
        updateMyLocation(request.getLocation(), user);

        user.initProfile(request.getAge(), request.getGender(),
                request.getIntro(), request.getContact()
        );
        user.activate();

        return UserMeProfileResponse.of(user);
    }

    @CheckUserStatus(UserStatus.ACTIVE)
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

        user.getInterests().clear();  // orphanRemoval 이 DB delete 처리

        //Hibernate가 기존 row의 delete를 반영해야함. -> Unique index error 방지
        userInterestRepository.flush();

        if (!interestIds.isEmpty()) {
            List<Interest> interests = interestRepository.findAllById(interestIds);
            if (interests.size() != interestIds.size()) {
                throw new SsupException(INTEREST_NOT_FOUND);
            }

            interests.forEach(i ->
                    user.getInterests().add(new UserInterest(user, i))
            );
        }
    }
}
