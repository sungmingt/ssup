package com.ssup.backend.domain.location;

import com.ssup.backend.domain.location.dto.LocationResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class LocationServiceTest {

    @InjectMocks
    private LocationService locationService;

    @Mock
    private LocationRepository locationRepository;

    @DisplayName("시/도 목록 조회 - 성공")
    @Test
    void findSiDoList_success() {
        //given
        Location seoul = Location.builder()
                .id(1L)
                .name("서울특별시")
                .level(1)
                .build();

        Location busan = Location.builder()
                .id(2L)
                .name("부산광역시")
                .level(1)
                .build();

        given(locationRepository.findByLevel(1))
                .willReturn(List.of(seoul, busan));

        //when
        List<LocationResponse> result = locationService.getSiDoList();

        //then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("name")
                .containsExactlyInAnyOrder("서울특별시", "부산광역시");
    }

    @DisplayName("특정 시/도의 군/구 목록 조회 성공")
    @Test
    void findSiGunGuList_success() {
        //given
        Location jongno = Location.builder()
                .id(10L)
                .name("종로구")
                .level(2)
                .build();

        Location gangnam = Location.builder()
                .id(11L)
                .name("강남구")
                .level(2)
                .build();

        given(locationRepository.findByParentId(1L))
                .willReturn(List.of(jongno, gangnam));

        //when
        List<LocationResponse> result = locationService.getSiGunGuList(1L);

        //then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting("level")
                .containsOnly(2);
    }

    @DisplayName("군/구 없는 시/도 조회 - empty list 반환")
    @Test
    void findSiGunGuList_empty() {
        //given
        given(locationRepository.findByParentId(99L))
                .willReturn(List.of());

        //when
        List<LocationResponse> result = locationService.getSiGunGuList(99L);

        //then
        assertThat(result).isEmpty();
    }
}
