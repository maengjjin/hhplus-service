package kr.hhplus.be.server.application.order;


import java.util.List;
import kr.hhplus.be.server.application.order.OrderResponse.OrderItems;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand.OrderItem;
import kr.hhplus.be.server.domain.order.OrderPaymentCalculator;
import kr.hhplus.be.server.domain.order.OrderPaymentCalculator.PriceSummary;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductDTO.ProductOrderResult;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderFacade {

    private final UserService userService;

    private final ProductService productService;

    private final OrderService orderService;

    private final CouponService couponService;

    private final PointService pointService;

    private final PaymentService paymentService;

    @Transactional
    public OrderResponse createOrder(OrderCriteria orderCriteria) {

        // 사용자 조회
        User user = userService.getUserInfo(orderCriteria.getUserId());

        // 쿠폰 확인
        UserCoupon userCoupon = couponService.getValidUserCouponForOrder(orderCriteria.getUserId(), orderCriteria.getCouponId());

        // 상품 확인
        List<ProductOrderResult> orderItems = productService.prepareOrderItems(ProductCommand.toCommand(orderCriteria.getItems()));

        // 주문서 저장
        Order order = orderService.createOrder(OrderItem.toCommand(orderItems), orderCriteria.getUserId());

        // 결제 로직
        Payment payment = createPayment(order.getOrderId(), user, userCoupon, orderItems);

        return new OrderResponse(order.getOrderNo(), OrderItems.of(orderItems), payment);

    }


    @Transactional
    public Payment createPayment(long orderId, User user, UserCoupon userCoupon, List<ProductOrderResult> productOrderResultList) {


        OrderPaymentCalculator calculator = new OrderPaymentCalculator();

        // 계산 금액
        PriceSummary priceSummary = calculator.calculateOrderAmount(user.getPoint(), userCoupon, productOrderResultList);

        // 쿠폰 사용
        couponService.useCoupon(userCoupon);

        // 포인트 차감
        pointService.usePoint(user, priceSummary.getTotalPrice());


       return  paymentService.createPayment(user, orderId, userCoupon.getCoupon().getCouponId(), priceSummary);


    }


}
