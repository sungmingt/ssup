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
    select u from User u
    left join fetch u.interests ui
    left join fetch ui.interest i
    left join fetch i.category
    where u.id = :id
    """)
    Optional<User> findMeProfileById(@Param("userId") Long id);

    //N+1 제거 (상대방 프로필)
    @Query("""
    select u from User u
    left join fetch u.interests ui
    left join fetch ui.interest i
    where u.id = :id
    """)
    Optional<User> findUserProfileById(@Param("userId") Long id);

    //N+1 제거
    @Query("""
    select u from User u
    join fetch u.languages ul
    join fetch ul.language
    where u.id = :userId
    """)
    Optional<User> findWithLanguages(@Param("userId") Long userId);
}
