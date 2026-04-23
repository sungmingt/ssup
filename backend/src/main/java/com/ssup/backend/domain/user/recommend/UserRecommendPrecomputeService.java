package com.ssup.backend.domain.user.recommend;

import com.ssup.backend.domain.user.recommend.util.SimilarityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserRecommendPrecomputeService {

    //저장된 embedding을 기반으로 추천 친구를 미리 계산해서 저장한다.

    private final UserProfileEmbeddingRepository embeddingRepository;
    private final UserRecommendRepository recommendationRepository;

    //가입/수정한 회원 말고, 다른 모든 회원의 추천 친구도 갱신되어야한다.
    public void precompute(Long userId, int topK) {
        precomputeUserRecommend(userId, topK);
        precomputeOtherUsersRecommend(userId, topK);
    }

    private void precomputeUserRecommend(Long userId, int topK) {
        UserProfileEmbedding me = embeddingRepository
                .findByUserId(userId)
                .orElseThrow();

        List<UserProfileEmbedding> allEmbeddings = embeddingRepository.findAllEmbeddings();

        //todo: 가중치 적용(언어->관심사->지역->자기소개), 언어 적용, 테스트, flow
        List<Long> recommended = allEmbeddings.stream()
                .filter(e -> !e.getUserId().equals(userId))
                .map(e -> new RecommendResult(
                        e.getUserId(),
                        SimilarityUtils.cosineSimilarity(
                                me.getVector(),
                                e.getVector())))
                .sorted((a, b) -> Double.compare(b.score(), a.score()))
                .limit(topK)
                .map(RecommendResult::userId)
                .toList();

        log.info("allEmbeddings size={}", allEmbeddings.size());

        //fallback -> 관련 유저 없을 시 보완
        if (recommended.isEmpty()) {
            recommended = allEmbeddings.stream()
                    .map(UserProfileEmbedding::getUserId)
                    .filter(id -> !id.equals(userId))
                    .limit(topK)
                    .toList();
        }

        recommendationRepository.save(userId, recommended);
    }

    private void precomputeOtherUsersRecommend(Long userId, int topK) {
        List<Long> allUserIds = embeddingRepository.findAllUserIds();

        for (Long id : allUserIds) {
            precomputeUserRecommend(id, topK);
        }
    }

    private record RecommendResult(Long userId, double score) {}
}