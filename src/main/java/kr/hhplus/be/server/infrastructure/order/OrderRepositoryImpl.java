package kr.hhplus.be.server.infrastructure.order;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

    private final OrderDetailJpaRepository orderDetailJpaRepository;

    private final OrderJpaRepository orderJpaRepository;


    @Override
    public Order saveOrder(Order order) {
        return orderJpaRepository.save(order);
    }

    @Override
    public void saveOrderDetail(OrderDetail detail) {
        orderDetailJpaRepository.save(detail);
    }
}
