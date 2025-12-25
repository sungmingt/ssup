package com.ssup.backend.global.config;

import com.ssup.backend.domain.user.UserStatus;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HibernateFilterConfig {

    private final EntityManager entityManager;

    @PostConstruct
    public void enableFilter() {
        Session session = entityManager.unwrap(Session.class);
        //comment
        session.enableFilter("activeCommentFilter")
                .setParameter("isDeleted", false);
        //user
        session.enableFilter("activeUserFilter")
                .setParameter("status", UserStatus.ACTIVE.name());
    }
}
