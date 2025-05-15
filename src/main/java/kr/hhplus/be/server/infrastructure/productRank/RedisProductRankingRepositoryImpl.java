package kr.hhplus.be.server.infrastructure.productRank;

import static kr.hhplus.be.server.config.RedisKeyFactory.getProductRankKey;
import static kr.hhplus.be.server.config.RedisKeyFactory.getTopProductDetailKey;
import static kr.hhplus.be.server.config.RedisKeyFactory.getTopProductRankKey;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.IntStream;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.productRank.ProductRank;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
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
    public void saveRanking(ProductRankEntry rankEntry) {

        String redisKey = getProductRankKey(rankEntry.getStatDate());


        // 한번에 여러 add를 실행하기 위해 레디스 파이프라인 사용
        redisTemplate.executePipelined((RedisCallback<Object>) connection -> {
            ZSetOperations<String, Object> zSetOps = redisTemplate.opsForZSet();

            // 기존 데이터 삭제
             redisTemplate.delete(redisKey);

            // 모든 상품 판매 데이터 일괄 추가
            for (ProductRankEntry.ProductSales stat : rankEntry.getProductSales()) {
                zSetOps.add(redisKey, String.valueOf(stat.getProductId()), stat.getTotalQty());
            }

            // TTL 설정
            redisTemplate.expire(redisKey, Duration.ofDays(DEFAULT_EXPIRY_DAYS));

            return null;
        });

    }

    @Override
    public boolean saveTopRanking(LocalDate date) {

        // 3일치 키 생성
        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(date.minusDays(i)))
            .toList();

        // 실제 Redis에 존재하는 키만 필터링
        List<String> existingKeys = dateKeys.stream()
            .filter(key -> Boolean.TRUE.equals(redisTemplate.hasKey(key)))
            .toList();


        // 존재하는 키가 없으면 처리 중단
        if (existingKeys.isEmpty()) {
            return false;
        }

        String redisKey = getTopProductRankKey(date);

        // 기존에 키가 있을 수 있으니 삭제 처리
        redisTemplate.delete(redisKey);

        switch (existingKeys.size()){
            case 1 -> {
                // 키가 하나뿐이면 그대로 복사
                Set<ZSetOperations.TypedTuple<Object>> values = redisTemplate.opsForZSet().rangeWithScores(existingKeys.get(0), 0, -1);

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
            case 2 -> {
                // 키가 2개일 경우
                redisTemplate.opsForZSet().unionAndStore(existingKeys.get(0), existingKeys.get(1), redisKey);
            }
            case 3 -> {
                // 키가 3개일 경우
                redisTemplate.opsForZSet().unionAndStore(existingKeys.get(0), existingKeys.subList(1, existingKeys.size()), redisKey);
            }
        }

        redisTemplate.expire(redisKey, Duration.ofDays(DEFAULT_EXPIRY_DAYS));

        return true;
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
}
