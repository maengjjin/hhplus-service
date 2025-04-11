package kr.hhplus.be.server.Exception;

public class CouponException {

    public static class CouponNotFoundException extends RuntimeException {
        public CouponNotFoundException() {
            super("쿠폰이 존재하지 않습니다");
        }
    }

}
