package kr.hhplus.be.server.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import kr.hhplus.be.server.RedisTestContainersConfig;
import kr.hhplus.be.server.config.RedisKeyFactory;
import kr.hhplus.be.server.config.RedisTemplateConfig;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.domain.statistics.ProductOrderVolume;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.core.ZSetOperations.TypedTuple;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.fasterxml.jackson.databind.ObjectMapper;

@Transactional
@Testcontainers
@SpringBootTest
public class ProductRankServiceRedisTest extends RedisTestContainersConfig {


    @Autowired
    private  RedisTemplateConfig redisTemplate;

    @Autowired
    private  ProductRankService productRankService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    EntityManager em;

    LocalDate date;
    LocalDateTime start;
    LocalDateTime end;

    LocalDate targetDate;
    List<Product> savedProducts;
    String id1, id2, id3, id4, id5;
    List<ProductOrderVolume> productOrderStats;


    @BeforeEach
    void beforeEach(){
        // 메서드 실행 전 캐시 초기화
        redisTemplate.redisConnectionFactory()
            .getConnection()
            .serverCommands()
            .flushAll();

        date = LocalDate.now();
        start = date.minusDays(1).atStartOfDay();
        end = date.atStartOfDay();


        savedProducts = productRepository.saveAll(List.of(
            new Product("스니커즈", 25_000L, ProductStatus.INACTIVE),
            new Product("크록스", 25_000L, ProductStatus.INACTIVE),
            new Product("메리제인", 35_000L, ProductStatus.INACTIVE),
            new Product("워커부츠", 45_000L, ProductStatus.INACTIVE),
            new Product("트레킹화", 60_000L, ProductStatus.INACTIVE)
        ));

        em.flush();
        em.clear();

        id1 = String.valueOf(savedProducts.get(0).getProductId());
        id2 = String.valueOf(savedProducts.get(1).getProductId());
        id3 = String.valueOf(savedProducts.get(2).getProductId());
        id4 = String.valueOf(savedProducts.get(3).getProductId());
        id5 = String.valueOf(savedProducts.get(4).getProductId());


        productOrderStats = List.of(
            new ProductOrderVolume(savedProducts.get(0).getProductId(), 10L),
            new ProductOrderVolume(savedProducts.get(1).getProductId(), 5L),
            new ProductOrderVolume(savedProducts.get(2).getProductId(), 30L), // 1위
            new ProductOrderVolume(savedProducts.get(3).getProductId(), 1L),  // 마지막
            new ProductOrderVolume(savedProducts.get(4).getProductId(), 7L)
        );




    }

    @Test
    void 하루_판매량_기준_상품ID별_ZSet_랭킹_저장_후_정확한_순위와_점수_조회_성공() {

        // given 상품별 판매량 데이터 작성
        LocalDate today = LocalDate.now();
        targetDate = today.minusDays(1); // 어제

        List<ProductOrderVolume> productOrderStats = List.of(
            new ProductOrderVolume(1L, 10L),
            new ProductOrderVolume(2L, 5L),
            new ProductOrderVolume(3L, 30L), // 1위
            new ProductOrderVolume(4L, 1L),  // 마지막
            new ProductOrderVolume(5L, 7L),
            new ProductOrderVolume(6L, 9L),
            new ProductOrderVolume(7L, 15L) // 2위
        );


        ProductRankCommand command = ProductRankCommand.toCommand(productOrderStats, targetDate);


        // when: Zsort 랭킹 저장
        productRankService.cacheProductRankingByDate(command);

        String redisKey = RedisKeyFactory.getProductRankKey(command.getStatDate());

        // then Zsort 역순 검증
        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.redisTemplate().opsForZSet().reverseRangeWithScores(redisKey, 0, -1);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(productOrderStats.size());
        assertThat(result).hasSize(productOrderStats.size());

        // 순서대로 검증
        List<TypedTuple<Object>> rankList = new ArrayList<>(result);
        TypedTuple<Object> first = rankList.get(0);
        TypedTuple<Object> second = rankList.get(1);
        TypedTuple<Object> last = rankList.get(rankList.size() - 1);

        // 1위 상품 검증 (productId: 3, 판매량: 30)
        assertThat(first.getValue()).isEqualTo("3");
        assertThat(first.getScore()).isEqualTo(30.0);

        // 2위 상품 검증 (productId: 7, 판매량: 15)
        assertThat(second.getValue()).isEqualTo("7");
        assertThat(second.getScore()).isEqualTo(15.0);

        // 마지막 순위 상품 검증 (productId: 4, 판매량: 1)
        assertThat(last.getValue()).isEqualTo("4");
        assertThat(last.getScore()).isEqualTo(1.0);


        Long ttl = redisTemplate.redisTemplate().getExpire(redisKey);
        assertThat(ttl).isGreaterThan(3L);

    }


    @Test
    void 삼일간_누적_판매량_기준_통합_상품_랭킹_및_상세정보_Redis에_저장_후_정확한_점수와_순위_검증() {
        // given: 3일치 상품 랭킹 데이터 + 상품 저장
        LocalDate targetDate = LocalDate.of(2025, 5, 14); // 기준일은 14일
        ZSetOperations<String, Object> zSetOps = redisTemplate.redisTemplate().opsForZSet();



        // 실제 생성된 ID로 Redis  1위(id3:58), 2위(id4:44), 3위(id2:32), 4위(id5:35), 5위(id1:26)
        zSetOps.add("product:rank:20250514", id1, 10);
        zSetOps.add("product:rank:20250514", id2, 20);
        zSetOps.add("product:rank:20250514", id3, 30);

        zSetOps.add("product:rank:20250513", id1, 16);
        zSetOps.add("product:rank:20250513", id4, 25);
        zSetOps.add("product:rank:20250513", id5, 35);

        zSetOps.add("product:rank:20250512", id2, 12);
        zSetOps.add("product:rank:20250512", id3, 28);
        zSetOps.add("product:rank:20250512", id4, 19);


        ProductRankCommand command = ProductRankCommand.toCommand(productOrderStats, targetDate);

        // when: 통계 캐싱 실행
        productRankService.cacheTopProductRankingByDate(command);

        // then 검증
        String redisKey = RedisKeyFactory.getTopProductRankKey(targetDate);

        Set<ZSetOperations.TypedTuple<Object>> result = redisTemplate.redisTemplate().opsForZSet().reverseRangeWithScores(redisKey, 0, -1);

        assertThat(result).isNotEmpty();
        assertThat(result).hasSize(5);

        // 순서 확인  id3(58), id4(44), id5(35), id2(32), id1(26)
        List<TypedTuple<Object>> rankList = new ArrayList<>(result);

        assertThat(rankList.get(0).getValue()).isEqualTo(id3);
        assertThat(rankList.get(0).getScore()).isEqualTo(58.0);

        assertThat(rankList.get(1).getScore()).isEqualTo(44.0);
        assertThat(rankList.get(2).getScore()).isEqualTo(35.0);
        assertThat(rankList.get(3).getScore()).isEqualTo(32.0);
        assertThat(rankList.get(4).getScore()).isEqualTo(26.0);

        // TTL 확인
        Long ttl = redisTemplate.redisTemplate().getExpire(redisKey);
        assertThat(ttl).isGreaterThan(3L);

    }

    @Test
    void Hash에_저장된_상품상세정보_json_구조_검증() {
        // given: 3일치 상품 랭킹 데이터 + 상품 저장
        LocalDate targetDate = LocalDate.of(2025, 5, 14); // 기준일은 14일
        ZSetOperations<String, Object> zSetOps = redisTemplate.redisTemplate().opsForZSet();



        // 실제 생성된 ID로 Redis  1위(id3:58), 2위(id4:44), 3위(id2:32), 4위(id5:35), 5위(id1:26)
        zSetOps.add("product:rank:20250514", id1, 10);
        zSetOps.add("product:rank:20250514", id2, 20);
        zSetOps.add("product:rank:20250514", id3, 30);

        zSetOps.add("product:rank:20250513", id1, 16);
        zSetOps.add("product:rank:20250513", id4, 25);
        zSetOps.add("product:rank:20250513", id5, 35);

        zSetOps.add("product:rank:20250512", id2, 12);
        zSetOps.add("product:rank:20250512", id3, 28);
        zSetOps.add("product:rank:20250512", id4, 19);

        ProductRankCommand command = ProductRankCommand.toCommand(productOrderStats, targetDate);

        // when: 통계 캐싱 실행
        productRankService.cacheTopProductRankingByDate(command);

        // then: Hash에 저장된 json 문자열 검증
        String hashKey = RedisKeyFactory.getTopProductDetailKey(targetDate);
        Map<Object, Object> detailMap = redisTemplate.redisTemplate().opsForHash().entries(hashKey);

        // id3 상품의 json 데이터 가져오기
        String jsonData = (String) detailMap.get(id3);
        assertThat(jsonData).isNotNull();

        // json 파싱 테스트
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        // json 구조 검증
        Map<String, Object> parsedData = null;
        try {
            parsedData = objectMapper.readValue(jsonData, Map.class);
            assertThat(parsedData).containsKeys("productId", "productName", "rank", "statDate");
            assertThat(parsedData.get("productId")).isEqualTo(((int) savedProducts.get(2).getProductId()));
            assertThat(parsedData.get("productName")).isEqualTo(savedProducts.get(2).getName());
            assertThat(parsedData.get("rank")).isEqualTo(1);
        } catch (JsonProcessingException e) {
            
        }
        
    }



    @Test
    void 랭킹데이터가_비어있을때_Redis에_빈_결과_저장_성공() {
        // given: 빈 판매량 데이터
        List<ProductOrderVolume> emptyStats = Collections.emptyList();
        LocalDate targetDate = LocalDate.now();
        ProductRankCommand command = ProductRankCommand.toCommand(emptyStats, targetDate);

        // when: 빈 데이터로 캐싱 실행
        productRankService.cacheProductRankingByDate(command);

        // then: Redis에 빈 결과가 저장됨
        String redisKey = RedisKeyFactory.getProductRankKey(targetDate);
        Set<ZSetOperations.TypedTuple<Object>> result =
            redisTemplate.redisTemplate().opsForZSet().reverseRangeWithScores(redisKey, 0, -1);

        assertThat(result).isEmpty();
    }

    @Test
    void 일부_날짜만_데이터가_있을때_통합랭킹_정상처리() {
        // given: 3일 중 하루만 데이터가 있는 상황
        LocalDate targetDate = LocalDate.of(2025, 5, 14);
        ZSetOperations<String, Object> zSetOps = redisTemplate.redisTemplate().opsForZSet();

        // 상품 저장
        List<Product> savedProducts = productRepository.saveAll(List.of(
            new Product("스니커즈", 25_000L, ProductStatus.INACTIVE),
            new Product("크록스", 25_000L, ProductStatus.INACTIVE)
        ));

        em.flush();
        em.clear();

        String id1 = String.valueOf(savedProducts.get(0).getProductId());
        String id2 = String.valueOf(savedProducts.get(1).getProductId());

        // 오늘(14일)만 데이터 있고 나머지 날짜는 데이터 없음
        zSetOps.add("product:rank:20250514", id1, 10);
        zSetOps.add("product:rank:20250514", id2, 20);

        List<ProductOrderVolume> productOrderStats = List.of(
            new ProductOrderVolume(savedProducts.get(0).getProductId(), 10L),
            new ProductOrderVolume(savedProducts.get(1).getProductId(), 20L)
        );

        ProductRankCommand command = ProductRankCommand.toCommand(productOrderStats, targetDate);

        // when: 통계 캐싱 실행
        productRankService.cacheTopProductRankingByDate(command);

        // then: 데이터가 정상적으로 처리됨
        String redisKey = RedisKeyFactory.getTopProductRankKey(targetDate);
        Set<ZSetOperations.TypedTuple<Object>> result =
            redisTemplate.redisTemplate().opsForZSet().reverseRangeWithScores(redisKey, 0, -1);

        assertThat(result).hasSize(2);

        // 점수가 원래 데이터와 동일한지 확인 (다른 날짜 데이터가 없으므로)
        List<TypedTuple<Object>> rankList = new ArrayList<>(result);
        assertThat(rankList.get(0).getValue()).isEqualTo((int) savedProducts.get(1).getProductId());
        assertThat(rankList.get(0).getScore()).isEqualTo(20.0);
    }



}
