package kr.hhplus.be.server.domain.coupon;

public enum CouponStatus {

    NOT_USED("N"),
    USED("Y");

    private final String code;

    CouponStatus(String code) {
        this.code = code;
    }
}
