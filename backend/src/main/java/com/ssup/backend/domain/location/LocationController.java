package com.ssup.backend.domain.location;

import com.ssup.backend.domain.location.dto.LocationResponse;
import com.ssup.backend.domain.post.PostService;
import com.ssup.backend.domain.post.dto.PostListResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/locations")
@Tag(name = "Location", description = "지역정보 API")
public class LocationController {

    private final LocationService locationService;

    @Operation(summary = "지역 조회", description = "시/도 or 군/구 조회")
    @GetMapping
    public List<LocationResponse> getLocation(@RequestParam(required = false, value = "level") Long level,
                                                @RequestParam(required = false, value = "parent_id") Long parentId) {

        if (parentId != null) {
            return locationService.getGuList(parentId);
        }

        return locationService.getSiList();
    }
}
