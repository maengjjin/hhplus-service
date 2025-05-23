package kr.hhplus.be.server.domain.external;

import kr.hhplus.be.server.domain.order.OrderEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExternalPlatformService {

    private final ExternalPlatformClient externalPlatformClient;

    public void sendOrderInfoToExternalPlatform(OrderEvent orderEvent) {

        externalPlatformClient.sendOrderInfo(orderEvent);


    }
}
