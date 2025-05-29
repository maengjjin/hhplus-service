package kr.hhplus.be.server;

import kr.hhplus.be.server.application.event.OrderEventPublisher;
import kr.hhplus.be.server.infrastructure.event.OrderSpringEventPublisher;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public OrderEventPublisher orderEventPublisher(
        ApplicationEventPublisher applicationEventPublisher) {
        return new OrderSpringEventPublisher(applicationEventPublisher);
    }

}
