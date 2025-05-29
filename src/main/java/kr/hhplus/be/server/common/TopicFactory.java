package kr.hhplus.be.server.common;


public class TopicFactory {


    // 외부 플랫폼 토픽 생성
    public static String getOrderPlatformTopic() {
        return "order-platform";
    }


    // 통합 상품 랭킹 키를 생성합니다.
    public static String getTopProductRankKey() {
        return "order-product-rank";
    }




}
