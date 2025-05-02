package kr.hhplus.be.server.domain.payment;


import kr.hhplus.be.server.domain.order.OrderPaymentCalculator.PriceSummary;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public Payment createPayment(User user, long orderId, long couponId, PriceSummary priceSummary) {

        Payment payment = Payment.create(user.getUserId(), orderId, couponId, priceSummary);

        // 결제 완료
        paymentRepository.savePayment(payment);

        return payment;
    }
}
