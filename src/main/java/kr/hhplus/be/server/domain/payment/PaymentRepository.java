package kr.hhplus.be.server.domain.payment;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository {

    void savePayment(Payment payment);

    Optional<Payment> findByPaymentIdAndUserId(long paymentId, long userId);
}
