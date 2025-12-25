package com.ssup.backend.fixture.location;

import com.ssup.backend.domain.location.Location;

public class LocationFixture {

    public static Location createSiDo() {
        return Location.builder()
                .name("서울특별시")
                .level(1)
                .build();
    }

    public static Location createSiGunGu(Location parent) {
        return Location.builder()
                .name("강남구")
                .level(2)
                .parent(parent)
                .build();
    }
}
