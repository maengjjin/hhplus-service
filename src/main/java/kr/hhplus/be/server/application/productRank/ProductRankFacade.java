package kr.hhplus.be.server.application.productRank;

import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.domain.statistics.ProductOrderService;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRankFacade {

    private final ProductRankService productRankService;

    private final ProductOrderService productOrderService;


    @Transactional
    public void aggregateYesterdayOrderStatistics(LocalDate targetDate){

        // 해당 날짜 통계 생성
        List<ProductOrderVolume> orderVolumes = productOrderService.findAggregateTopOrders(targetDate);

        if(orderVolumes.isEmpty()) {
            log.info("주문 데이터가 없음");

            return; // 주문 없으면 종료
        }

        // 통계 저장
        productOrderService.createAggregateTopOrders(orderVolumes, targetDate);

        ProductRankCommand command = ProductRankCommand.toCommand(orderVolumes, targetDate);

        // Redis에 랭킹 정보 캐싱
        productRankService.cacheProductRankingByDate(command);

        // Redis에 전체 랭킹 캐싱
        productRankService.cacheTopProductRankingByDate(command);

    }

}