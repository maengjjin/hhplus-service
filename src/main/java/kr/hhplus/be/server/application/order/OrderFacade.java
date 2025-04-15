package kr.hhplus.be.server.application.order;

import static kr.hhplus.be.server.domain.product.ProductCommand.*;

import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.order.OrderCommand;
import kr.hhplus.be.server.domain.order.OrderItemInfo;
import kr.hhplus.be.server.domain.order.OrderService;
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

    @Transactional
    public void createOrderWithPayment(OrderCriteria orderCriteria){


        // 사용자 조회
        User user = userService.getUserInfo(orderCriteria.getUserId());

        // 쿠폰 확인


        // 상품 확인
        List<OrderItemInfo> list = new ArrayList<>();


        List<OrderCommand.OrderItem> item = OrderCommand.OrderItem.toCommand(orderCriteria.getItems());

        for (OrderCommand.OrderItem order : item) {

            ProductValidation  productValidation =  productService.checkProductAvailability(Product.toCommand(order));

            // 주문 항목 정보 생성
            OrderItemInfo orderItemInfo = new OrderItemInfo(order.getProductId(), order.getOptionId(), order.getQty(), productValidation.getPrice());

            list.add(orderItemInfo);

        }

        // 결제




    }



}
