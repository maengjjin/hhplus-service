package kr.hhplus.be.server.domain.external;

public interface ExternalPlatformClient {


    void sendOrderInfo(long orderId, long userId);

}
