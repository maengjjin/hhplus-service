package kr.hhplus.be.server.interfaces;

import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.List;
import kr.hhplus.be.server.TestcontainersConfiguration;
import kr.hhplus.be.server.common.TopicFactory;
import kr.hhplus.be.server.domain.external.ExternalPlatformService;
import kr.hhplus.be.server.domain.order.OrderEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent;
import kr.hhplus.be.server.domain.productRank.ProductRankEvent.ProductRankItemEvent;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@Import(TestcontainersConfiguration.class)
@Testcontainers
public class KafkaOrderConsumerIntegrationTest extends TestcontainersConfiguration {


    @MockitoBean
    ExternalPlatformService externalPlatformService;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;




//    @SneakyThrows
//    @Test
//    void 리스너_메서드가_서비스를_올바르게_호출하는지_검증() {
//
//        // given: 주문 이벤트 세팅
//        OrderEvent orderEvent = new OrderEvent(10L, 1L);
//
//        Acknowledgment mockAck = mock(Acknowledgment.class);
//
//        // when: 비동기 이벤트 리스너 실행
//        String topic = TopicFactory.getOrderPlatformTopic();
//        ObjectMapper mapper = new ObjectMapper();
//        String json = mapper.writeValueAsString(orderEvent);
//        kafkaTemplate.send(topic, json);
//
//        // then: 비동기 작업 실행 검증
//        verify(externalPlatformService).sendOrderInfoToExternalPlatform(
//            argThat(event ->
//                event.getOrderId() == orderEvent.getOrderId() && event.getUserId() == orderEvent.getUserId()
//            )
//        );
//
//    }


}