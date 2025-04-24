package kr.hhplus.be.server.web.order.controller;


import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.order.OrderService;
import kr.hhplus.be.server.web.ApiResult;
import kr.hhplus.be.server.web.order.request.OrderRequest;

import kr.hhplus.be.server.application.order.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Tag(name = "주문/결제 API")
@RequiredArgsConstructor
public class OrderController {

    private final OrderFacade orderFacade;

    @PostMapping("/payment")
    public ApiResult<OrderResponse> orderProduct(@RequestBody OrderRequest orderRequest){
        OrderResponse response = orderFacade.createOrderWithPayment(OrderCriteria.of(orderRequest));
        return ApiResult.success(response);
    }



}
