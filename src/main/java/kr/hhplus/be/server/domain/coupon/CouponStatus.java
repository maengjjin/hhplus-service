package kr.hhplus.be.server.domain.coupon;

public enum CouponStatus {

    NOT_USED("N"),
    USED("Y");

    private final String code;

    public String getCode() {
        return code;
    }

    CouponStatus(String code) {
        this.code = code;
    }


    public static CouponStatus fromCode(String code) {
        for (CouponStatus status : values()) {
            if (status.code.equals(code)) return status;
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
