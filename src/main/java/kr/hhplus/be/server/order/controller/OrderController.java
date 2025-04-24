package kr.hhplus.be.server.order.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.common.ApiResult;
import kr.hhplus.be.server.order.request.OrderRequest;
import kr.hhplus.be.server.order.request.SwaggerOrderRequest;
import kr.hhplus.be.server.order.response.OrderResponse;
import kr.hhplus.be.server.order.response.SwaggerOrderResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Tag(name = "주문/결제 API")
public class OrderController {

    @Operation(summary = "결제 API", description = "주문 결제",
        requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(content = @Content(schema = @Schema(implementation = SwaggerOrderRequest.class))),
        responses = @ApiResponse(responseCode = "200", description = "success", content = @Content(schema = @Schema(implementation = SwaggerOrderResponse.class))))
    @PostMapping("/payment")
    public ApiResult<OrderResponse> orderProduct(@RequestBody OrderRequest orderRequest){
        return ApiResult.success(OrderResponse.of(1L, 58000, "20250404143000"));
    }



}
