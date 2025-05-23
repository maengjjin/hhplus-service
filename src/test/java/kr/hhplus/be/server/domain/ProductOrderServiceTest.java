package kr.hhplus.be.server.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankEntry.ProductSales;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent.ProductRankItemEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.domain.statistics.ProductOrderCommand;
import kr.hhplus.be.server.domain.statistics.ProductOrderRepository;
import kr.hhplus.be.server.domain.statistics.ProductOrderService;
import kr.hhplus.be.server.domain.statistics.ProductOrderStats;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductOrderServiceTest {

    @Mock
    ProductOrderRepository productOrderRepository;

    @Mock
    ProductRepository productRepository;


    @Mock
    ProductRankService productRankService;

    @InjectMocks
    ProductOrderService productOrderService;

    List<ProductOrderStats> list;

    LocalDate date;
    LocalDateTime end;
    LocalDateTime start;

    @BeforeEach
    void beforeEach(){
        // top5 생성
        date = LocalDate.now();
        start = date.minusDays(1).atStartOfDay();
        end = date.atStartOfDay();

        list = List.of(
            new ProductOrderStats(1L, "나이키운동화", 1L, date),
            new ProductOrderStats(2L, "퓨마운동화", 2L, date),
            new ProductOrderStats(3L, "아디다스운동화", 3L, date),
            new ProductOrderStats(4L, "샌들", 4L, date),
            new ProductOrderStats(5L, "슬리퍼", 5L, date)
        );

    }




    @Test
    void 주문_결제_완료_시_rank_zsort_에_적재_성공() {

        // given: 주문 시 적재할 상품별 판매량 데이터 생성
        LocalDate date = LocalDate.now();

        List<ProductRankCommand.OrderStats> orderStats = List.of(
            new ProductRankCommand.OrderStats(1L, 10),
            new ProductRankCommand.OrderStats(2L, 5),
            new ProductRankCommand.OrderStats(3L, 15)
        );

        ProductRankCommand command = new ProductRankCommand(orderStats, date);

        List<ProductRankItemEvent>  events = ProductRankEvent.form(command);


        // when: 실행
        productRankService.incrementDailyProductSales(new ProductRankEvent(events, command.getStatDate()));

        // then: 파라미터, 상품 판매량 검증
        ArgumentCaptor<ProductRankEvent> commandCaptor = ArgumentCaptor.forClass(ProductRankEvent.class);
        verify(productRankService, times(1)).incrementDailyProductSales(commandCaptor.capture());

        ProductRankEvent event = commandCaptor.getValue();
        assertThat(event).isNotNull();
        assertThat(event.getDate()).isEqualTo(date);

        List<ProductRankEvent.ProductRankItemEvent> capturedStats = event.getItems();
        assertThat(capturedStats).hasSize(3);

        assertThat(capturedStats.get(0).getProductId()).isEqualTo(1L);
        assertThat(capturedStats.get(0).getOrderQty()).isEqualTo(10L);

        assertThat(capturedStats.get(1).getProductId()).isEqualTo(2L);
        assertThat(capturedStats.get(1).getOrderQty()).isEqualTo(5L);

        assertThat(capturedStats.get(2).getProductId()).isEqualTo(3L);
        assertThat(capturedStats.get(2).getOrderQty()).isEqualTo(15L);
    }




    @Test
    void 상품판매량통계데이터_DB저장_성공() {

        // given 특정 날짜 상품별 판매량 데이터 생성 및 상품 정보 생성
        LocalDate today = LocalDate.now();
        LocalDate targetDate = today.minusDays(1);

        List<ProductSales> productSalesList = List.of(
            new ProductSales(1, 10L),
            new ProductSales(2, 5L),
            new ProductSales(3, 6L)
        );

        List<Long> productIds = List.of(1L, 2L, 3L);

        List<Product> products = List.of(
            new Product(1L, "상품1", 10000L, ProductStatus.ACTIVE),
            new Product(2L, "상품2", 20000L, ProductStatus.ACTIVE),
            new Product(3L, "상품3", 30000L, ProductStatus.ACTIVE)
        );


        ProductOrderCommand command = ProductOrderCommand.toCommand(productSalesList, targetDate);

        when(productRepository.findByIdIn(anyList())).thenReturn(products);

        // when: 특정 날짜 상품별 판매량 데이터 저장
        productOrderService.createAggregateTopOrders(command, targetDate);

        // then: 데이터가 저장 검증
        ArgumentCaptor<List<ProductOrderStats>> captor = ArgumentCaptor.forClass(List.class);
        verify(productOrderRepository, times(1)).saveAll(captor.capture());


        List<ProductOrderStats> savedStats = captor.getValue();
        assertThat(savedStats).hasSize(3);

        assertThat(savedStats.get(0).getProductId()).isEqualTo(1L);
        assertThat(savedStats.get(0).getOrderQty()).isEqualTo(10L);
        assertThat(savedStats.get(0).getProductName()).isEqualTo("상품1");

        assertThat(savedStats.get(1).getProductId()).isEqualTo(2L);
        assertThat(savedStats.get(1).getOrderQty()).isEqualTo(5L);
        assertThat(savedStats.get(1).getProductName()).isEqualTo("상품2");

        assertThat(savedStats.get(2).getProductId()).isEqualTo(3L);
        assertThat(savedStats.get(2).getOrderQty()).isEqualTo(6L);
        assertThat(savedStats.get(2).getProductName()).isEqualTo("상품3");


    }



}
