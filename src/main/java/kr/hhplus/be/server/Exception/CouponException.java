package kr.hhplus.be.server.Exception;

public class CouponException {

    public static class CouponNotFoundException extends RuntimeException {

        public CouponNotFoundException() {
            super("쿠폰이 존재하지 않습니다");
        }
    }

    public static class CouponMinimumAmountNotMetException extends RuntimeException {

        public CouponMinimumAmountNotMetException(long minAmount, long actualAmount) {
            super("최소 사용 금액(" + minAmount + "원) 이상이어야 쿠폰을 사용할 수 있습니다. 현재 금액: " + actualAmount + "원");
        }
    }

    public static class CouponExpiredException extends RuntimeException {

        public CouponExpiredException() {
            super("유효기간이 만료 되었습니다");
        }
    }

    public static class CouponAlreadyUsedException extends RuntimeException {

        public CouponAlreadyUsedException() {
            super("쿠폰이 이미 사용 되었습니다");
        }
    }



    public static class AlreadyIssuedCouponException extends RuntimeException {

        public AlreadyIssuedCouponException() {
            super("이미 발급 된 쿠폰 입니다");
        }
    }

    public static class CouponOutOfStockException  extends RuntimeException {

        public CouponOutOfStockException() {
            super("쿠폰 재고가 없습니다.");
        }
    }
}
