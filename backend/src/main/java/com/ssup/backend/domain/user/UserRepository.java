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
    join fetch u.languages ul
    join fetch ul.language
    where u.id = :userId
    """)
    Optional<User> findWithLanguages(@Param("userId") Long userId);
}
