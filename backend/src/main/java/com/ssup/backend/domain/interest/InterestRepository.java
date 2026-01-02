package com.ssup.backend.domain.interest;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InterestRepository extends JpaRepository<Interest, Long> {

    //N+1 방지
    List<Interest> findAllById(Iterable<Long> ids);

    boolean existsByCode(String code);
}
