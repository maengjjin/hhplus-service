package kr.hhplus.be.server.interfaces;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.domain.productRank.ProductRankCommand;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankService;
import kr.hhplus.be.server.interfaces.event.ProductSalesStatisticsListener;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ProductSalesStatisticsListenerTest {


    @InjectMocks
    private ProductSalesStatisticsListener productSalesStatisticsListener;

    @Mock
    private ProductRankService productRankService;



    @Test
    void 이벤트_리스너가_주문이벤트를_받아_통계_서비스를_호출하는지_검증(){

        // given: 주문 이벤트 준비
        List<ProductOrderResult> orderItems = List.of(
            new ProductOrderResult(1L, 100L, 50L, 2L, ProductStatus.ACTIVE, 15000L, "맥북 프로", "16인치"),
            new ProductOrderResult(2L, 101L, 30L, 1L, ProductStatus.ACTIVE, 20000L, "아이패드 프로", "12.9인치"),
            new ProductOrderResult(3L, 102L, 100L, 5L, ProductStatus.ACTIVE, 1000L, "애플펜슬", "2세대")
        );

        LocalDate date = LocalDate.now();

        List<ProductRankEvent.ProductRankItemEvent> expectedEvents = ProductRankEvent.form(ProductRankCommand.toCommand(orderItems, date));
        ProductRankEvent productRankEvent = new ProductRankEvent(expectedEvents, date);


        // when: 이벤트 리스너 호출
        productSalesStatisticsListener.handleOrderCreatedEvent(productRankEvent);

        // then: 통계 서비스 실행
        Mockito.verify(productRankService).incrementDailyProductSales(productRankEvent);


    }

    @Test
    void 이벤트_리스너가_비동기적으로_실행되고_완료되는지_검증() throws InterruptedException {

        // given: 주문 이벤트와 비동기 완료 감지용 래치 준비
        List<ProductOrderResult> orderItems = List.of(
            new ProductOrderResult(1L, 100L, 50L, 2L, ProductStatus.ACTIVE, 15000L, "맥북 프로", "16인치"),
            new ProductOrderResult(2L, 101L, 30L, 1L, ProductStatus.ACTIVE, 20000L, "아이패드 프로", "12.9인치"),
            new ProductOrderResult(3L, 102L, 100L, 5L, ProductStatus.ACTIVE, 1000L, "애플펜슬", "2세대")
        );

        LocalDate date = LocalDate.now();

        List<ProductRankEvent.ProductRankItemEvent> expectedEvents = ProductRankEvent.form(ProductRankCommand.toCommand(orderItems, date));
        ProductRankEvent event = new ProductRankEvent(expectedEvents, date);

        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(productRankService).incrementDailyProductSales(any(ProductRankEvent.class));



        // when: 비동기 이벤트 리스너 호출
        productSalesStatisticsListener.handleOrderCreatedEvent(event);

        // then: 비동기 작업이 지정된 시간 내에 완료되는지 확인
        boolean asyncCompleted = latch.await(2, TimeUnit.SECONDS);
        assertTrue(asyncCompleted, "비동기 작업이 시간 내에 완료되지 않았습니다");
        verify(productRankService).incrementDailyProductSales(event);

    }
}
