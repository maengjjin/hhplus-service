package kr.hhplus.be.server.infrastructure.coupon;


import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;



public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {

    @Query("""
            SELECT uc
            FROM UserCoupon uc
            JOIN FETCH uc.coupon
            WHERE uc.user.userId = :userId
            AND uc.coupon.couponId = :couponId
           """)
    Optional<UserCoupon> findWithCouponByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);


    @Modifying
    @Query(""" 
            UPDATE UserCoupon uc
            SET uc.couponYn = 'Y',
            uc.updateAt = CURRENT_TIMESTAMP
            WHERE uc.user.userId = :userId
            AND uc.coupon.couponId = :couponId
            """)
    void updateCouponUsed(@Param("userId") long userId, @Param("couponId") long couponId);

}
