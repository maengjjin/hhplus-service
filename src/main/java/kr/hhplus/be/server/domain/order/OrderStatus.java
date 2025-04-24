package kr.hhplus.be.server.domain.order;

public enum OrderStatus {

    ORDERED,            // 주문 완료
    PAID,               // 결제 완료
    PAYMENT_CANCELED,   // 결제 취소
    ORDER_CANCELED      // 주문 취소

}
