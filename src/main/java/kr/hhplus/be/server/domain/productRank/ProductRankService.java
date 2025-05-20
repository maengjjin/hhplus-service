package kr.hhplus.be.server.domain.productRank;


import static kr.hhplus.be.server.config.RedisKeyFactory.getProductRankKey;
import static kr.hhplus.be.server.config.RedisKeyFactory.getTopProductRankKey;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.productRank.RankingMergeStrategy.MergeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductRankService {

    private final ProductRepository productRepository;

    private final ProductRankRepository  productRankRepository;

    // 주문 시 상품 판매량수 증가
    public void incrementDailyProductSales(ProductRankCommand command) {

        // 날짜 키 생성
        String redisKey = getProductRankKey(command.getStatDate());

        ProductRankEntry productEntry = ProductRankEntry.fromCommand(command);

        productRankRepository.incrementDailyProductSales(redisKey, productEntry);

    }

    // 특정 날짜의 상품 랭킹 데이터 조회
    public List<ProductRankEntry.ProductSales> findDailyProductSalesData(LocalDate date) {

        String redisKey = getProductRankKey(date);

        return productRankRepository.findDailyProductSalesData(redisKey);

    }

    // 3일치 데이터 통합 랭킹 생성
    public void cacheTopProductRankingByDate(LocalDate date) {

        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(date.minusDays(i)))
            .toList();

        List<String> existingKeys = productRankRepository.existingKeys(dateKeys);

        // 존재하는 키가 없으면 처리 중단
        if (existingKeys.isEmpty()) {
            return;
        }

        MergeType mergeType;

        // 존재하는 랭킹 키 수에 따라 Redis ZSet 처리 방식 결정
        switch (existingKeys.size()){
            case 1 -> mergeType = MergeType.COPY;
            case 2 -> mergeType = MergeType.UNION_TWO;
            case 3 -> mergeType = MergeType.UNION_MULTIPLE;
            default -> {
                return;
            }
        }

        RankingMergeStrategy strategy = new RankingMergeStrategy(existingKeys, mergeType);

        String redisKey = getTopProductRankKey(date);

        productRankRepository.saveTopRanking(strategy, redisKey);

    }





    public List<Long> extractProductIds(ProductRankCommand command) {
        return  command.getOrderStats().stream()
            .map(ProductRankCommand.OrderStats::getProductId)
            .toList();
    }


    public Map<Long, Product> fetchProductMapById(List<Long> productIds) {
        if (productIds.isEmpty()) {
            return Collections.emptyMap();
        }

        return productRepository.findByIdIn(productIds)
            .stream()
            .collect(Collectors.toMap(Product::getProductId, Function.identity()));
    }



}
