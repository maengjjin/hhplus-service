package kr.hhplus.be.server.application.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry;
import kr.hhplus.be.server.domain.statistics.ProductOrderService;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
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

        // 해당 날짜 통계 가져오기
        List<ProductRankEntry.ProductSales> orderVolumes = productRankService.findDailyProductSalesData(targetDate);

        if(orderVolumes.isEmpty()) {
            log.info("주문 데이터가 없음");

            return; // 주문 없으면 종료
        }

        List<ProductOrderVolume> productOrderVolumes = orderVolumes.stream()
            .map(volumes -> new ProductOrderVolume(volumes.getProductId(), volumes.getOrderQty()))
            .toList();

        // 통계 저장
        productOrderService.createAggregateTopOrders(productOrderVolumes, targetDate);

        // Redis에 전체 랭킹 캐싱
        productRankService.cacheTopProductRankingByDate(targetDate);

    }

}