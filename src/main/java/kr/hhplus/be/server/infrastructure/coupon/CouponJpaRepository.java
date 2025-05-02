package kr.hhplus.be.server.infrastructure.coupon;


import jakarta.persistence.LockModeType;
import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.hibernate.PessimisticLockException;
import org.springframework.dao.CannotAcquireLockException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;


public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {

    @Retryable(
        value = {
            CannotAcquireLockException.class,
            PessimisticLockException.class
        },
        maxAttempts = 3,
        backoff = @Backoff(delay = 1000)
    )
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId")
    Optional<Coupon> findByIdWithLock(long couponId);
}
