package com.ssup.backend.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatus {

    PENDING, ACTIVE, DELETED
}
