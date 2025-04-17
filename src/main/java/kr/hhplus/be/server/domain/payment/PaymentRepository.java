package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {

    void savePayment(Payment payment);

}
