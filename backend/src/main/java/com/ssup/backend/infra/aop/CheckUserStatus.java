package com.ssup.backend.infra.aop;

import com.ssup.backend.domain.user.UserStatus;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CheckUserStatus {
    //userId를 항상 첫번째 인자로 받자!
    int userIdParamIndex() default 0;
    UserStatus[] value();
}