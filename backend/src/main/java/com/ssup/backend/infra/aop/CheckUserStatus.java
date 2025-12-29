package com.ssup.backend.infra.aop;

import com.ssup.backend.domain.user.UserStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUserStatus {
    UserStatus[] value();
}