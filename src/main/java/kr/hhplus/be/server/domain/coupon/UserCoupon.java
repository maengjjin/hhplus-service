package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponAlreadyUsedException;
import kr.hhplus.be.server.Exception.CouponException.CouponExpiredException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Getter
@Table(name = "user_coupon")
public class UserCoupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id")
    private long userCouponId;

    private long userId;

    @ManyToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "coupon_yn")
    @Enumerated(EnumType.STRING)
    private CouponStatus couponYn;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;


    public static UserCoupon create(long userId, Coupon coupon) {

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.userId = userId;
        userCoupon.coupon = coupon;
        userCoupon.couponYn = CouponStatus.NOT_USED;
        userCoupon.createAt = LocalDateTime.now();
        userCoupon.updateAt = LocalDateTime.now();

        return  userCoupon;

    }

    public void useCoupon(){
        this.couponYn = CouponStatus.USED;
        this.updateAt = LocalDateTime.now();
    }


    void validateUsable(){
        if(couponYn == CouponStatus.USED){
            throw new CouponAlreadyUsedException();
        }
    }

    void validateNotExpired(){
        if (coupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException();
        }
    }

}
