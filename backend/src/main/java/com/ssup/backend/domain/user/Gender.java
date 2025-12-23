package com.ssup.backend.domain.user;

import java.util.Arrays;
import java.util.Objects;

public enum Gender {

    MALE, FEMALE, UNSELECTED;

    public static Gender of(String input) {
        if (input == null || Objects.equals(input, "")) {
            return UNSELECTED;
        } else {
            return Arrays.stream(values())
                    .filter(gender -> gender.name().equals(input.toUpperCase()))
                    .findFirst()
                    .orElse(UNSELECTED);
        }
    }
}