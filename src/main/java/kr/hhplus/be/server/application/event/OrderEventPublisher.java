package kr.hhplus.be.server.application.event;

public interface OrderEventPublisher {

    // 주문 플랫폼 보내기
    void sendOrderToExternalPlatform(long orderId, long userId);

}
