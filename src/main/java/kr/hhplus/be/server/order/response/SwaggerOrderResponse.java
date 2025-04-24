package kr.hhplus.be.server.order.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SwaggerOrderResponse {

    @Schema(description = "code", example = "200")
    private int code;

    @Schema(description = "message", example = "success")
    private String message;

    @Schema(
        description = "주문 결과 데이터",
        example = "{\"userId\":1,\"totalAmount\":27000,\"orderAt\":20250404091000}"
    )
    private List<OrderResponse> data;

}
