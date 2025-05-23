package kr.hhplus.be.server.interfaces;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import kr.hhplus.be.server.domain.external.ExternalPlatformClient;
import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.interfaces.event.ExternalPlatformEventListener;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
public class ExternalPlatformEventListenerTest {

    @InjectMocks
    private ExternalPlatformEventListener externalPlatformEventListener;

    @Mock
    private ExternalPlatformClient externalPlatformClient;

    private ExternalPlatformService externalPlatformService;



    @BeforeEach
    void setUp() {
        // 수동으로 spy 객체 생성
        externalPlatformService = spy(new ExternalPlatformService(externalPlatformClient));

        // 리스너에 주입
        ReflectionTestUtils.setField(externalPlatformEventListener, "externalPlatformService", externalPlatformService);
    }



    @Test
    void 이벤트_리스너가_주문이벤트를_받아_외부플랫폼클라이언트를_호출하는지_검증(){

        // given: 주문 이벤트 준비
        OrderEvent orderEvent = new OrderEvent(10L, 1L);

        // when: 이벤트 리스너 호출
        externalPlatformEventListener.handleOrderCreatedEvent(orderEvent);

        // then: 외부 플랫폼 클라이언트 호출 확인
        Mockito.verify(externalPlatformClient).sendOrderInfo(orderEvent);


    }

    @Test
    void 이벤트_리스너가_비동기적으로_실행되고_완료되는지_검증() throws InterruptedException {

        // given: 주문 이벤트와 비동기 완료 감지용 래치 준비
        OrderEvent event = new OrderEvent(10L, 1L);
        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(externalPlatformService).sendOrderInfoToExternalPlatform(any(OrderEvent.class));

        // when: 비동기 이벤트 리스너 호출
        externalPlatformEventListener.handleOrderCreatedEvent(event);

        // then: 비동기 작업이 지정된 시간 내에 완료되는지 확인
        boolean asyncCompleted = latch.await(2, TimeUnit.SECONDS);
        assertTrue(asyncCompleted, "비동기 작업이 시간 내에 완료되지 않았습니다");
        verify(externalPlatformService).sendOrderInfoToExternalPlatform(event);

    }




}