package kr.hhplus.be.server.domain.payment;

import java.util.List;
import kr.hhplus.be.server.Exception.PointException.InsufficientPointBalanceException;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(User user, long orderId, UserCouponInfo userCoupon, List<OrderCommand.OrderItemDetail> item) {

        Payment payment = Payment.create(user.getUserId(), orderId, userCoupon.getCouponId());
        // 상품금액
        payment.totalProductPrice(item);

        // 쿠폰금액 사용할 수 있는지 확인
        payment.validateCouponCondition(userCoupon);

        payment.CouponCalculator(userCoupon);

        payment.totalAmount();

        if (payment.getPaidPay() > user.getPoint()){
            throw new InsufficientPointBalanceException();
        }

        Point point = new Point(user.getPoint());

        point.use(payment.getPaidPay());

        // 결제 완료
        paymentRepository.savePayment(payment);

        return payment;
    }
}
