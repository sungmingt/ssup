package com.ssup.backend.fixture.location;

import com.ssup.backend.domain.location.Location;
import jakarta.persistence.EntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

public class LocationJpaFixture {

    public static Location createSiDo(EntityManager em) {
        Location siDo = Location.builder()
                .name("서울특별시")
                .level(1)
                .build();
        em.persist(siDo);
        return siDo;
    }

    public static Location createSiGunGu(EntityManager em, Location parent) {
        Location siGunGu = Location.builder()
                .name("강남구")
                .level(2)
                .parent(parent)
                .build();
        em.persist(siGunGu);
        return siGunGu;
    }
}
