package kr.hhplus.be.server.infrastructure.scheduler;

import java.time.LocalDate;
import kr.hhplus.be.server.application.productRank.ProductRankFacade;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRankScheduler {

    private final ProductRankFacade productRankFacade;

    private final ProductRankService productRankService;

    private final CacheManager cacheManager;

    @Scheduled(cron = "0 30 0 * * *")
    public void generateDailyOrderStatistics() {
        // 1. 전날의 통계 생성
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.minusDays(1); // 어제
        productRankFacade.aggregateAndSaveTopRankedProducts(targetDate);

        // 인기 상품 캐시 삭제
        Cache cache = cacheManager.getCache("TopSellingProduct");


        if (cache != null) {
            cache.clear();
        }

        // 캐시 최신화
        productRankService.getProductRank(today, 5L);


    }
}
