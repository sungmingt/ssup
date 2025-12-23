package com.ssup.backend.domain.location;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LocationRepository extends JpaRepository<Location, Long> {

    // ✅ 시/도 목록 (level = 1)
    List<Location> findByLevel(Integer level);

    // ✅ 특정 시/도의 군/구 목록
    List<Location> findByParentId(Long parentId);
}