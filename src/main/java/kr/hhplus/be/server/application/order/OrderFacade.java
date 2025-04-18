package kr.hhplus.be.server.application.order;


import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCouponInfo;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentService;
import kr.hhplus.be.server.domain.point.PointService;
import kr.hhplus.be.server.domain.product.ProductCommand;
import kr.hhplus.be.server.domain.product.ProductService;
import kr.hhplus.be.server.domain.product.ProductValidation;
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

    private final PaymentService paymentService;

    private final PointService pointService;

    @Transactional
    public void createOrderWithPayment(OrderCriteria orderCriteria) {

        // 사용자 조회
        User user = userService.getUserInfo(orderCriteria.getUserId());

        // 쿠폰 확인
        UserCouponInfo userCoupon = couponService.findUserCoupon(orderCriteria.getUserId(), orderCriteria.getCouponId());

        // 주문상품 list를 만들기 위해 item 작성
        List<OrderCommand> item = OrderCommand.toCommand(orderCriteria.getItems());

        // 주문상품 item에서 추가적으로 필요한 정보를 위해 orderItemInfoList 생성
        List<OrderCommand.OrderItemDetail> orderItemInfoList = new ArrayList<>();

        // 상품재고 확인 및 재고차감을 위해 생성
        List<ProductValidation> productValidationList = new ArrayList<>();

        for (OrderCommand order : item) {

            // 재고 확인
            ProductValidation productValidation = productService.checkProductAvailability(ProductCommand.toCommand(order));

            // 주문 항목 정보 생성
            orderItemInfoList.add( new OrderCommand.OrderItemDetail(order.getProductId(), order.getOptionId(), order.getQty(), productValidation.getPrice()));
            productValidationList.add(productValidation);

        }

        // 주문서 저장
        Order order = orderService.createOrder(orderItemInfoList, orderCriteria.getUserId());

        // 결제
        Payment payment = paymentService.createPayment(user, order.getOrderId(), userCoupon, orderItemInfoList);

        // 포인트 차감
        pointService.usePoint(user, payment.getPaidPay());

        // 쿠폰 사용
        pointService.usePoint(user, payment.getPaidPay());

        // 쿠폰 사용
        couponService.useCoupon(userCoupon);

        // 재고 차감
        for (ProductValidation validation : productValidationList) {

            productService.decreaseStock(validation);

        }


    }


}
