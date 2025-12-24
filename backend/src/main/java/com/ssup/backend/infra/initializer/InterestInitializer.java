package com.ssup.backend.infra.initializer;

import com.ssup.backend.domain.interest.Interest;
import com.ssup.backend.domain.interest.InterestCategory;
import com.ssup.backend.domain.interest.InterestCategoryRepository;
import com.ssup.backend.domain.interest.InterestRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("local")
@RequiredArgsConstructor
public class InterestInitializer implements CommandLineRunner {

    private final InterestCategoryRepository categoryRepository;
    private final InterestRepository interestRepository;

    @Override
    public void run(String... args) {
        initCategories();
        initInterests();
    }

    private void initCategories() {
        saveCategory("MUSIC", "음악");
        saveCategory("SPORTS", "운동");
        saveCategory("STUDY", "학습");
        saveCategory("HOBBY", "취미");
        saveCategory("LIFESTYLE", "일상");
        saveCategory("ENTERTAINMENT", "엔터테인먼트");
    }

    private void initInterests() {
        saveMusic();
        saveSports();
        saveStudy();
        saveHobby();
        saveLifestyle();
        saveEntertainment();
    }

    private void saveCategory(String code, String name) {
        categoryRepository.save(
                InterestCategory.builder()
                        .code(code)
                        .name(name)
                        .build()
        );
    }

    private InterestCategory getCategory(String code) {
        return categoryRepository.findByCode(code)
                .orElseThrow(() -> new IllegalStateException("Category not found: " + code));
    }

    /* ================= MUSIC ================= */

    private void saveMusic() {
        InterestCategory music = getCategory("MUSIC");

        saveInterest("VOCAL", "보컬", music);
        saveInterest("INSTRUMENT", "악기연주", music);
        saveInterest("JAZZ", "재즈", music);
        saveInterest("CLASSICAL", "클래식", music);
        saveInterest("KPOP", "K-POP", music);
        saveInterest("BAND", "밴드", music);
        saveInterest("HIPHOP", "힙합", music);
    }

    /* ================= SPORTS ================= */

    private void saveSports() {
        InterestCategory sports = getCategory("SPORTS");

        saveInterest("FITNESS", "헬스", sports);
        saveInterest("RUNNING", "러닝", sports);
        saveInterest("HIKING", "등산", sports);
        saveInterest("CLIMBING", "클라이밍", sports);
        saveInterest("YOGA", "요가", sports);
        saveInterest("PILATES", "필라테스", sports);
        saveInterest("SOCCER", "축구", sports);
        saveInterest("BASKETBALL", "농구", sports);
        saveInterest("BADMINTON", "배드민턴", sports);
        saveInterest("SWIMMING", "수영", sports);
    }

    /* ================= STUDY ================= */

    private void saveStudy() {
        InterestCategory study = getCategory("STUDY");

        saveInterest("AI", "인공지능", study);
        saveInterest("DESIGN", "디자인", study);
        saveInterest("INVESTING", "재테크", study);
        saveInterest("BOOK", "독서", study);
    }

    /* ================= HOBBY ================= */

    private void saveHobby() {
        InterestCategory hobby = getCategory("HOBBY");

        saveInterest("PHOTOGRAPHY", "사진", hobby);
        saveInterest("VIDEO_EDIT", "영상편집", hobby);
        saveInterest("PAINTING", "그림", hobby);
        saveInterest("COOKING", "요리", hobby);
        saveInterest("BAKING", "베이킹", hobby);
        saveInterest("CAFE", "카페", hobby);
        saveInterest("BOARDGAME", "보드게임", hobby);
        saveInterest("ESCAPE_ROOM", "방탈출", hobby);
    }

    /* ================= LIFESTYLE ================= */

    private void saveLifestyle() {
        InterestCategory lifestyle = getCategory("LIFESTYLE");

        saveInterest("TRAVEL", "여행", lifestyle);
        saveInterest("CAMPING", "캠핑", lifestyle);
        saveInterest("PET", "반려동물", lifestyle);
        saveInterest("PLANT", "식물 키우기", lifestyle);
        saveInterest("VOLUNTEER", "봉사활동", lifestyle);
    }

    /* ================= ENTERTAINMENT ================= */

    private void saveEntertainment() {
        InterestCategory entertainment = getCategory("ENTERTAINMENT");

        saveInterest("MOVIE", "영화", entertainment);
        saveInterest("DRAMA", "드라마", entertainment);
        saveInterest("NETFLIX", "넷플릭스", entertainment);
        saveInterest("ANIMATION", "애니메이션", entertainment);
        saveInterest("YOUTUBE", "유튜브", entertainment);
        saveInterest("GAMING", "게임", entertainment);
    }

    private void saveInterest(String code, String name, InterestCategory category) {
        interestRepository.save(
                Interest.builder()
                        .code(code)
                        .name(name)
                        .category(category)
                        .build()
        );
    }
}
