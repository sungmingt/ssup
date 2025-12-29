package com.ssup.backend.infra.aop;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static com.ssup.backend.global.exception.ErrorCode.*;

@Aspect
@Component
@RequiredArgsConstructor
public class UserStatusCheckAspect {

    private final UserRepository userRepository;

    @Around("@annotation(checkUserStatus)")
    public Object validateUserStatus(ProceedingJoinPoint joinPoint,
                                     CheckUserStatus checkUserStatus) throws Throwable {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new SsupException(LOGIN_REQUIRED);
        }

        AppUser appUser = (AppUser) authentication.getPrincipal();
        User user = userRepository.findById(appUser.getId())
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));

        UserStatus currentStatus = user.getStatus();

        boolean allowed = false;
        for (UserStatus allowedStatus : checkUserStatus.value()) {
            if (allowedStatus == currentStatus) {
                allowed = true;
                break;
            }
        }

        if (!allowed) {
            throw new SsupException(USER_STATUS_PENDING);
        }

        return joinPoint.proceed();
    }
}
