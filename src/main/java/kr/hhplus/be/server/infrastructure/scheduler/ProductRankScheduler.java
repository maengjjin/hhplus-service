package kr.hhplus.be.server.infrastructure.scheduler;

import java.time.LocalDate;
import kr.hhplus.be.server.application.productRank.ProductRankFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductRankScheduler {

    private final ProductRankFacade productRankFacade;

    @Scheduled(cron = "0 30 0 * * *")
    public void generateDailyOrderStatistics() {
        // 1. 전날의 통계 생성
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.minusDays(1); // 어제
        productRankFacade.aggregateAndSaveTopRankedProducts(targetDate);
    }
}
