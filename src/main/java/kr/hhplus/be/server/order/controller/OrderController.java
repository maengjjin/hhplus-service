package kr.hhplus.be.server.order.controller;


import kr.hhplus.be.server.common.ApiResponse;
import kr.hhplus.be.server.order.request.OrderRequest;
import kr.hhplus.be.server.order.response.OrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
public class OrderController {

    @PostMapping("/payment")
    public ApiResponse<OrderResponse> orderProduct(@RequestBody OrderRequest orderRequest){

        return ApiResponse.success(OrderResponse.of(1L, 58000, "20250404143000"));
    }



}
