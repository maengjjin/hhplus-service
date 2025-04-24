package kr.hhplus.be.server.infrastructure.coupon;


import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
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
    UserCoupon findWithCouponByUserIdAndCouponId(@Param("userId") Long userId, @Param("couponId") Long couponId);




}
