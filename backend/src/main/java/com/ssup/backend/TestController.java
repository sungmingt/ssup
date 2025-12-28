package com.ssup.backend;

import com.ssup.backend.domain.auth.AppUser;
import com.ssup.backend.domain.auth.CurrentUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/users/me")
    public String me(@CurrentUser AppUser user) {
        return "this is authenticated user! userId: " + user.getId();
    }
}
