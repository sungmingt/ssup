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

    public void precompute(Long userId, int topK) {

        UserProfileEmbedding me = embeddingRepository
                .findByUserId(userId)
                .orElseThrow();

        List<UserProfileEmbedding> allEmbeddings =
                embeddingRepository.findAllEmbeddings();

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

        recommendationRepository.save(userId, recommended);
    }

    private record RecommendResult(Long userId, double score) {}
}