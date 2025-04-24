package kr.hhplus.be.server.infrastructure.coupon;


import jakarta.persistence.LockModeType;
import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;


public interface CouponJpaRepository extends JpaRepository<Coupon, Long> {


    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.couponId = :couponId")
    Optional<Coupon> findByIdWithLock(long couponId);
}
