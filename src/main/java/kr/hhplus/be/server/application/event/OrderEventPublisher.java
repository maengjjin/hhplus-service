package kr.hhplus.be.server.application.event;

import kr.hhplus.be.server.domain.productRank.ProductRankCommand;

public interface OrderEventPublisher {

    // 주문 플랫폼 보내기
    void sendOrderToExternalPlatform(long orderId, long userId);

    void publishProductSalesStatistics(ProductRankCommand command);

}
