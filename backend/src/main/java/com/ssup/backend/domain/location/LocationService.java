package com.ssup.backend.domain.location;


import com.ssup.backend.domain.location.dto.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LocationService {


    private final LocationRepository locationRepository;

    // ✅ 시/도 조회
    public List<LocationResponse> getSiList() {
        return locationRepository.findByLevel(1).stream()
                .map(l -> new LocationResponse(
                        l.getId(),
                        l.getName(),
                        l.getLevel()
                ))
                .toList();
    }

    // ✅ 특정 시/도의 군/구 조회
    public List<LocationResponse> getGuList(Long parentId) {
        return locationRepository.findByParentId(parentId).stream()
                .map(l -> new LocationResponse(
                        l.getId(),
                        l.getName(),
                        l.getLevel()
                ))
                .toList();
    }
}
