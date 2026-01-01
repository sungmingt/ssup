package com.ssup.backend.domain.user.interest;

import com.ssup.backend.domain.interest.UserInterest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserInterestRepository extends JpaRepository<UserInterest, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("delete from UserInterest ui where ui.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
}
