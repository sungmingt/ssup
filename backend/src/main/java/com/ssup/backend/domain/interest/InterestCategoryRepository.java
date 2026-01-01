package com.ssup.backend.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InterestCategoryRepository extends JpaRepository<InterestCategory, Long> {
    Optional<InterestCategory> findByCode(String code);
}
