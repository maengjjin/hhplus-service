package kr.hhplus.be.server.infrastructure.scheduler;

import java.time.LocalDate;
import kr.hhplus.be.server.application.productRank.ProductRankFacade;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProductRankScheduler {

    private final ProductRankFacade productRankFacade;


    @Scheduled(cron = "0 30 0 * * *")
    public void generateDailyOrderStatistics() {

        try {
            // 1. 전날의 통계 생성
            LocalDate today = LocalDate.now();

            // 어제날짜 생성
            LocalDate targetDate = today.minusDays(1);
            productRankFacade.aggregateYesterdayOrderStatistics(targetDate);

        } catch (Exception e) {
            log.error("일일 주문 통계 작업 중 오류 발생", e);
        }

    }
}
