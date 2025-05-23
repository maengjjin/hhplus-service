package kr.hhplus.be.server.infrastructure.external;

import kr.hhplus.be.server.domain.external.ExternalPlatformClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ExternalPlatformClientImpl implements ExternalPlatformClient {

    @Override
    public void sendOrderInfo(long orderId, long userId) {
        log.info("외부 플랫폼으로 주문 정보 전송");
        log.info("외부 플랫폼 주문 전송 완료");

    }
}
