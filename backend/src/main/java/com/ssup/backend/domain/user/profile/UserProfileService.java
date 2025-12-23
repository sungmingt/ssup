package com.ssup.backend.domain.user.profile;

import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.profile.dto.UserLocationUpdateRequest;
import com.ssup.backend.domain.user.profile.dto.UserMeProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileResponse;
import com.ssup.backend.domain.user.profile.dto.UserProfileUpdateRequest;
import com.ssup.backend.global.exception.ErrorCode;
import com.ssup.backend.global.exception.SsupException;
import com.ssup.backend.infra.s3.ImageStorage;
import com.ssup.backend.infra.s3.ImageType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import static com.ssup.backend.global.exception.ErrorCode.INVALID_LOCATION_LEVEL;
import static com.ssup.backend.global.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class UserProfileService {

    private final UserRepository userRepository;
    private final ImageStorage imageStorage;
    private final LocationRepository locationRepository;

    @Transactional(readOnly = true)
    public UserProfileResponse findUserProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserProfileResponse.of(user);
    }

    @Transactional(readOnly = true)
    public UserMeProfileResponse findMyProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        return UserMeProfileResponse.of(user);
    }

    public UserMeProfileResponse updateMyProfile(Long userId, MultipartFile image, UserProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        updateMyLocation(request, user);
        updateMyProfileImage(image, request.isRemoveImage(), user);
        user.updateProfile(request.getNickname(), request.getIntro(),
                request.getAge(), request.getGender(), request.getContact());

        return UserMeProfileResponse.of(user);
    }

    //=== update ===

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

    private void updateMyLocation(UserProfileUpdateRequest request, User user) {
        UserLocationUpdateRequest userLocationUpdateRequest = request.getUserLocationUpdateRequest();

        if (userLocationUpdateRequest.getSiGunGuId() != null) {
            Location siGunGu = locationRepository.findById(userLocationUpdateRequest.getSiGunGuId())
                    .orElseThrow(() -> new SsupException(ErrorCode.LOCATION_NOT_FOUND));

            if (siGunGu.getLevel() != 2) {
                throw new SsupException(INVALID_LOCATION_LEVEL);
            }

            user.updateLocation(siGunGu);
        }
    }
}
