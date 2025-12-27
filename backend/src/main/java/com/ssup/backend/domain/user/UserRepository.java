package com.ssup.backend.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Boolean existsByEmail(String email);
    Boolean existsByNickname(String nickname);

    //N+1 제거
    @Query("""
        select distinct u from User u
        left join fetch u.languages ul
        left join fetch ul.language
        left join fetch u.interests ui
        left join fetch ui.interest i
        left join fetch i.category
        left join fetch u.location l
        left join fetch l.parent
        where u.id = :id
        and u.status = 'ACTIVE'
    """)
    Optional<User> findMeProfileById(@Param("id") Long id);

    //N+1 제거 (상대방 프로필)
    @Query("""
        select distinct u from User u
        left join fetch u.languages ul
        left join fetch ul.language
        left join fetch u.interests ui
        left join fetch ui.interest i
        left join fetch i.category
        left join fetch u.location l
        left join fetch l.parent
        where u.id = :id
        and u.status = 'ACTIVE'
    """)
    Optional<User> findUserProfileById(@Param("id") Long id);

    //N+1 제거
    @Query("""
    select u from User u
    join fetch u.languages ul
    join fetch ul.language
    where u.id = :id
    and u.status = 'ACTIVE'
    """)
    Optional<User> findWithLanguages(@Param("id") Long id);
}
