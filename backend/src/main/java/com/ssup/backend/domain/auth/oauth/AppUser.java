package com.ssup.backend.domain.auth.oauth;

import lombok.Getter;

@Getter
public class AppUser {

    Long id;

    public AppUser(Long id) {
        this.id = id;
    }
}
