package kr.hhplus.be.server.infrastructure.productRank;

import static kr.hhplus.be.server.common.RedisKeyFactory.getTopProductDetailKey;
import static kr.hhplus.be.server.common.RedisKeyFactory.getTopProductRankKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
import kr.hhplus.be.server.domain.productRank.RankingMergeStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisProductRankingRepositoryImpl implements ProductRankRepository {

    private final RedisTemplate<String, Object> redisTemplate;
    private final ObjectMapper objectMapper;
    private static final int DEFAULT_EXPIRY_DAYS = 3;


    @Override
    public void incrementDailyProductSales(String redisKey, ProductRankEntry entry) {

        // 한번에 여러 incrementScore를 실행하기 위해 레디스 파이프라인 사용
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

            // 주문할때 마다 누적하기 위해 증감
            for (ProductRankEntry.ProductSales orderSales : entry.getProductSales()) {
                zSetOps.incrementScore(redisKey, String.valueOf(orderSales.getProductId()), orderSales.getOrderQty());
            }

            // TTL 설정
            redisTemplate.expire(redisKey, Duration.ofDays(DEFAULT_EXPIRY_DAYS));

            return null;
        });

    }

    @Override
    public void saveTopRanking(RankingMergeStrategy strategy, String redisKey) {

        // 기존에 키가 있을 수 있으니 삭제 처리
        redisTemplate.delete(redisKey);

        switch (strategy.mergeType) {
            case COPY -> {

                Set<ZSetOperations.TypedTuple<Object>> values = redisTemplate.opsForZSet().rangeWithScores(strategy.getExistingKeys().get(0), 0, -1);

                // 한번에 여러 add를 실행하기 위해 레디스 파이프라인 사용
                redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
                    for (TypedTuple<Object> tuple : values) {
                        connection.zSetCommands().zAdd(
                            redisKey.getBytes(),
                            tuple.getScore(),
                            tuple.getValue().toString().getBytes()
                        );
                    }
                    return null;
                });

            }
            case UNION_TWO -> {
                // 키가 2개일 경우
                redisTemplate.opsForZSet().unionAndStore(strategy.getExistingKeys().get(0), strategy.getExistingKeys().get(1), redisKey);
            }
            case UNION_MULTIPLE -> {
                // 키가 3개일 경우
                redisTemplate.opsForZSet().unionAndStore(strategy.getExistingKeys().get(0), strategy.getExistingKeys().subList(1, strategy.getExistingKeys().size()), redisKey);
            }
        }

        redisTemplate.expire(redisKey, Duration.ofDays(DEFAULT_EXPIRY_DAYS));

    }


    @Override
    public void saveTopRankingDetail(LocalDate statDate, Map<Long, Product> productMap) {

        String redisKey = getTopProductRankKey(statDate);

        Set<ZSetOperations.TypedTuple<Object>> topProducts = redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, -1);

        // localdate는 변환이 안됨
        objectMapper.registerModule(new JavaTimeModule());

        Map<String, String> productRankMap = new HashMap<>();

        int rank = 1;

        for(ZSetOperations.TypedTuple<Object> tuple : topProducts){

            try {
                long productId = Long.parseLong(tuple.getValue().toString());
                String productName = productMap.get(productId).getName();

                ProductRank productOrderStats = new ProductRank(productId, productName, rank++, statDate);

                productRankMap.put(String.valueOf(productId), objectMapper.writeValueAsString(productOrderStats));
            } catch (JsonProcessingException e) {
                log.info(e.getMessage(), e);
            }

        }

        String hashKey = getTopProductDetailKey(statDate);

        redisTemplate.delete(hashKey);

        redisTemplate.opsForHash().putAll(hashKey, productRankMap);

        redisTemplate.expire(hashKey, Duration.ofDays(DEFAULT_EXPIRY_DAYS));
    }

    @Override
    public List<ProductRankEntry.ProductSales> findDailyProductSalesData(String redisKey) {

        Set<ZSetOperations.TypedTuple<Object>> topItemsWithScores = redisTemplate.opsForZSet().reverseRangeWithScores(redisKey, 0, -1);
        return topItemsWithScores.stream()
            .map(volume -> new ProductRankEntry.ProductSales(
                Long.parseLong(String.valueOf(volume.getValue())),
                volume.getScore().longValue()))
            .collect(Collectors.toList());

    }

    @Override
    public List<String> existingKeys(List<String> dateKeys){
      return dateKeys.stream()
            .filter(key -> Boolean.TRUE.equals(redisTemplate.hasKey(key)))
            .toList();
    }
}
