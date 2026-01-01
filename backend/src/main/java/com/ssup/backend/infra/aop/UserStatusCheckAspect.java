package com.ssup.backend.infra.aop;

import com.ssup.backend.domain.user.User;
import com.ssup.backend.domain.user.UserRepository;
import com.ssup.backend.domain.user.UserStatus;
import com.ssup.backend.global.exception.SsupException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import static com.ssup.backend.global.exception.ErrorCode.*;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class UserStatusCheckAspect {

    private final UserRepository userRepository;

    @Around("@annotation(checkUserStatus)")
    public Object validateUserStatus(ProceedingJoinPoint joinPoint,
                                     CheckUserStatus checkUserStatus) throws Throwable {
        Object[] args = joinPoint.getArgs();
        int index = checkUserStatus.userIdParamIndex();

        if (args.length <= index || !(args[index] instanceof Long userId)) {
            throw new SsupException(LOGIN_REQUIRED);
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new SsupException(USER_NOT_FOUND));
        UserStatus current = user.getStatus();

        boolean allowed = Arrays.stream(checkUserStatus.value())
                .anyMatch(s -> s == current);

        if (!allowed) throw new SsupException(USER_STATUS_PENDING);
        return joinPoint.proceed();
    }
}
