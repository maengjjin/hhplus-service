package kr.hhplus.be.server.domain.external;

import kr.hhplus.be.server.domain.order.OrderEvent;

public interface ExternalPlatformClient {


    void sendOrderInfo(OrderEvent orderEvent);

}
