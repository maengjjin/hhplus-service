package kr.hhplus.be.server.domain.coupon;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import kr.hhplus.be.server.Exception.CouponException.CouponAlreadyUsedException;
import kr.hhplus.be.server.Exception.CouponException.CouponExpiredException;
import kr.hhplus.be.server.domain.user.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

    @Column(name = "coupon_yn")
    private String couponYn;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    public UserCoupon(User user, Coupon coupon, String couponYn) {
        this.user = user;
        this.coupon = coupon;
        this.couponYn = couponYn;
    }

    public static UserCoupon create(User user, Coupon coupon) {

        UserCoupon userCoupon = new UserCoupon();
        userCoupon.user = user;
        userCoupon.coupon = coupon;
        userCoupon.couponYn = "N";
        userCoupon.createAt = LocalDateTime.now();
        userCoupon.updateAt = LocalDateTime.now();

        return  userCoupon;

    }

    public void useCoupon(){
        this.couponYn = "Y";
        this.updateAt = LocalDateTime.now();
    }


    void validateUsable(){
        if("Y".equals(couponYn)){
            throw new CouponAlreadyUsedException();
        }
    }

    void validateNotExpired(){
        if (coupon.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new CouponExpiredException();
        }
    }

}
