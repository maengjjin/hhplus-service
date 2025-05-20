package kr.hhplus.be.server.domain.productRank;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import kr.hhplus.be.server.domain.product.Product;

public interface ProductRankRepository {

    // 주문 시 상품 누적 판매량 증가
    void incrementDailyProductSales(String redisKey, ProductRankEntry entry);

    // 랭킹 데이터 저장 (풀 리셋)
    void saveTopRanking(RankingMergeStrategy strategy, String redisKey);

    //통합 랭킹에 포함된 상품들의 상세 정보 저장
    void saveTopRankingDetail(LocalDate date, Map<Long, Product> details);

    List<ProductRankEntry.ProductSales> findDailyProductSalesData(String redisKey);

    // 존재하는 키만 필터링
    List<String> existingKeys(List<String> dateKeys);
}
