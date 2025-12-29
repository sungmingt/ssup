package com.ssup.backend.domain.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class AppUserProvider {

    public Long getUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth.getPrincipal().equals("anonymousUser")) {
            return null;
        }

        AppUser appUser = (AppUser) auth.getPrincipal();
        return appUser.getId();
    }

    public boolean isAuthenticated() {
        return getUserId() != null;
    }
}
