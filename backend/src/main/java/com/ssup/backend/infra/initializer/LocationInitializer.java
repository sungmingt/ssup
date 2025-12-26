package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.location.Location;
import com.ssup.backend.domain.location.LocationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Profile("!test")
//@Profile("local")
@Order(3)
public class LocationInitializer implements CommandLineRunner {

    private final LocationRepository locationRepository;

    @Override
    @Transactional
    public void run(String... args) {

        if (locationRepository.count() > 0) return;

        // =================== 1단계 : 시/도 ===================
        Map<Long, Location> siDoMap = new HashMap<>();

        saveSiDo(siDoMap, 1L, "서울특별시");
        saveSiDo(siDoMap, 2L, "부산광역시");
        saveSiDo(siDoMap, 3L, "대구광역시");
        saveSiDo(siDoMap, 4L, "인천광역시");
        saveSiDo(siDoMap, 5L, "광주광역시");
        saveSiDo(siDoMap, 6L, "대전광역시");
        saveSiDo(siDoMap, 7L, "울산광역시");
        saveSiDo(siDoMap, 8L, "경기도");
        saveSiDo(siDoMap, 9L, "강원특별자치도");
        saveSiDo(siDoMap, 10L, "충청북도");
        saveSiDo(siDoMap, 11L, "충청남도");
        saveSiDo(siDoMap, 12L, "전라북도");
        saveSiDo(siDoMap, 13L, "전라남도");
        saveSiDo(siDoMap, 14L, "경상북도");
        saveSiDo(siDoMap, 15L, "경상남도");
        saveSiDo(siDoMap, 16L, "제주특별자치도");
        saveSiDo(siDoMap, 17L, "세종특별자치시");

        // =================== 2단계 : 군/구 ===================
        saveSiGunGu(101L, "강남구", siDoMap.get(1L));
        saveSiGunGu(102L, "강동구", siDoMap.get(1L));
        saveSiGunGu(103L, "강북구", siDoMap.get(1L));
        saveSiGunGu(104L, "강서구", siDoMap.get(1L));
        saveSiGunGu(105L, "관악구", siDoMap.get(1L));
        saveSiGunGu(106L, "광진구", siDoMap.get(1L));
        saveSiGunGu(107L, "구로구", siDoMap.get(1L));
        saveSiGunGu(108L, "금천구", siDoMap.get(1L));
        saveSiGunGu(109L, "노원구", siDoMap.get(1L));
        saveSiGunGu(110L, "도봉구", siDoMap.get(1L));
        saveSiGunGu(111L, "동대문구", siDoMap.get(1L));
        saveSiGunGu(112L, "동작구", siDoMap.get(1L));
        saveSiGunGu(113L, "마포구", siDoMap.get(1L));
        saveSiGunGu(114L, "서대문구", siDoMap.get(1L));
        saveSiGunGu(115L, "서초구", siDoMap.get(1L));
        saveSiGunGu(116L, "성동구", siDoMap.get(1L));
        saveSiGunGu(117L, "성북구", siDoMap.get(1L));
        saveSiGunGu(118L, "송파구", siDoMap.get(1L));
        saveSiGunGu(119L, "양천구", siDoMap.get(1L));
        saveSiGunGu(120L, "영등포구", siDoMap.get(1L));
        saveSiGunGu(121L, "용산구", siDoMap.get(1L));
        saveSiGunGu(122L, "은평구", siDoMap.get(1L));
        saveSiGunGu(123L, "종로구", siDoMap.get(1L));
        saveSiGunGu(124L, "중구", siDoMap.get(1L));
        saveSiGunGu(125L, "중랑구", siDoMap.get(1L));

        saveSiGunGu(201L, "해운대구", siDoMap.get(2L));
        saveSiGunGu(202L, "수영구", siDoMap.get(2L));
        saveSiGunGu(203L, "남구", siDoMap.get(2L));
        saveSiGunGu(204L, "동래구", siDoMap.get(2L));
        saveSiGunGu(205L, "부산진구", siDoMap.get(2L));
        saveSiGunGu(206L, "사하구", siDoMap.get(2L));
        saveSiGunGu(207L, "금정구", siDoMap.get(2L));

        saveSiGunGu(301L, "수성구", siDoMap.get(3L));
        saveSiGunGu(302L, "달서구", siDoMap.get(3L));
        saveSiGunGu(303L, "중구", siDoMap.get(3L));

        saveSiGunGu(401L, "연수구", siDoMap.get(4L));
        saveSiGunGu(402L, "남동구", siDoMap.get(4L));
        saveSiGunGu(403L, "부평구", siDoMap.get(4L));
        saveSiGunGu(404L, "서구", siDoMap.get(4L));
        saveSiGunGu(405L, "미추홀구", siDoMap.get(4L));

        saveSiGunGu(501L, "서구", siDoMap.get(5L));
        saveSiGunGu(502L, "북구", siDoMap.get(5L));

        saveSiGunGu(601L, "유성구", siDoMap.get(6L));
        saveSiGunGu(602L, "서구", siDoMap.get(6L));

        saveSiGunGu(701L, "남구", siDoMap.get(7L));
        saveSiGunGu(702L, "중구", siDoMap.get(7L));

        saveSiGunGu(801L, "성남시", siDoMap.get(8L));
        saveSiGunGu(802L, "수원시", siDoMap.get(8L));
        saveSiGunGu(803L, "용인시", siDoMap.get(8L));
        saveSiGunGu(804L, "고양시", siDoMap.get(8L));
        saveSiGunGu(805L, "부천시", siDoMap.get(8L));
        saveSiGunGu(806L, "안양시", siDoMap.get(8L));
        saveSiGunGu(807L, "하남시", siDoMap.get(8L));
    }

    private void saveSiDo(Map<Long, Location> map, Long id, String name) {
        Location location = Location.builder()
                .id(id)
                .name(name)
                .level(1)
                .build();
        locationRepository.save(location);
        map.put(id, location);
    }

    private void saveSiGunGu(Long id, String name, Location parent) {
        Location location = Location.builder()
                .id(id)
                .name(name)
                .level(2)
                .parent(parent)
                .build();
        locationRepository.save(location);
    }
}
