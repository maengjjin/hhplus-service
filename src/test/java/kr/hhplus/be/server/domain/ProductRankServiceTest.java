package kr.hhplus.be.server.domain;

import static kr.hhplus.be.server.common.RedisKeyFactory.getProductRankKey;
import static kr.hhplus.be.server.common.RedisKeyFactory.getTopProductRankKey;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.IntStream;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry.ProductSales;
import kr.hhplus.be.server.domain.productRank.ProductRankRepository;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.domain.productRank.RankingMergeStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductRankServiceTest {

    @InjectMocks
    ProductRankService productRankService;

    @Mock
    ProductRankRepository productRankRepository;



    @Test
    void 지정된_날짜로_키를_생성해서_Redis에서_상품별판매량데이터_조회_성공() {
        // given 특정 날짜 세팅 및 날짜 키로 저장된 상품별 판매량 데이터 생성
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.minusDays(1);

        List<ProductSales> productSalesList = List.of(
            new ProductSales(1, 10L),
            new ProductSales(2, 5L),
            new ProductSales(3, 6L)
        );

        String redisKey = getProductRankKey(targetDate);
        when(productRankRepository.findDailyProductSalesData(redisKey)).thenReturn(productSalesList);


        // when: 해당 날짜의 상품별 판매량 데이터를 조회할 때
        List<ProductRankEntry.ProductSales> result = productRankService.findDailyProductSalesData(targetDate);

        // then  올바른 키로 데이터를 조회하고 정확한 판매량 정보가 반환되어야 함
        verify(productRankRepository, times(1)).findDailyProductSalesData(redisKey);
        assertThat(result).hasSize(3);
        assertThat(result).isEqualTo(productSalesList);

        assertThat(result.get(0).getProductId()).isEqualTo(1);
        assertThat(result.get(0).getOrderQty()).isEqualTo(10L);
        assertThat(result.get(1).getProductId()).isEqualTo(2);
        assertThat(result.get(1).getOrderQty()).isEqualTo(5L);
        assertThat(result.get(2).getProductId()).isEqualTo(3);
        assertThat(result.get(2).getOrderQty()).isEqualTo(6L);

    }

    @Test
    void 존재하는_키가_없을_때_통합랭킹_처리중단_확인() {
        // given: 3일치 날짜 설정 및 존재하는 키가 없는 상황
        LocalDate targetDate = LocalDate.now();
        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(targetDate.minusDays(i)))
            .toList();


        when(productRankRepository.existingKeys(dateKeys)).thenReturn(List.of());

        // when 통합 랭킹 생성 메서드 호출
        productRankService.cacheTopProductRankingByDate(targetDate);

        // then 키가 없으므로 saveTopRanking 메서드가 호출되지 않아야 함
        verify(productRankRepository, times(1)).existingKeys(dateKeys);
        verify(productRankRepository, times(0)).saveTopRanking(any(), any());
    }

    @Test
    void 하나의_키만_존재할_때_단일복사방식으로_통합랭킹_생성_성공() {
        // given: 3일치 날짜 설정 및 하나의 키만 존재하는 상황
        LocalDate targetDate = LocalDate.now();

        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(targetDate.minusDays(i)))
            .toList();

        List<String> existingKeys = List.of(dateKeys.get(0));
        when(productRankRepository.existingKeys(dateKeys)).thenReturn(existingKeys);

        // when: 통합 랭킹 생성 메서드 호출
        productRankService.cacheTopProductRankingByDate(targetDate);

        // then: COPY 전략 검증
        verify(productRankRepository, times(1)).existingKeys(dateKeys);

        ArgumentCaptor<RankingMergeStrategy> strategyCaptor = ArgumentCaptor.forClass(RankingMergeStrategy.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRankRepository, times(1)).saveTopRanking(strategyCaptor.capture(), keyCaptor.capture());

        RankingMergeStrategy capturedStrategy = strategyCaptor.getValue();
        assertThat(capturedStrategy).isNotNull();

        String capturedKey = keyCaptor.getValue();
        assertThat(capturedKey).isEqualTo(getTopProductRankKey(targetDate));
    }

    @Test
    void 두개의_키가_존재할_때_두키병합방식으로_통합랭킹_생성_성공() {
        // given: 3일치 날짜 설정 및 두 개의 키가 존재하는 상황
        LocalDate targetDate = LocalDate.now();

        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(targetDate.minusDays(i)))
            .toList();

        List<String> existingKeys = List.of(dateKeys.get(0), dateKeys.get(1));
        when(productRankRepository.existingKeys(dateKeys)).thenReturn(existingKeys);

        // when: 통합 랭킹 생성 메서드 호출
        productRankService.cacheTopProductRankingByDate(targetDate);

        // then: UNION_TWO 전략 선들
        verify(productRankRepository, times(1)).existingKeys(dateKeys);


        ArgumentCaptor<RankingMergeStrategy> strategyCaptor = ArgumentCaptor.forClass(RankingMergeStrategy.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRankRepository, times(1)).saveTopRanking(strategyCaptor.capture(), keyCaptor.capture());

        RankingMergeStrategy capturedStrategy = strategyCaptor.getValue();
        assertThat(capturedStrategy).isNotNull();

        String capturedKey = keyCaptor.getValue();
        assertThat(capturedKey).isEqualTo(getTopProductRankKey(targetDate));
    }

    @Test
    void 세개의_키가_존재할_때_다중키병합방식으로_통합랭킹_생성_성공() {
        // given: 3일치 날짜 설정 및 세 개의 키가 존재하는 상황
        LocalDate targetDate = LocalDate.now();

        List<String> dateKeys = IntStream.range(0, 3)
            .mapToObj(i -> getProductRankKey(targetDate.minusDays(i)))
            .toList();

        List<String> existingKeys = List.of(dateKeys.get(0), dateKeys.get(1), dateKeys.get(2));
        when(productRankRepository.existingKeys(dateKeys)).thenReturn(existingKeys);

        // when: 통합 랭킹 생성 메서드 호출
        productRankService.cacheTopProductRankingByDate(targetDate);

        // then: UNION_MULTIPLE 전략 검증
        verify(productRankRepository, times(1)).existingKeys(dateKeys);

        ArgumentCaptor<RankingMergeStrategy> strategyCaptor = ArgumentCaptor.forClass(RankingMergeStrategy.class);
        ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);

        verify(productRankRepository, times(1)).saveTopRanking(strategyCaptor.capture(), keyCaptor.capture());

        RankingMergeStrategy capturedStrategy = strategyCaptor.getValue();
        assertThat(capturedStrategy).isNotNull();

        String capturedKey = keyCaptor.getValue();
        assertThat(capturedKey).isEqualTo(getTopProductRankKey(targetDate));
    }


}
